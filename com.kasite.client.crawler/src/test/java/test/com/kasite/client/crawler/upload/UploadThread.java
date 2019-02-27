package test.com.kasite.client.crawler.upload;

import com.kasite.client.crawler.config.Convent;
import com.kasite.client.crawler.modules.upload.job.UploadJob;
import com.kasite.client.crawler.modules.upload.job.service.ThreadUploadThread;
import com.kasite.client.crawler.modules.upload.job.service.UploadFileGuardThread;

public class UploadThread {

	public static void main(String[] args) {
		int size = Convent.getFileUploadThreadSize();
		for (int i = 0; i < size; i++) {
			Thread t = new Thread(new ThreadUploadThread("ThreadUploadThread_"+i));
			t.setDaemon(false);
			t.start();
		}
		new Thread(()->{
			while(true) {
				try {
					Thread.sleep(1000);
					new UploadJob().work();
				} catch ( Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		Thread guardThread = new Thread(new UploadFileGuardThread());
		guardThread.setDaemon(false);
		guardThread.setName("UploadThreadGuardThread");
		guardThread.start();
		
	}
}
