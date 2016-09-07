package com.taylor.redis.common.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.taylor.redis.common.shard.RedisMasterSlaverGroup;
import com.taylor.redis.common.shard.RedisShardSplit;
import com.taylor.redis.common.shard.Sharded;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

public class RedisBinaryClient extends Sharded implements BinaryJedisCommands {

	public RedisBinaryClient() {
		super();
	}

	public RedisBinaryClient(RedisShardSplit<JedisPool, RedisMasterSlaverGroup> splitor) {
		super(splitor);
	}

	protected Jedis create(JedisShardInfo shard) {
		return new Jedis(shard);
	}

	public String set(final byte[] key, final byte[] value) {

		return doOperation(key, new JedisCallBack<String>() {

			public String doBiz(Jedis j) {
				return j.set(key, value);
			}

			public String getOperationName() {
				return "set";
			}

		});
	}

	public byte[] get(final byte[] key) {

		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.get(key);
			}

			public String getOperationName() {
				return "get";
			}
		});
	}

	public Boolean exists(final byte[] key) {
		return doOperation(key, new JedisCallBack<Boolean>() {

			public Boolean doBiz(Jedis j) {
				return j.exists(key);
			}

			public String getOperationName() {
				return "exists";
			}
		});
	}

	public String type(final byte[] key) {
		return doOperation(key, new JedisCallBack<String>() {

			public String doBiz(Jedis j) {
				return j.type(key);
			}

			public String getOperationName() {
				return "type";
			}
		});
	}

	public Long expire(final byte[] key, final int seconds) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.expire(key, seconds);
			}

			public String getOperationName() {
				return "expire";
			}
		});
	}

	public Long expireAt(final byte[] key, final long unixTime) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.expireAt(key, unixTime);
			}

			public String getOperationName() {
				return "expireAt";
			}
		});
	}

	public Long ttl(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.ttl(key);
			}

			public String getOperationName() {
				return "ttl";
			}
		});
	}

	public byte[] getSet(final byte[] key, final byte[] value) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.getSet(key, value);
			}

			public String getOperationName() {
				return "getSet";
			}
		});
	}

	public Long setnx(final byte[] key, final byte[] value) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.setnx(key, value);
			}

			public String getOperationName() {
				return "setnx";
			}
		});
	}

	public String setex(final byte[] key, final int seconds, final byte[] value) {
		return doOperation(key, new JedisCallBack<String>() {

			public String doBiz(Jedis j) {
				return j.setex(key, seconds, value);
			}

			public String getOperationName() {
				return "setex";
			}
		});
	}

	public Long decrBy(final byte[] key, final long integer) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.decrBy(key, integer);
			}

			public String getOperationName() {
				return "decrBy";
			}
		});
	}

	public Long decr(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.decr(key);
			}

			public String getOperationName() {
				return "decr";
			}
		});
	}

	public Long del(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.del(key);
			}

			public String getOperationName() {
				return "del";
			}
		});
	}

	public Long incrBy(final byte[] key, final long integer) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.incrBy(key, integer);
			}

			public String getOperationName() {
				return "incrBy";
			}
		});
	}

	public Long incr(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.incr(key);
			}

			public String getOperationName() {
				return "incr";
			}
		});
	}

	public Long append(final byte[] key, final byte[] value) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.append(key, value);
			}

			public String getOperationName() {
				return "append";
			}
		});
	}

	public byte[] substr(final byte[] key, final int start, final int end) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.substr(key, start, end);
			}

			public String getOperationName() {
				return "substr";
			}
		});
	}

	public Long hset(final byte[] key, final byte[] field, final byte[] value) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.hset(key, field, value);
			}

			public String getOperationName() {
				return "hset";
			}
		});
	}

	public byte[] hget(final byte[] key, final byte[] field) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.hget(key, field);
			}

			public String getOperationName() {
				return "hget";
			}
		});
	}

	public Long hsetnx(final byte[] key, final byte[] field, final byte[] value) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.hsetnx(key, field, value);
			}

			public String getOperationName() {
				return "hsetnx";
			}
		});
	}

	public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
		return doOperation(key, new JedisCallBack<String>() {

			public String doBiz(Jedis j) {
				return j.hmset(key, hash);
			}

			public String getOperationName() {
				return "hmset";
			}
		});
	}

	public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
		return doOperation(key, new JedisCallBack<List<byte[]>>() {

			public List<byte[]> doBiz(Jedis j) {
				return j.hmget(key, fields);
			}

			public String getOperationName() {
				return "hmget";
			}
		});
	}

	public Long hincrBy(final byte[] key, final byte[] field, final long value) {

		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.hincrBy(key, field, value);
			}

			public String getOperationName() {
				return "hincrBy";
			}
		});
	}

	public Boolean hexists(final byte[] key, final byte[] field) {
		return doOperation(key, new JedisCallBack<Boolean>() {

			public Boolean doBiz(Jedis j) {
				return j.hexists(key, field);
			}

			public String getOperationName() {
				return "hexists";
			}
		});
	}

	public Long hdel(final byte[] key, final byte[]... fields) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.hdel(key, fields);
			}

			public String getOperationName() {
				return "hdel";
			}
		});
	}

	public Long hlen(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.hlen(key);
			}

			public String getOperationName() {
				return "hlen";
			}
		});
	}

	public Set<byte[]> hkeys(final byte[] key) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.hkeys(key);
			}

			public String getOperationName() {
				return "hkeys";
			}
		});
	}

	public Collection<byte[]> hvals(final byte[] key) {
		return doOperation(key, new JedisCallBack<Collection<byte[]>>() {

			public Collection<byte[]> doBiz(Jedis j) {
				return j.hvals(key);
			}

			public String getOperationName() {
				return "hvals";
			}
		});
	}

	public Map<byte[], byte[]> hgetAll(final byte[] key) {
		return doOperation(key, new JedisCallBack<Map<byte[], byte[]>>() {

			public Map<byte[], byte[]> doBiz(Jedis j) {
				return j.hgetAll(key);
			}

			public String getOperationName() {
				return "hgetAll";
			}
		});
	}

	public Long rpush(final byte[] key, final byte[]... strings) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.rpush(key, strings);
			}

			public String getOperationName() {
				return "rpush";
			}
		});
	}

	public Long lpush(final byte[] key, final byte[]... strings) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.lpush(key, strings);
			}

			public String getOperationName() {
				return "lpush";
			}
		});
	}

	public Long strlen(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.strlen(key);
			}

			public String getOperationName() {
				return "strlen";
			}
		});
	}

	public Long persist(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.persist(key);
			}

			public String getOperationName() {
				return "persist";
			}
		});
	}

	public Long llen(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.llen(key);
			}

			public String getOperationName() {
				return "llen";
			}
		});
	}

	public List<byte[]> lrange(final byte[] key, final int start, final int end) {
		return doOperation(key, new JedisCallBack<List<byte[]>>() {

			public List<byte[]> doBiz(Jedis j) {
				return j.lrange(key, start, end);
			}

			public String getOperationName() {
				return "lrange";
			}
		});
	}

	public String ltrim(final byte[] key, final int start, final int end) {
		return doOperation(key, new JedisCallBack<String>() {

			public String doBiz(Jedis j) {
				return j.ltrim(key, start, end);
			}

			public String getOperationName() {
				return "ltrim";
			}
		});
	}

	public byte[] lpop(final byte[] key) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.lpop(key);
			}

			public String getOperationName() {
				return "lpop";
			}
		});
	}

	public byte[] rpop(final byte[] key) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.rpop(key);
			}

			public String getOperationName() {
				return "rpop";
			}
		});
	}

	public Long sadd(final byte[] key, final byte[]... members) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.sadd(key, members);
			}

			public String getOperationName() {
				return "sadd";
			}
		});
	}

	public Set<byte[]> smembers(final byte[] key) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.smembers(key);
			}

			public String getOperationName() {
				return "smembers";
			}
		});
	}

	public Long srem(final byte[] key, final byte[]... members) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.srem(key, members);
			}

			public String getOperationName() {
				return "srem";
			}
		});
	}

	public byte[] spop(final byte[] key) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.spop(key);
			}

			public String getOperationName() {
				return "spop";
			}
		});
	}

	public Long scard(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.scard(key);
			}

			public String getOperationName() {
				return "scard";
			}
		});
	}

	public Boolean sismember(final byte[] key, final byte[] member) {
		return doOperation(key, new JedisCallBack<Boolean>() {

			public Boolean doBiz(Jedis j) {
				return j.sismember(key, member);
			}

			public String getOperationName() {
				return "sismember";
			}
		});
	}

	public byte[] srandmember(final byte[] key) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.srandmember(key);
			}

			public String getOperationName() {
				return "srandmember";
			}
		});
	}

	public Long zadd(final byte[] key, final double score, final byte[] member) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zadd(key, score, member);
			}

			public String getOperationName() {
				return "zadd";
			}
		});
	}

	public Long zadd(final byte[] key, final Map<byte[], Double> scoreMembers) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zadd(key, scoreMembers);
			}

			public String getOperationName() {
				return "zadd";
			}
		});
	}

	public Set<byte[]> zrange(final byte[] key, final int start, final int end) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrange(key, start, end);
			}

			public String getOperationName() {
				return "zrange";
			}
		});
	}

	public Long zrem(final byte[] key, final byte[]... members) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zrem(key, members);
			}

			public String getOperationName() {
				return "zrem";
			}
		});
	}

	public Double zincrby(final byte[] key, final double score, final byte[] member) {
		return doOperation(key, new JedisCallBack<Double>() {

			public Double doBiz(Jedis j) {
				return j.zincrby(key, score, member);
			}

			public String getOperationName() {
				return "zincrby";
			}
		});
	}

	public Long zrank(final byte[] key, final byte[] member) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zrank(key, member);
			}

			public String getOperationName() {
				return "zrank";
			}
		});
	}

	public Long zrevrank(final byte[] key, final byte[] member) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zrevrank(key, member);
			}

			public String getOperationName() {
				return "zrevrank";
			}
		});
	}

	public Set<byte[]> zrevrange(final byte[] key, final int start, final int end) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrevrange(key, start, end);
			}

			public String getOperationName() {
				return "zrevrange";
			}
		});
	}

	public Set<Tuple> zrangeWithScores(final byte[] key, final int start, final int end) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrangeWithScores(key, start, end);
			}

			public String getOperationName() {
				return "zrangeWithScores";
			}
		});
	}

	public Set<Tuple> zrevrangeWithScores(final byte[] key, final int start, final int end) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrevrangeWithScores(key, start, end);
			}

			public String getOperationName() {
				return "zrevrangeWithScores";
			}
		});
	}

	public Long zcard(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zcard(key);
			}

			public String getOperationName() {
				return "zcard";
			}
		});
	}

	public Double zscore(final byte[] key, final byte[] member) {
		return doOperation(key, new JedisCallBack<Double>() {

			public Double doBiz(Jedis j) {
				return j.zscore(key, member);
			}

			public String getOperationName() {
				return "zscore";
			}
		});
	}

	public List<byte[]> sort(final byte[] key) {
		return doOperation(key, new JedisCallBack<List<byte[]>>() {

			public List<byte[]> doBiz(Jedis j) {
				return j.sort(key);
			}

			public String getOperationName() {
				return "sort";
			}
		});
	}

	public List<byte[]> sort(final byte[] key, final SortingParams sortingParameters) {
		return doOperation(key, new JedisCallBack<List<byte[]>>() {

			public List<byte[]> doBiz(Jedis j) {
				return j.sort(key, sortingParameters);
			}

			public String getOperationName() {
				return "sort";
			}
		});
	}

	public Long zcount(final byte[] key, final double min, final double max) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zcount(key, min, max);
			}

			public String getOperationName() {
				return "zcount";
			}
		});
	}

	public Long zcount(final byte[] key, final byte[] min, final byte[] max) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zcount(key, min, max);
			}

			public String getOperationName() {
				return "zcount";
			}
		});
	}

	public Set<byte[]> zrangeByScore(final byte[] key, final double min, final double max) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrangeByScore(key, min, max);
			}

			public String getOperationName() {
				return "zrangeByScore";
			}
		});
	}

	public Set<byte[]> zrangeByScore(final byte[] key, final double min, final double max, final int offset, final int count) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrangeByScore(key, min, max, offset, count);
			}

			public String getOperationName() {
				return "zrangeByScore";
			}
		});
	}

	public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final double min, final double max) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrangeByScoreWithScores(key, min, max);
			}

			public String getOperationName() {
				return "zrangeByScoreWithScores";
			}
		});
	}

	public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final double min, final double max, final int offset, final int count) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrangeByScoreWithScores(key, min, max, offset, count);
			}

			public String getOperationName() {
				return "zrangeByScoreWithScores";
			}
		});
	}

	public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min, final byte[] max) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrangeByScore(key, min, max);
			}

			public String getOperationName() {
				return "zrangeByScore";
			}
		});
	}

	public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrangeByScoreWithScores(key, min, max);
			}

			public String getOperationName() {
				return "zrangeByScoreWithScores";
			}
		});
	}

	public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrangeByScoreWithScores(key, min, max, offset, count);
			}

			public String getOperationName() {
				return "zrangeByScoreWithScores";
			}
		});
	}

	public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrangeByScore(key, min, max, offset, count);
			}

			public String getOperationName() {
				return "zrangeByScore";
			}
		});
	}

	public Set<byte[]> zrevrangeByScore(final byte[] key, final double max, final double min) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrevrangeByScore(key, max, min);
			}

			public String getOperationName() {
				return "zrevrangeByScore";
			}
		});
	}

	public Set<byte[]> zrevrangeByScore(final byte[] key, final double max, final double min, final int offset, final int count) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrevrangeByScore(key, max, min, offset, count);
			}

			public String getOperationName() {
				return "zrevrangeByScore";
			}
		});
	}

	public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrevrangeByScoreWithScores(key, max, min);
			}

			public String getOperationName() {
				return "zrevrangeByScoreWithScores";
			}
		});
	}

	public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min, final int offset, final int count) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
			}

			public String getOperationName() {
				return "zrevrangeByScoreWithScores";
			}
		});
	}

	public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrevrangeByScore(key, max, min);
			}

			public String getOperationName() {
				return "zrevrangeByScore";
			}
		});
	}

	public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
		return doOperation(key, new JedisCallBack<Set<byte[]>>() {

			public Set<byte[]> doBiz(Jedis j) {
				return j.zrevrangeByScore(key, max, min, offset, count);
			}

			public String getOperationName() {
				return "zrevrangeByScore";
			}
		});
	}

	public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrevrangeByScoreWithScores(key, max, min);
			}

			public String getOperationName() {
				return "zrevrangeByScoreWithScores";
			}
		});
	}

	public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
		return doOperation(key, new JedisCallBack<Set<Tuple>>() {

			public Set<Tuple> doBiz(Jedis j) {
				return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
			}

			public String getOperationName() {
				return "zrevrangeByScoreWithScores";
			}
		});
	}

	public Long zremrangeByRank(final byte[] key, final int start, final int end) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zremrangeByRank(key, start, end);
			}

			public String getOperationName() {
				return "zremrangeByRank";
			}
		});
	}

	public Long zremrangeByScore(final byte[] key, final double start, final double end) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zremrangeByScore(key, start, end);
			}

			public String getOperationName() {
				return "zremrangeByScore";
			}
		});
	}

	public Long zremrangeByScore(final byte[] key, final byte[] start, final byte[] end) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.zremrangeByScore(key, start, end);
			}

			public String getOperationName() {
				return "zremrangeByScore";
			}
		});
	}

	public Long linsert(final byte[] key, final LIST_POSITION where, final byte[] pivot, final byte[] value) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.linsert(key, where, pivot, value);
			}

			public String getOperationName() {
				return "linsert";
			}
		});
	}

	public Long objectRefcount(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.objectRefcount(key);
			}

			public String getOperationName() {
				return "objectRefcount";
			}
		});
	}

	public byte[] objectEncoding(final byte[] key) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.objectEncoding(key);
			}

			public String getOperationName() {
				return "objectEncoding";
			}
		});
	}

	public Long objectIdletime(final byte[] key) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.objectIdletime(key);
			}

			public String getOperationName() {
				return "objectIdletime";
			}
		});
	}

	public Boolean setbit(final String key, final long offset, final boolean value) {
		return doOperation(key, new JedisCallBack<Boolean>() {

			public Boolean doBiz(Jedis j) {
				return j.setbit(key, offset, value);
			}

			public String getOperationName() {
				return "setbit";
			}
		});
	}

	public Boolean setbit(final byte[] key, final long offset, final byte[] value) {
		return doOperation(key, new JedisCallBack<Boolean>() {

			public Boolean doBiz(Jedis j) {
				return j.setbit(key, offset, value);
			}

			public String getOperationName() {
				return "setbit";
			}
		});
	}

	public Boolean getbit(final byte[] key, final long offset) {
		return doOperation(key, new JedisCallBack<Boolean>() {

			public Boolean doBiz(Jedis j) {
				return j.getbit(key, offset);
			}

			public String getOperationName() {
				return "getbit";
			}
		});
	}

	public Long setrange(final byte[] key, final long offset, final byte[] value) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.setrange(key, offset, value);
			}

			public String getOperationName() {
				return "setrange";
			}
		});
	}

	public Long move(final byte[] key, final int dbIndex) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.move(key, dbIndex);
			}

			public String getOperationName() {
				return "move";
			}
		});
	}

	public byte[] echo(final byte[] arg) {
		return doOperation(arg, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.echo(arg);
			}

			public String getOperationName() {
				return "echo";
			}
		});
	}

	public byte[] lindex(final byte[] key, final int index) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.lindex(key, index);
			}

			public String getOperationName() {
				return "lindex";
			}
		});
	}

	public Long lpushx(final byte[] key, final byte[] string) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.lpushx(key, string);
			}

			public String getOperationName() {
				return "lpushx";
			}
		});
	}

	public byte[] getrange(final byte[] key, final long startOffset, final long endOffset) {
		return doOperation(key, new JedisCallBack<byte[]>() {

			public byte[] doBiz(Jedis j) {
				return j.getrange(key, startOffset, endOffset);
			}

			public String getOperationName() {
				return "getrange";
			}
		});
	}

	public Long lrem(final byte[] key, final int count, final byte[] value) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.lrem(key, count, value);
			}

			public String getOperationName() {
				return "lrem";
			}
		});
	}

	public String lset(final byte[] key, final int index, final byte[] value) {
		return doOperation(key, new JedisCallBack<String>() {

			public String doBiz(Jedis j) {
				return j.lset(key, index, value);
			}

			public String getOperationName() {
				return "lset";
			}
		});
	}

	public Long rpushx(final byte[] key, final byte[] string) {
		return doOperation(key, new JedisCallBack<Long>() {

			public Long doBiz(Jedis j) {
				return j.rpushx(key, string);
			}

			public String getOperationName() {
				return "rpushx";
			}
		});
	}

	@Override
	public Long bitcount(byte[] arg0) {
		return null;
	}

	@Override
	public Long bitcount(byte[] arg0, long arg1, long arg2) {
		return null;
	}

	@Override
	public List<byte[]> bitfield(byte[] arg0, byte[]... arg1) {
		return null;
	}

	@Override
	public List<byte[]> blpop(byte[] arg0) {
		return null;
	}

	@Override
	public List<byte[]> brpop(byte[] arg0) {
		return null;
	}

	@Override
	public Long geoadd(byte[] arg0, Map<byte[], GeoCoordinate> arg1) {
		return null;
	}

	@Override
	public Long geoadd(byte[] arg0, double arg1, double arg2, byte[] arg3) {
		return null;
	}

	@Override
	public Double geodist(byte[] arg0, byte[] arg1, byte[] arg2) {
		return null;
	}

	@Override
	public Double geodist(byte[] arg0, byte[] arg1, byte[] arg2, GeoUnit arg3) {
		return null;
	}

	@Override
	public List<byte[]> geohash(byte[] arg0, byte[]... arg1) {
		return null;
	}

	@Override
	public List<GeoCoordinate> geopos(byte[] arg0, byte[]... arg1) {
		return null;
	}

	@Override
	public List<GeoRadiusResponse> georadius(byte[] arg0, double arg1, double arg2, double arg3, GeoUnit arg4) {
		return null;
	}

	@Override
	public List<GeoRadiusResponse> georadius(byte[] arg0, double arg1, double arg2, double arg3, GeoUnit arg4, GeoRadiusParam arg5) {
		return null;
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(byte[] arg0, byte[] arg1, double arg2, GeoUnit arg3) {
		return null;
	}

	@Override
	public List<GeoRadiusResponse> georadiusByMember(byte[] arg0, byte[] arg1, double arg2, GeoUnit arg3, GeoRadiusParam arg4) {
		return null;
	}

	@Override
	public Double hincrByFloat(byte[] arg0, byte[] arg1, double arg2) {
		return null;
	}

	@Override
	public ScanResult<Entry<byte[], byte[]>> hscan(byte[] arg0, byte[] arg1) {
		return null;
	}

	@Override
	public ScanResult<Entry<byte[], byte[]>> hscan(byte[] arg0, byte[] arg1, ScanParams arg2) {
		return null;
	}

	@Override
	public Double incrByFloat(byte[] arg0, double arg1) {
		return null;
	}

	@Override
	public byte[] lindex(byte[] arg0, long arg1) {
		return null;
	}

	@Override
	public Long lpushx(byte[] arg0, byte[]... arg1) {
		return null;
	}

	@Override
	public List<byte[]> lrange(byte[] arg0, long arg1, long arg2) {
		return null;
	}

	@Override
	public Long lrem(byte[] arg0, long arg1, byte[] arg2) {
		return null;
	}

	@Override
	public String lset(byte[] arg0, long arg1, byte[] arg2) {
		return null;
	}

	@Override
	public String ltrim(byte[] arg0, long arg1, long arg2) {
		return null;
	}

	@Override
	public Long pexpire(String arg0, long arg1) {
		return null;
	}

	@Override
	public Long pexpire(byte[] arg0, long arg1) {
		return null;
	}

	@Override
	public Long pexpireAt(byte[] arg0, long arg1) {
		return null;
	}

	@Override
	public Long pfadd(byte[] arg0, byte[]... arg1) {
		return null;
	}

	@Override
	public long pfcount(byte[] arg0) {
		return 0;
	}

	@Override
	public Long rpushx(byte[] arg0, byte[]... arg1) {
		return null;
	}

	@Override
	public String set(byte[] arg0, byte[] arg1, byte[] arg2) {
		return null;
	}

	@Override
	public String set(byte[] arg0, byte[] arg1, byte[] arg2, byte[] arg3, long arg4) {
		return null;
	}

	@Override
	public Boolean setbit(byte[] arg0, long arg1, boolean arg2) {
		return null;
	}

	@Override
	public Set<byte[]> spop(byte[] arg0, long arg1) {
		return null;
	}

	@Override
	public List<byte[]> srandmember(byte[] arg0, int arg1) {
		return null;
	}

	@Override
	public ScanResult<byte[]> sscan(byte[] arg0, byte[] arg1) {
		return null;
	}

	@Override
	public ScanResult<byte[]> sscan(byte[] arg0, byte[] arg1, ScanParams arg2) {
		return null;
	}

	@Override
	public Long zadd(byte[] arg0, Map<byte[], Double> arg1, ZAddParams arg2) {
		return null;
	}

	@Override
	public Long zadd(byte[] arg0, double arg1, byte[] arg2, ZAddParams arg3) {
		return null;
	}

	@Override
	public Double zincrby(byte[] arg0, double arg1, byte[] arg2, ZIncrByParams arg3) {
		return null;
	}

	@Override
	public Long zlexcount(byte[] arg0, byte[] arg1, byte[] arg2) {
		return null;
	}

	@Override
	public Set<byte[]> zrange(byte[] arg0, long arg1, long arg2) {
		return null;
	}

	@Override
	public Set<byte[]> zrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2) {
		return null;
	}

	@Override
	public Set<byte[]> zrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {
		return null;
	}

	@Override
	public Set<Tuple> zrangeWithScores(byte[] arg0, long arg1, long arg2) {
		return null;
	}

	@Override
	public Long zremrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2) {
		return null;
	}

	@Override
	public Long zremrangeByRank(byte[] arg0, long arg1, long arg2) {
		return null;
	}

	@Override
	public Set<byte[]> zrevrange(byte[] arg0, long arg1, long arg2) {
		return null;
	}

	@Override
	public Set<byte[]> zrevrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2) {
		return null;
	}

	@Override
	public Set<byte[]> zrevrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {
		return null;
	}

	@Override
	public Set<Tuple> zrevrangeWithScores(byte[] arg0, long arg1, long arg2) {
		return null;
	}

	@Override
	public ScanResult<Tuple> zscan(byte[] arg0, byte[] arg1) {
		return null;
	}

	@Override
	public ScanResult<Tuple> zscan(byte[] arg0, byte[] arg1, ScanParams arg2) {
		return null;
	}
}
