package com.taylor.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taylor.redis.common.client.RedisExtAPI;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

public interface RedisClientService extends JedisCommands, RedisExtAPI {

	public String set(final String key, final String value);

	public String get(final String key);

	public String echo(final String key);

	public Boolean exists(final String key);

	public String type(final String key);

	public Long expire(final String key, final int seconds);

	public Long expireAt(final String key, final long unixTime);

	public Long ttl(final String key);

	public Boolean setbit(final String key, final long offset, final boolean value);

	public Boolean setbit(final String key, final long offset, final String value);

	public Boolean getbit(final String key, final long offset);

	public Long setrange(final String key, final long offset, final String value);

	public String getrange(final String key, final long startOffset, final long endOffset);

	public String getSet(final String key, final String value);

	public Long setnx(final String key, final String value);

	public String setex(final String key, final int seconds, final String value);

	public Long decrBy(final String key, final long integer);

	public Long decr(final String key);

	public Long incrBy(final String key, final long integer);

	public Long incr(final String key);

	public Long append(final String key, final String value);

	public String substr(final String key, final int start, final int end);

	public Long hset(final String key, final String field, final String value);

	public String hget(final String key, final String field);

	public Long hsetnx(final String key, final String field, final String value);

	public String hmset(final String key, final Map<String, String> hash);

	public List<String> hmget(final String key, final String... fields);

	public Long hincrBy(final String key, final String field, final long value);

	public Boolean hexists(final String key, final String field);

	public Long del(final String key);

	public Long hdel(final String key, final String... fields);

	public Long hlen(final String key);

	public Set<String> hkeys(final String key);

	public List<String> hvals(final String key);

	public Map<String, String> hgetAll(final String key);

	public Long rpush(final String key, final String... strings);

	public Long lpush(final String key, final String... strings);

	public Long lpushx(final String key, final String string);

	public Long strlen(final String key);

	public Long move(final String key, final int dbIndex);

	public Long rpushx(final String key, final String string);

	public Long persist(final String key);

	public Long llen(final String key);

	public List<String> lrange(final String key, final long start, final long end);

	public String ltrim(final String key, final long start, final long end);

	public String lindex(final String key, final long index);

	public String lset(final String key, final long index, final String value);

	public Long lrem(final String key, final long count, final String value);

	public String lpop(final String key);

	public String rpop(final String key);

	public Long sadd(final String key, final String... members);

	public Set<String> smembers(final String key);

	public Long srem(final String key, final String... members);

	public String spop(final String key);

	public Long scard(final String key);

	public Boolean sismember(final String key, final String member);

	public String srandmember(final String key);

	public Long zadd(final String key, final double score, final String member);

	public Long zadd(final String key, final Map<String, Double> scoreMembers);

	public Set<String> zrange(final String key, final long start, final long end);

	public Long zrem(final String key, final String... members);

	public Double zincrby(final String key, final double score, final String member);

	public Long zrank(final String key, final String member);

	public Long zrevrank(final String key, final String member);

	public Set<String> zrevrange(final String key, final long start, final long end);

	public Set<Tuple> zrangeWithScores(final String key, final long start, final long end);

	public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end);

	public Long zcard(final String key);

	public Double zscore(final String key, final String member);

	public List<String> sort(final String key);

	public List<String> sort(final String key, final SortingParams sortingParameters);

	public Long zcount(final String key, final double min, final double max);

	public Long zcount(final String key, final String min, final String max);

	public Set<String> zrangeByScore(final String key, final double min, final double max);

	public Set<String> zrevrangeByScore(final String key, final double max, final double min);

	public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count);

	public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count);

	public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max);

	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min);

	public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count);

	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count);

	public Set<String> zrangeByScore(final String key, final String min, final String max);

	public Set<String> zrevrangeByScore(final String key, final String max, final String min);

	public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count);

	public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count);

	public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max);

	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min);

	public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count);

	public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count);

	public Long zremrangeByRank(final String key, final long start, final long end);

	public Long zremrangeByScore(final String key, final double start, final double end);

	public Long zremrangeByScore(final String key, final String start, final String end);

	public Long linsert(final String key, final LIST_POSITION where, final String pivot, final String value);

	public String set(final String key, final Object value);

	public Object getObject(final String key);

	public String info(final String key);
}
