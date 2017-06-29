package hrbust.sprider.util;

import hrbust.sprider.config.Config;

public class UrlFilter {
	//判断是否添加到高级队列
	public static boolean isHighQueue(String url) {

		for (int i = 0; i < Config.HIGH_QUEUE.length; i++) {
			if (url.contains(Config.HIGH_QUEUE[i])) {
				return true;
			}
		}
		return false;
	}
}
