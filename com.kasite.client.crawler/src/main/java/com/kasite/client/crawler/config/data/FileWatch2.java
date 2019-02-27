package com.kasite.client.crawler.config.data;

import java.io.File;

class FileWatch1_5 extends Thread{
	
	DictBus1_5 bus;
	
	static FileWatch1_5 inst;
	
	private FileWatch1_5(DictBus1_5 bus){
		this.bus = bus;
	}
	
	public static synchronized FileWatch1_5 getInstall(DictBus1_5 bus) {
		if(null == inst) {
			inst = new FileWatch1_5(bus);
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
		file = null;
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