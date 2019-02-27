package com.kasite.client.crawler.config;

import java.io.File;

class FileWatch extends Thread{
	
	DictBus bus;
	
	static FileWatch inst;
	
	private FileWatch(DictBus bus){
		this.bus = bus;
	}
	
	public static synchronized FileWatch getInstall(DictBus bus) {
		if(null == inst) {
			inst = new FileWatch(bus);
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