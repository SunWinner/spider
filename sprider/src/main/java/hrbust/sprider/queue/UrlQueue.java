package hrbust.sprider.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UrlQueue implements QueueInterface{
	Queue<String> highQueue = new ConcurrentLinkedQueue<String>();
	
	Queue<String> lowQueue = new ConcurrentLinkedQueue<String>();
	
	public String poll(){
		String url = highQueue.poll();
		if(url == null || "".equals(url)) {
			url = lowQueue.poll();
		}
		return url;
	}
	
	public void addHighQueue(String url) {
		highQueue.add(url);
	}
	
	public void addLowQueue(String url) {
		lowQueue.add(url);
	}
	
}
