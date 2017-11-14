package com.codingapi.tx.datasource.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * create by lorne on 2017/8/22
 */
public class LCNRollBackRedisConnection implements RedisConnection {

    private RedisConnection redisConnection;


    public LCNRollBackRedisConnection(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
    }

    @Override
    public void close() throws DataAccessException {
        redisConnection.close();
    }

    @Override
    public void multi() {
        redisConnection.multi();
    }

    @Override
    public List<Object> exec() {
        return null;
    }


    /**
     * default
     **/

    @Override
    public void unwatch() {
        redisConnection.unwatch();
    }

    @Override
    public boolean isClosed() {
        return redisConnection.isClosed();
    }

    @Override
    public Object getNativeConnection() {
        return redisConnection.getNativeConnection();
    }

    @Override
    public boolean isQueueing() {
        return redisConnection.isQueueing();
    }

    @Override
    public boolean isPipelined() {
        return redisConnection.isPipelined();
    }

    @Override
    public void openPipeline() {
        redisConnection.openPipeline();
    }

    @Override
    public List<Object> closePipeline() throws RedisPipelineException {
        return redisConnection.closePipeline();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return redisConnection.getSentinelConnection();
    }

    @Override
    public Object execute(String command, byte[]... args) {
        return redisConnection.execute(command, args);
    }

    @Override
    public Boolean exists(byte[] key) {
        return redisConnection.exists(key);
    }

    @Override
    public Long del(byte[]... keys) {
        return redisConnection.del(keys);
    }

    @Override
    public DataType type(byte[] key) {
        return redisConnection.type(key);
    }

    @Override
    public Set<byte[]> keys(byte[] pattern) {
        return redisConnection.keys(pattern);
    }

    @Override
    public Cursor<byte[]> scan(ScanOptions options) {
        return redisConnection.scan(options);
    }

    @Override
    public byte[] randomKey() {
        return redisConnection.randomKey();
    }

    @Override
    public void rename(byte[] oldName, byte[] newName) {
        redisConnection.rename(oldName, newName);
    }

    @Override
    public Boolean renameNX(byte[] oldName, byte[] newName) {
        return redisConnection.renameNX(oldName, newName);
    }

    @Override
    public Boolean expire(byte[] key, long seconds) {
        return redisConnection.expire(key, seconds);
    }

    @Override
    public Boolean pExpire(byte[] key, long millis) {
        return redisConnection.pExpire(key, millis);
    }

    @Override
    public Boolean expireAt(byte[] key, long unixTime) {
        return redisConnection.expireAt(key, unixTime);
    }

