package hrbust.sprider.queue;

import hrbust.sprider.util.RedisUtils;

public class RedisQueue implements QueueInterface {
	
	RedisUtils redisUtils = new RedisUtils();
	
	@Override
	public String poll() {
		String url = redisUtils.poll(RedisUtils.HIGH_KEY);
		if(url==null){
			url = redisUtils.poll(RedisUtils.LOW_KEY);
		}
		return url;
	}

	@Override
	public void addHighQueue(String url) {
		redisUtils.addHighQueue(url);
	}

	@Override
	public void addLowQueue(String url) {
		redisUtils.addLowQueue(url);
	}
	
}
