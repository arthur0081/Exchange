package com.slabs.exchange.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;

@Component
@Slf4j
public class RedisUtil {
    private static final String activeName = "activeUser:";

    @Resource
    private JedisPool jedisPool;

    private Jedis getJedis()  {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return  jedis;
        } catch (JedisConnectionException e) {
            log.error("获取Redis 异常", e);
            throw e;
        }
    }


    /**
     * 保存对象到Redis 对象不过期
     *
     * @param key    待缓存的key
     * @param object 待缓存的对象
     * @return 返回是否缓存成功
     */
    public boolean setObject(String key, Object object) throws Exception {
        return setObject(key, object, -1);
    }

    /**
     * 保存对象到Redis 并设置超时时间
     *
     * @param key     缓存key
     * @param object  缓存对象
     * @param timeout 超时时间
     * @return 返回是否缓存成功
     * @throws Exception 异常上抛
     */
    public boolean setObject(String key, Object object, int timeout) throws Exception {
        String value = SerializeUtil.serialize(object);
        boolean result = false;
        try {
            //为-1时不设置超时时间
            if (timeout != -1) {
                setString(key,value,timeout);
            } else {
                setString(key,value);
            }
            result = false;
        } catch (Exception e) {
            throw e;
        }
        return  result;
    }

    /**
     * 从Redis中获取对象
     *
     * @param key 待获取数据的key
     * @return 返回key对应的对象
     */
    public  Object getObject(String key) throws Exception {
        Object object = null;
        try {
            String serializeObj = getString(key);
            if (null == serializeObj || serializeObj.length() == 0) {
                object = null;
            } else {
                object = SerializeUtil.deserialize(serializeObj);
            }
        }  catch (Exception e) {
            throw e;
        }
        return object;
    }

    /**
     * 缓存String类型的数据,数据不过期
     *
     * @param key   待缓存数据的key
     * @param value 需要缓存的额数据
     * @return 返回是否缓存成功
     */
    public boolean setString(String key, String value) throws Exception {
        return setString(key, value, -1);
    }

    /**
     * 缓存String类型的数据并设置超时时间
     *
     * @param key     key
     * @param value   value
     * @param timeout 超时时间
     * @return 返回是否缓存成功
     */
    public boolean setString(String key, String value, int timeout) throws Exception {
        String result;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            result = jedis.set(key, value);
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
            if ("OK".equals(result)) {
                return true;
            } else {
                return  false;
            }
        } catch (Exception e){
            throw  e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 获取String类型的数据
     *
     * @param key 需要获取数据的key
     * @return 返回key对应的数据
     */
    @SuppressWarnings("deprecation")
    public  String getString(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.get(key);
        } catch (Exception e) {
            throw e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * Jedis 对象释放
     * @param jedis
     */
    public void releaseRedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


    /**
     * 删除缓存中的数据
     *
     * @param key 需要删除数据的key
     * @return 返回是否删除成功
     */
    public boolean del(String key) throws Exception {
        Long num;
        Jedis jedis = null;
        boolean result = false;
        try {
            jedis = getJedis();
            num = jedis.del(key);
            if (num.equals(1L)) {
                result = true;
            }
        } catch (Exception e) {
            throw  e;
        } finally {
            releaseRedis(jedis);
        }
        return result;
    }


    /**
     * 对某个键的值自增
     * @param key 键
     * @param timeout 超时时间
     * @return
     */
    public Long setIncr(String key,int timeout) throws Exception {
        Long num;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            num = jedis.incr(key);
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
        } catch (Exception e){
            throw  e;
        } finally {
            releaseRedis(jedis);
        }
        return num;
    }


    /**
     * 当用户登陆的时候，传入id的值是 1 （成为当天的活跃用户）
     *
     * @param id 传入1代表登陆
     * @param timeout 超时时间
     */
    public void setActiveUserCount(Integer userId, int timeout) throws Exception {
        Jedis jedis = null;
        try {
            String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String key = activeName + currentStr;
            jedis = getJedis();
            jedis.setbit(key, userId, true);
            if (timeout != -1) {
                jedis.expire(key, timeout);
            }
        } catch (Exception e){
            throw  e;
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     *  统计当天的活跃的用户
     */
    public int getActiveUserCount() throws Exception {
        int count;
        Jedis jedis = null;
        BitSet all = new BitSet();
        try {
            String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String key = activeName + currentStr;
            // 获取这一天日期的活跃用户字节数组
            jedis = getJedis();
            byte[] loginByte = jedis.get(key.getBytes());
            //有可能当天没有一个用户登陆
            if (loginByte != null) {
                BitSet user = BitSet.valueOf(loginByte);
                // 一天内只有有一次登陆即可  （如果是几天内同时在线的时候，需要使用 循环取交集）
                all.or(user);
                count = all.cardinality();
                return count;
            }
        } catch (Exception e){
            throw  e;
        } finally {
            releaseRedis(jedis);
        }
        // 如果当天没有用户登陆，则返回 0
        return 0;
    }

}
