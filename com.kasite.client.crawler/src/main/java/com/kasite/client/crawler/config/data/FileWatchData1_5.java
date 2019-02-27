package com.kasite.client.crawler.config.data;

import java.io.File;

class FileWatchData1_5 extends Thread{
	
	Data1_5Bus bus;
	
	static FileWatchData1_5 inst;
	
	private FileWatchData1_5(Data1_5Bus bus){
		this.bus = bus;
	}
	
	public static synchronized FileWatchData1_5 getInstall(Data1_5Bus bus) {
		if(null == inst) {
			inst = new FileWatchData1_5(bus);
		}
		return inst;
	}
	
	private boolean started;
	private long lastModif=-1L;
	private long delay = 10000;
	private void deal() throws Exception
	{
		File file=new File(bus.getFilePath());
		boolean fileExists = file.exists();
		if (fileExists) 
		{
			if(this.lastModif<0){
				this.lastModif=file.lastModified();
				return;
			}
			if(file.lastModified()>lastModif)
			{
				lastModif=file.lastModified();
			}
			bus.init();
		}
	}
	@Override
	public void run() {
		if(started){
			return;
		}
		started=true;
		while(true)
		{
			try 
			{
				Thread.sleep(this.delay);
				deal();
			} catch (Exception e) {
//				System.out.println(e.getMessage());
			}
			
		}
	}

}