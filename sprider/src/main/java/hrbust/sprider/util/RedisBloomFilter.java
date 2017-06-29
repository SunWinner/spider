package hrbust.sprider.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * redis实现的共享BloomFilter
 * @author zhiyong.xiongzy
 *
 */
public class RedisBloomFilter {
 
	JedisCluster jedisCluster = null;
	
    private static final Logger logger       = Logger.getLogger(RedisBloomFilter.class);
 
    private static final int[]  seeds        = new int[] { 3, 7, 11, 13, 31, 37, 55 };

    private static final String key          = "redis:bloom:filter";
 
    private BloomHash[]         func         = new BloomHash[seeds.length];
 
    public RedisBloomFilter() {
        for (int i = 0; i < seeds.length; i++) {
            func[i] = new BloomHash(2 << 26, seeds[i]);
        }
    }
 
    /**
     * 加入一个数据
     * @param value
     */
    public void add(String value) {
        for (BloomHash f : func) {
            setBig(f.hash(value), true);
        }
    }
 
    /**
     * 判重
     * @param value
     * @return
     */
    public boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        for (BloomHash f : func) {
            ret = ret && getBig(f.hash(value));
        }
        return ret;
    }
 
    /**
     * redis连接池初始化并返回一个redis连接
     * @return redis连接实例
     */
    private JedisCluster getJedis() {
        /*if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(10);
            config.setMaxIdle(2);
            config.setMaxWaitMillis(2000);
            pool = new JedisPool(config, redisHost, redisPort, 120, redisPass);
        }
        return pool.getResource();*/
        
        String[] serverArray = "192.168.1.148:7000,192.168.1.148:7001,192.168.1.148:7002,192.168.1.164:7003,192.168.1.164:7004,192.168.1.164:7005"
				.split(",");
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();

		for (String ipPort : serverArray) {
			String[] ipPortPair = ipPort.split(":");
			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer
					.valueOf(ipPortPair[1].trim())));
		}

		// 注意：这里超时时间不要太短，他会有超时重试机制。而且其他像httpclient、dubbo等RPC框架也要注意这点
		jedisCluster = new JedisCluster(nodes, 1000, 1000, 1,
				new GenericObjectPoolConfig());
		
		return jedisCluster;
    }
 
    private boolean setBig(int offset, boolean value) {
        JedisCluster jedis = null;
        try {
            jedis = getJedis();
            return jedis.setbit(key, offset, value);
        } catch (Exception e) {
            logger.error("Redis hset error", e);
            return true;
        } 
    }
    private boolean getBig(int offset) {
        JedisCluster jedis = null;
        try {
            jedis = getJedis();
            return jedis.getbit(key, offset);
        } catch (Exception e) {
            logger.error("Redis hset error", e);
            //redis异常，返回true,保证bloomfilter规则之存在的元素一定是返回存在
            return true;
        } 
    }
 
	
 
    /**
     * 一个简单的hash算法类，输出int类型hash值
     * @author zhiyong.xiongzy
     *
     */
    public static class BloomHash {
 
        private int cap;
        private int seed;
 
        public BloomHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }
 
        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }
 
    public static void main(String[] args) {
        String value = "95cea659143842e3f787f96910cac2bb2f32d207";
        RedisBloomFilter filter = new RedisBloomFilter();
        System.out.println(filter.contains(value));
        filter.add(value);
        System.out.println(filter.contains(value));
 
    }
 
}