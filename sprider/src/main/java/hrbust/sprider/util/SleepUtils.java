package hrbust.sprider.util;

public class SleepUtils {
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			System.out.println("线程休息失败");
		}
	}
}	
