package hrbust.sprider.test;

import hrbust.sprider.Spider;
import hrbust.sprider.domain.Page;
import hrbust.sprider.processer.JDProcess;

public class SpiderTest {
	public static void main(String[] args) {
		Spider spider = new Spider();
		String url = "http://www.sina.com.cn/";
		//下载页面
		spider.start(url);
		//获取页面所有内容
		spider.setProcess(new JDProcess());
		//spider.process(page);
		
//		System.out.println(page.getValues().get("spec"));
		//spider.setStoreable(new ConsoleStoreable());
		//spider.store(page);
	}
}
