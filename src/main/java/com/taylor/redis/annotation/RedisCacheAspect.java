package com.taylor.redis.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taylor.common.jackson.JacksonUtil;
import com.taylor.redis.service.RedisClientService;

import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class RedisCacheAspect {

	private static final int ONEDAY = 60 * 60 * 24; // 24h

	@Autowired
	private RedisClientService redisClientService;

	@Around("@annotation(RedisCacheGet)")
	public Object cacheGet(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();
		log.info("redisCache get method.name {}", method.getName());
		return goRedisCacheGet(args, method, joinPoint);

	}

	@Around("@annotation(RedisCacheClean)")
	public Object cacheClean(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();
		log.info("redisCache clean method.name {}", method.getName());
		return goRedisCacheClean(args, method, joinPoint);

	}

	/**
	 * @param args
	 * @param method
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 * @description redisCacheGet注解解析方法
	 */
	public Object goRedisCacheGet(Object[] args, Method method, ProceedingJoinPoint joinPoint) throws Throwable {

		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();

		// 获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		// 把方法参数放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		// 如果有这个注解，则获取注解类
		RedisCacheGet methodType = method.getAnnotation(RedisCacheGet.class);
		String key = parser.parseExpression(methodType.key()).getValue(context, String.class);
		log.info("redisCache key {}", key);
		if (methodType.dataType() == RedisCacheGet.DataType.JSON) {// JSON形式保存
			if (methodType.force() == true) {// 强制更新数据
				Object object = joinPoint.proceed(args);
				if (object != null) {
					setRedisValueJson(methodType, key, object);
				}
				// 返回值类型
				return object;
			} else {
				if (redisClientService.exists(key)) {
					String json = redisClientService.get(key);

					try {
						log.debug("redis" + method.getReturnType() + "key{}" + key + "\t value{}" + json);
						if (method.getGenericReturnType().toString().contains("List") || method.getGenericReturnType().toString().contains("Set") || method.getGenericReturnType().toString().contains("Map")) {
							ObjectMapper mapper = JacksonUtil.getInstance().getObjectMapper();
							String clazzName = ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0].toString().substring(6);
							Object o = Class.forName(clazzName).newInstance();
							JavaType javaType = getCollectionType(mapper, method.getReturnType(), o.getClass());
							return JacksonUtil.getInstance().json2JavaType(json, javaType);
						} else {
							return JacksonUtil.getInstance().json2Bean(json, method.getReturnType());
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
					return null;
				} else {// 查询数据，缓存，返回对象
					Object object = joinPoint.proceed(args);
					if (object != null) {
						setRedisValueJson(methodType, key, object);
					}
					return object;
				}
			}
		} else {// CLASS形式保存
			if (methodType.force() == true) {// 强制更新数据
				Object object = joinPoint.proceed(args);
				if (object != null) {
					setRedisValueClass(methodType, key, object);
				}
				// 返回值类型
				return object;
			} else {
				if (redisClientService.exists(key)) {// 对象存在直接返回
					return redisClientService.getObject(key);
				} else {// 查询数据，缓存，返回对象
					Object object = joinPoint.proceed(args);
					if (object != null) {
						setRedisValueClass(methodType, key, object);
					}
					return object;
				}
			}
		}
	}

	/**
	 * @param methodType
	 * @param key
	 * @param object
	 */
	private void setRedisValueClass(RedisCacheGet methodType, String key, Object object) {
		// 设置缓存时长
		if (methodType.expire() == 0) {
			redisClientService.set(key, object);
		} else if (methodType.expire() == 1) {
			redisClientService.set(key, ONEDAY);
		} else {
			redisClientService.set(key, object);
		}
	}

	/**
	 * @param methodType
	 * @param key
	 * @param object
	 */
	private void setRedisValueJson(RedisCacheGet methodType, String key, Object object) {

		String jsonStr = JacksonUtil.getInstance().bean2Json(object);
		if (methodType.expire() == 0) {// 0:永不过期
			redisClientService.set(key, jsonStr);
		} else if (methodType.expire() == 1) {// 1:过期时间为24h
			redisClientService.set(key, ONEDAY);
		} else {// 手动指定
			redisClientService.set(key, methodType.expire());
		}

	}

	/**
	 * @param args
	 * @param method
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 * @description redisCache 清除方法
	 */
	public Object goRedisCacheClean(Object[] args, Method method, ProceedingJoinPoint joinPoint) throws Throwable {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();

		// 获取被拦截方法参数名列表(使用Spring支持类库)
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paraNameArr = u.getParameterNames(method);
		// 把方法参数放入SPEL上下文中
		for (int i = 0; i < paraNameArr.length; i++) {
			context.setVariable(paraNameArr[i], args[i]);
		}
		// 如果有这个注解，则获取注解类
		Object object = joinPoint.proceed(args);

		// 如果有这个注解，则获取注解类
		RedisCacheClean methodType = method.getAnnotation(RedisCacheClean.class);
		if (methodType.isBatch()) {
			for (String str : methodType.key()) {
				String keyStr = parser.parseExpression(str).getValue(context, String.class);
				String[] keys = keyStr.split(",");
				for (String key : keys) {
					redisClientService.del(key);
				}
			}
		} else {
			for (String str : methodType.key()) {
				String key = parser.parseExpression(str).getValue(context, String.class);
				redisClientService.del(key);
			}
		}
		return object;
	}

	private static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}
}