    @Override
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        return redisConnection.pExpireAt(key, unixTimeInMillis);
    }

    @Override
    public Boolean persist(byte[] key) {
        return redisConnection.persist(key);
    }

    @Override
    public Boolean move(byte[] key, int dbIndex) {
        return redisConnection.move(key, dbIndex);
    }

    @Override
    public Long ttl(byte[] key) {
        return redisConnection.ttl(key);
    }

    @Override
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        return redisConnection.ttl(key, timeUnit);
    }

    @Override
    public Long pTtl(byte[] key) {
        return redisConnection.pTtl(key);
    }

    @Override
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        return redisConnection.pTtl(key, timeUnit);
    }

    @Override
    public List<byte[]> sort(byte[] key, SortParameters params) {
        return redisConnection.sort(key, params);
    }

    @Override
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        return redisConnection.sort(key, params, storeKey);
    }

    @Override
    public byte[] dump(byte[] key) {
        return redisConnection.dump(key);
    }

    @Override
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
        redisConnection.restore(key, ttlInMillis, serializedValue);
    }

    @Override
    public byte[] get(byte[] key) {
        return redisConnection.get(key);
    }

    @Override
    public byte[] getSet(byte[] key, byte[] value) {
        return redisConnection.getSet(key, value);
    }

    @Override
    public List<byte[]> mGet(byte[]... keys) {
        return redisConnection.mGet(keys);
    }

    @Override
    public void set(byte[] key, byte[] value) {
        redisConnection.set(key, value);
    }

    @Override
    public void set(byte[] key, byte[] value, Expiration expiration, SetOption option) {
        redisConnection.set(key, value, expiration, option);
    }

    @Override
    public Boolean setNX(byte[] key, byte[] value) {
        return redisConnection.setNX(key, value);
    }

    @Override
    public void setEx(byte[] key, long seconds, byte[] value) {
        redisConnection.setEx(key, seconds, value);
    }

    @Override
    public void pSetEx(byte[] key, long milliseconds, byte[] value) {
        redisConnection.pSetEx(key, milliseconds, value);
    }

    @Override
    public void mSet(Map<byte[], byte[]> tuple) {
        redisConnection.mSet(tuple);
    }

    @Override
    public Boolean mSetNX(Map<byte[], byte[]> tuple) {
        return redisConnection.mSetNX(tuple);
    }

    @Override
    public Long incr(byte[] key) {
        return redisConnection.incr(key);
    }

    @Override
    public Long incrBy(byte[] key, long value) {
        return redisConnection.incrBy(key, value);
    }

    @Override
    public Double incrBy(byte[] key, double value) {
        return redisConnection.incrBy(key, value);
    }

    @Override
    public Long decr(byte[] key) {
        return redisConnection.decr(key);
    }

    @Override
    public Long decrBy(byte[] key, long value) {
        return redisConnection.decrBy(key, value);
    }

    @Override
    public Long append(byte[] key, byte[] value) {
        return redisConnection.append(key, value);
    }

    @Override
    public byte[] getRange(byte[] key, long begin, long end) {
        return redisConnection.getRange(key, begin, end);
    }

    @Override
    public void setRange(byte[] key, byte[] value, long offset) {
        redisConnection.setRange(key, value, offset);
    }

    @Override
    public Boolean getBit(byte[] key, long offset) {
        return redisConnection.getBit(key, offset);
    }

    @Override
    public Boolean setBit(byte[] key, long offset, boolean value) {
        return redisConnection.setBit(key, offset, value);
    }

    @Override
    public Long bitCount(byte[] key) {
        return redisConnection.bitCount(key);
    }

    @Override
    public Long bitCount(byte[] key, long begin, long end) {
        return redisConnection.bitCount(key, begin, end);
    }

    @Override
    public Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {
        return redisConnection.bitOp(op, destination, keys);
    }

    @Override
    public Long strLen(byte[] key) {
        return redisConnection.strLen(key);
    }

    @Override
    public Long rPush(byte[] key, byte[]... values) {
        return redisConnection.rPush(key, values);
    }

    @Override
    public Long lPush(byte[] key, byte[]... values) {
        return redisConnection.lPush(key, values);
    }

    @Override
    public Long rPushX(byte[] key, byte[] value) {
        return redisConnection.rPushX(key, value);
    }

    @Override
    public Long lPushX(byte[] key, byte[] value) {
        return redisConnection.lPushX(key, value);
    }

    @Override
    public Long lLen(byte[] key) {
        return redisConnection.lLen(key);
    }

    @Override
    public List<byte[]> lRange(byte[] key, long start, long end) {
        return redisConnection.lRange(key, start, end);
    }

    @Override
    public void lTrim(byte[] key, long start, long end) {
        redisConnection.lTrim(key, start, end);
    }

    @Override
    public byte[] lIndex(byte[] key, long index) {
        return redisConnection.lIndex(key, index);
    }

    @Override
    public Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value) {
        return redisConnection.lInsert(key, where, pivot, value);
    }

    @Override
    public void lSet(byte[] key, long index, byte[] value) {
        redisConnection.lSet(key, index, value);
    }

    @Override
    public Long lRem(byte[] key, long count, byte[] value) {
        return redisConnection.lRem(key, count, value);
    }

    @Override
    public byte[] lPop(byte[] key) {
        return redisConnection.lPop(key);
    }

    @Override
    public byte[] rPop(byte[] key) {
        return redisConnection.rPop(key);
    }

    @Override
    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        return redisConnection.bLPop(timeout, keys);
    }

    @Override
    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        return redisConnection.bRPop(timeout, keys);
    }

    @Override
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        return redisConnection.rPopLPush(srcKey, dstKey);
    }

    @Override
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        return redisConnection.bRPopLPush(timeout, srcKey, dstKey);
    }

    @Override
    public Long sAdd(byte[] key, byte[]... values) {
        return redisConnection.sAdd(key, values);
    }

    @Override
    public Long sRem(byte[] key, byte[]... values) {
        return redisConnection.sRem(key, values);
    }

    @Override
    public byte[] sPop(byte[] key) {
        return redisConnection.sPop(key);
    }

    @Override
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        return redisConnection.sMove(srcKey, destKey, value);
    }

    @Override
    public Long sCard(byte[] key) {
        return redisConnection.sCard(key);
    }

    @Override
    public Boolean sIsMember(byte[] key, byte[] value) {
        return redisConnection.sIsMember(key, value);
    }

    @Override
    public Set<byte[]> sInter(byte[]... keys) {
        return redisConnection.sInter(keys);
    }

    @Override
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        return redisConnection.sInterStore(destKey, keys);
    }

    @Override
    public Set<byte[]> sUnion(byte[]... keys) {
        return redisConnection.sUnion(keys);
    }

    @Override
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        return redisConnection.sUnionStore(destKey, keys);
    }

    @Override
    public Set<byte[]> sDiff(byte[]... keys) {
        return redisConnection.sDiff(keys);
    }

    @Override
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        return redisConnection.sDiffStore(destKey, keys);
    }

    @Override
    public Set<byte[]> sMembers(byte[] key) {
        return redisConnection.sMembers(key);
    }

    @Override
    public byte[] sRandMember(byte[] key) {
        return redisConnection.sRandMember(key);
    }

    @Override
    public List<byte[]> sRandMember(byte[] key, long count) {
        return redisConnection.sRandMember(key, count);
    }

    @Override
    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        return redisConnection.sScan(key, options);
    }

    @Override
    public Boolean zAdd(byte[] key, double score, byte[] value) {
        return redisConnection.zAdd(key, score, value);
    }

    @Override
    public Long zAdd(byte[] key, Set<Tuple> tuples) {
        return redisConnection.zAdd(key, tuples);
    }

    @Override
    public Long zRem(byte[] key, byte[]... values) {
        return redisConnection.zRem(key, values);
    }

    @Override
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        return redisConnection.zIncrBy(key, increment, value);
    }

    @Override
    public Long zRank(byte[] key, byte[] value) {
        return redisConnection.zRank(key, value);
    }

    @Override
    public Long zRevRank(byte[] key, byte[] value) {
        return redisConnection.zRevRank(key, value);
    }

    @Override
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        return redisConnection.zRange(key, start, end);
    }

    @Override
    public Set<Tuple> zRangeWithScores(byte[] key, long start, long end) {
        return redisConnection.zRangeWithScores(key, start, end);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        return redisConnection.zRangeByScore(key, min, max);
    }

    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, Range range) {
        return redisConnection.zRangeByScoreWithScores(key, range);
    }

    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        return redisConnection.zRangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return redisConnection.zRangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return redisConnection.zRangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<Tuple> zRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
        return redisConnection.zRangeByScoreWithScores(key, range, limit);
    }

    @Override
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        return redisConnection.zRevRange(key, start, end);
    }

    @Override
    public Set<Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        return redisConnection.zRevRangeWithScores(key, start, end);
    }

    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        return redisConnection.zRevRangeByScore(key, min, max);
    }

    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, Range range) {
        return redisConnection.zRevRangeByScore(key, range);
    }

    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        return redisConnection.zRevRangeByScoreWithScores(key, min, max);
    }

    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        return redisConnection.zRevRangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<byte[]> zRevRangeByScore(byte[] key, Range range, Limit limit) {
        return redisConnection.zRevRangeByScore(key, range, limit);
    }

    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        return redisConnection.zRevRangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, Range range) {
        return redisConnection.zRevRangeByScoreWithScores(key, range);
    }

    @Override
    public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
        return redisConnection.zRevRangeByScoreWithScores(key, range, limit);
    }

    @Override
    public Long zCount(byte[] key, double min, double max) {
        return redisConnection.zCount(key, min, max);
    }

    @Override
    public Long zCount(byte[] key, Range range) {
        return redisConnection.zCount(key, range);
    }

    @Override
    public Long zCard(byte[] key) {
        return redisConnection.zCard(key);
    }

    @Override
    public Double zScore(byte[] key, byte[] value) {
        return redisConnection.zScore(key, value);
    }

    @Override
    public Long zRemRange(byte[] key, long start, long end) {
        return redisConnection.zRemRange(key, start, end);
    }

    @Override
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        return redisConnection.zRemRangeByScore(key, min, max);
    }

    @Override
    public Long zRemRangeByScore(byte[] key, Range range) {
        return redisConnection.zRemRangeByScore(key, range);
    }

    @Override
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        return redisConnection.zUnionStore(destKey, sets);
    }

    @Override
    public Long zUnionStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
        return redisConnection.zUnionStore(destKey, aggregate, weights, sets);
    }

    @Override
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        return redisConnection.zInterStore(destKey, sets);
    }

    @Override
    public Long zInterStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
        return redisConnection.zInterStore(destKey, aggregate, weights, sets);
    }

    @Override
    public Cursor<Tuple> zScan(byte[] key, ScanOptions options) {
        return redisConnection.zScan(key, options);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        return redisConnection.zRangeByScore(key, min, max);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, Range range) {
        return redisConnection.zRangeByScore(key, range);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        return redisConnection.zRangeByScore(key, min, max, offset, count);
    }

    @Override
    public Set<byte[]> zRangeByScore(byte[] key, Range range, Limit limit) {
        return redisConnection.zRangeByScore(key, range, limit);
    }

    @Override
    public Set<byte[]> zRangeByLex(byte[] key) {
        return redisConnection.zRangeByLex(key);
    }

    @Override
    public Set<byte[]> zRangeByLex(byte[] key, Range range) {
        return redisConnection.zRangeByLex(key, range);
    }

    @Override
    public Set<byte[]> zRangeByLex(byte[] key, Range range, Limit limit) {
        return redisConnection.zRangeByLex(key, range, limit);
    }

    @Override
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        return redisConnection.hSet(key, field, value);
    }

    @Override
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        return redisConnection.hSetNX(key, field, value);
    }

    @Override
    public byte[] hGet(byte[] key, byte[] field) {
        return redisConnection.hGet(key, field);
    }

    @Override
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        return redisConnection.hMGet(key, fields);
    }

    @Override
    public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
        redisConnection.hMSet(key, hashes);
    }

    @Override
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        return redisConnection.hIncrBy(key, field, delta);
    }

    @Override
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        return redisConnection.hIncrBy(key, field, delta);
    }

    @Override
    public Boolean hExists(byte[] key, byte[] field) {
        return redisConnection.hExists(key, field);
    }

    @Override
    public Long hDel(byte[] key, byte[]... fields) {
        return redisConnection.hDel(key, fields);
    }

    @Override
    public Long hLen(byte[] key) {
        return redisConnection.hLen(key);
    }

    @Override
    public Set<byte[]> hKeys(byte[] key) {
        return redisConnection.hKeys(key);
    }

    @Override
    public List<byte[]> hVals(byte[] key) {
        return redisConnection.hVals(key);
    }

    @Override
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        return redisConnection.hGetAll(key);
    }

    @Override
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        return redisConnection.hScan(key, options);
    }

    @Override
    public void discard() {
        redisConnection.discard();
    }

    @Override
    public void watch(byte[]... keys) {
        redisConnection.watch(keys);
    }


    @Override
    public boolean isSubscribed() {
        return redisConnection.isSubscribed();
    }

    @Override
    public Subscription getSubscription() {
        return redisConnection.getSubscription();
    }

    @Override
    public Long publish(byte[] channel, byte[] message) {
        return redisConnection.publish(channel, message);
    }

    @Override
    public void subscribe(MessageListener listener, byte[]... channels) {
        redisConnection.subscribe(listener, channels);
    }

    @Override
    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        redisConnection.pSubscribe(listener, patterns);
    }

    @Override
    public void select(int dbIndex) {
        redisConnection.select(dbIndex);
    }

    @Override
    public byte[] echo(byte[] message) {
        return redisConnection.echo(message);
    }

    @Override
    public String ping() {
        return redisConnection.ping();
    }

    @Override
    @Deprecated
    public void bgWriteAof() {
        redisConnection.bgWriteAof();
    }

    @Override
    public void bgReWriteAof() {
        redisConnection.bgReWriteAof();
    }

    @Override
    public void bgSave() {
        redisConnection.bgSave();
    }

    @Override
    public Long lastSave() {
        return redisConnection.lastSave();
    }

    @Override
    public void save() {
        redisConnection.save();
    }

    @Override
    public Long dbSize() {
        return redisConnection.dbSize();
    }

    @Override
    public void flushDb() {
        redisConnection.flushDb();
    }

    @Override
    public void flushAll() {
        redisConnection.flushAll();
    }

    @Override
    public Properties info() {
        return redisConnection.info();
    }

    @Override
    public Properties info(String section) {
        return redisConnection.info(section);
    }

    @Override
    public void shutdown() {
        redisConnection.shutdown();
    }

    @Override
    public void shutdown(ShutdownOption option) {
        redisConnection.shutdown(option);
    }

    @Override
    public List<String> getConfig(String pattern) {
        return redisConnection.getConfig(pattern);
    }

    @Override
    public void setConfig(String param, String value) {
        redisConnection.setConfig(param, value);
    }

    @Override
    public void resetConfigStats() {
        redisConnection.resetConfigStats();
    }

    @Override
    public Long time() {
        return redisConnection.time();
    }

    @Override
    public void killClient(String host, int port) {
        redisConnection.killClient(host, port);
    }

    @Override
    public void setClientName(byte[] name) {
        redisConnection.setClientName(name);
    }

    @Override
    public String getClientName() {
        return redisConnection.getClientName();
    }

    @Override
    public List<RedisClientInfo> getClientList() {
        return redisConnection.getClientList();
    }

    @Override
    public void slaveOf(String host, int port) {
        redisConnection.slaveOf(host, port);
    }

    @Override
    public void slaveOfNoOne() {
        redisConnection.slaveOfNoOne();
    }

    @Override
    public void migrate(byte[] key, RedisNode target, int dbIndex, MigrateOption option) {
        redisConnection.migrate(key, target, dbIndex, option);
    }

    @Override
    public void migrate(byte[] key, RedisNode target, int dbIndex, MigrateOption option, long timeout) {
        redisConnection.migrate(key, target, dbIndex, option, timeout);
    }

    @Override
    public void scriptFlush() {
        redisConnection.scriptFlush();
    }

    @Override
    public void scriptKill() {
        redisConnection.scriptKill();
    }

    @Override
    public String scriptLoad(byte[] script) {
        return redisConnection.scriptLoad(script);
    }

    @Override
    public List<Boolean> scriptExists(String... scriptShas) {
        return redisConnection.scriptExists(scriptShas);
    }

    @Override
    public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        return redisConnection.eval(script, returnType, numKeys, keysAndArgs);
    }

    @Override
    public <T> T evalSha(String scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        return redisConnection.evalSha(scriptSha, returnType, numKeys, keysAndArgs);
    }

    @Override
    public <T> T evalSha(byte[] scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        return redisConnection.evalSha(scriptSha, returnType, numKeys, keysAndArgs);
    }

    @Override
    public Long geoAdd(byte[] key, Point point, byte[] member) {
        return redisConnection.geoAdd(key, point, member);
    }

    @Override
    public Long geoAdd(byte[] key, GeoLocation<byte[]> location) {
        return redisConnection.geoAdd(key, location);
    }

    @Override
    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
        return redisConnection.geoAdd(key, memberCoordinateMap);
    }

    @Override
    public Long geoAdd(byte[] key, Iterable<GeoLocation<byte[]>> locations) {
        return redisConnection.geoAdd(key, locations);
    }

    @Override
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
        return redisConnection.geoDist(key, member1, member2);
    }

    @Override
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
        return redisConnection.geoDist(key, member1, member2, metric);
    }

    @Override
    public List<String> geoHash(byte[] key, byte[]... members) {
        return redisConnection.geoHash(key, members);
    }

    @Override
    public List<Point> geoPos(byte[] key, byte[]... members) {
        return redisConnection.geoPos(key, members);
    }

    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
        return redisConnection.geoRadius(key, within);
    }

    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, GeoRadiusCommandArgs args) {
        return redisConnection.geoRadius(key, within, args);
    }

    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
        return redisConnection.geoRadiusByMember(key, member, radius);
    }

    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
        return redisConnection.geoRadiusByMember(key, member, radius);
    }

    @Override
    public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius, GeoRadiusCommandArgs args) {
        return redisConnection.geoRadiusByMember(key, member, radius, args);
    }

    @Override
    public Long geoRemove(byte[] key, byte[]... members) {
        return redisConnection.geoRemove(key, members);
    }

    @Override
    public Long pfAdd(byte[] key, byte[]... values) {
        return redisConnection.pfAdd(key, values);
    }

    @Override
    public Long pfCount(byte[]... keys) {
        return redisConnection.pfCount(keys);
    }

    @Override
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        redisConnection.pfMerge(destinationKey, sourceKeys);
    }

}
