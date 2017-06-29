package hrbust.sprider;

import hrbust.sprider.config.Config;
import hrbust.sprider.domain.Page;
import hrbust.sprider.download.DownLoad;
import hrbust.sprider.download.impl.DownLoadImpl;
import hrbust.sprider.processer.Processable;
import hrbust.sprider.queue.QueueInterface;
import hrbust.sprider.queue.UrlQueue;
import hrbust.sprider.util.JsoupUtil;
import hrbust.sprider.util.RedisBloomFilter;
import hrbust.sprider.util.SleepUtils;
import hrbust.sprider.util.UrlFilter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;

public class Spider {
	
	RedisBloomFilter filter = new RedisBloomFilter();
	DownLoad download = new DownLoadImpl();
	ExecutorService threadPool = Executors.newFixedThreadPool(10);
	QueueInterface queueInterface = new UrlQueue();
	JsoupUtil jsoupUtil = new JsoupUtil();
	Processable process;

	public Spider() {

	}
	public void start(String startUrl) {
		if(queueInterface.poll() == null){
			System.out.println(queueInterface.poll());
			Page page = Spider.this.download(startUrl);
			//解析html中的所有url信息
			List<String> urls = jsoupUtil.getUrls(page);
			page.setUrlList(urls);
			//对页面所需要的内容
			Spider.this.process(page);
			for (String url : urls) {
				//对url进行第一次去重
				if(!filter.contains(url)) {
					if (UrlFilter.isHighQueue(url)) {
						queueInterface.addHighQueue(url);
					} else {
						queueInterface.addLowQueue(url);
					}
				}
			}
			// 列表页面才进行存储
			if (urls == null || urls.size() == 0) {
				Spider.this.save(page);
			}
		}
		while (true) {
			final String url = queueInterface.poll();
			System.out.println(url);
			if (StringUtils.isNotBlank(url)) {
				threadPool.execute(new Runnable() {
					public void run() {
						System.out.println(url);
						Page page = Spider.this.download(url);
						//解析html中的所有url信息
						List<String> urls = jsoupUtil.getUrls(page);
						page.setUrlList(urls);
						//对页面所需要的内容
						Spider.this.process(page);
						for (String url : urls) {
							//对url进行第一次去重
							if(!filter.contains(url)) {
								if (UrlFilter.isHighQueue(url)) {
									queueInterface.addHighQueue(url);
								} else {
									queueInterface.addLowQueue(url);
								}
							}
						}
						// 列表页面才进行存储
						if (urls == null || urls.size() == 0) {
							Spider.this.save(page);
						}
						SleepUtils.sleep(Config.MILLION_1);
					}
				});
			} else {
				SleepUtils.sleep(Config.MILLION_5);
			}
		}
	}

	protected void save(Page page) {
		// TODO Auto-generated method stub

	}

	public Page download(String url) {
		return download.downLoadPage(url);
	}

	protected void process(Page page) {
		//process.process(page);
	}

	//设置配置项
	public RedisBloomFilter getFilter() {
		return filter;
	}

	public void setFilter(RedisBloomFilter filter) {
		this.filter = filter;
	}

	public DownLoad getDownload() {
		return download;
	}

	public void setDownload(DownLoad download) {
		this.download = download;
	}

	public ExecutorService getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}

	public QueueInterface getQueueInterface() {
		return queueInterface;
	}

	public void setQueueInterface(QueueInterface queueInterface) {
		this.queueInterface = queueInterface;
	}

	public JsoupUtil getJsoupUtil() {
		return jsoupUtil;
	}

	public void setJsoupUtil(JsoupUtil jsoupUtil) {
		this.jsoupUtil = jsoupUtil;
	}

	public Processable getProcess() {
		return process;
	}

	public void setProcess(Processable process) {
		this.process = process;
	}
}
