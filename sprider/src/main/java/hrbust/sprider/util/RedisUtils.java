package hrbust.sprider.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class RedisUtils {

	JedisCluster jedisCluster = null;

	public static final String HIGH_KEY = "spider.high.url";
	public static final String LOW_KEY = "spider.low.url";

	public RedisUtils() {
		String[] serverArray = "192.168.1.148:7000,192.168.1.148:7001,192.168.1.148:7002,192.168.1.164:7003,192.168.1.164:7004,192.168.1.164:7005"
				.split(",");
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();

		for (String ipPort : serverArray) {
			String[] ipPortPair = ipPort.split(":");
			nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer
					.valueOf(ipPortPair[1].trim())));
		}

		// 注意：这里超时时间不要太短，他会有超时重试机制。而且其他像httpclient、dubbo等RPC框架也要注意这点
		jedisCluster = new JedisCluster(nodes, 3000, 3000, 1,
				new GenericObjectPoolConfig());
	}

	public Long add(String url) {
		return jedisCluster.setnx(url, "content");
	}

	public String poll(String key) {
		String result = jedisCluster.rpop(key);
		return result;
	}

	public void addHighQueue(String url) {
		jedisCluster.lpush(RedisUtils.HIGH_KEY, url);
	}

	public void addLowQueue(String url) {
		jedisCluster.lpush(RedisUtils.LOW_KEY, url);
	}

}
