package hrbust.sprider.test;

import hrbust.sprider.queue.QueueInterface;
import hrbust.sprider.queue.RedisQueue;
import hrbust.sprider.util.RedisUtils;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class RedisTest {

	JedisCluster jedisCluster;
	@Before
	public void before() {
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

		// 大多数测试都是使用【nameKey】测试的，所以在启动之前先把这个key删掉
	}

	@Test
	public void test01() {
		RedisUtils redisUtils = new RedisUtils();
		redisUtils.addHighQueue("asdfasdfadsf");
		redisUtils.addHighQueue("ahjfgghjfgh");
		redisUtils.addHighQueue("errttyertty");
		QueueInterface queueInterface = new RedisQueue();
		String url = queueInterface.poll();
		System.out.println(url);
		String url1 = queueInterface.poll();
		System.out.println(url);
		String url2 = queueInterface.poll();
		System.out.println(url);
	}
}
