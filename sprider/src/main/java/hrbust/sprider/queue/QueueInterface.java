package hrbust.sprider.queue;

public interface QueueInterface {
	public String poll();
	public void addHighQueue(String url);
	public void addLowQueue(String url);
}
