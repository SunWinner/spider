package hrbust.sprider.config;

public class Config {
	
	//爬虫线程休息
	public static final long MILLION_1 = 1000;
	//等待队列添加url
	public static final long MILLION_5 = 5000;
	//开启的爬虫线程数
	public static final int THREAD_COUNT = 10;
	
	//高级队列的添加条件
	public static final String[] HIGH_QUEUE = {"https://item.jd.com","news.sina.com"};
	
	
}
