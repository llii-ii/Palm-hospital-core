package com.yihu.hos.util;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.kasite.core.common.config.KasiteConfig;

/**
 * @author Administrator
 * 
 */
public class FileWatch implements Runnable {
	/**
	 * 配置文件
	 */
	private File file;
	/**
	 * 最后修改时间
	 */
	private Map<String, Long> lastModifMap = new HashMap<String, Long>();
	/**
	 * 
	 */
	private long lastModif = 0;
	/**
	 * 检索时间
	 */
	private long delay = 30000;
	/**
	 * 
	 */
	private FileChange change;

	public FileWatch(String name, FileChange change) {
		try {
			String filename = java.net.URLDecoder.decode(
					FileWatch.class.getClassLoader().getResource(name).getPath(),
					"utf-8");
			KasiteConfig.print(filename);
			file = new File(filename);
			boolean fileExists = file.exists();
			if (fileExists) {
				// KasiteConfig.print("文件存在");
				lastModif = file.lastModified();
			}
			File[] files = file.getParentFile().listFiles(new FileTypeFilter());
			for (int i = 0; i < files.length; i++) {
				File _tempFile = files[i];
				lastModifMap.put(_tempFile.getName(), _tempFile.lastModified());
			}
			this.change = change;
		} catch (Exception e) {
			KasiteConfig.print(e.getMessage());
		}
	}

	public FileWatch(File file, FileChange change) {
		this.file = file;
		this.change = change;
	}

	@Override
	public void run() {
		while (true) {
			try {
				boolean fileExists = file.exists();
				if (fileExists) {
					long l = file.lastModified();
					if (l > lastModif) {
						lastModif = l;
						KasiteConfig.print("文件修改了");
						this.change.onChange(file);
					}
					File[] files = file.getParentFile().listFiles(new FileTypeFilter());
					if (files != null) {
						for (File file : files) {
							Long lst = (Long) this.lastModifMap.get(file.getName());
							if (lst == null) {
								this.lastModifMap.put(file.getName(), Long.valueOf(file.lastModified()));
							} else if (file.lastModified() > lst.longValue()) {
								this.lastModifMap.put(file.getName(), Long.valueOf(file.lastModified()));
								KasiteConfig.print(file.getName() + "文件修改了");
								this.change.onChange(file);
							}
						}
					}

				}
			} catch (Exception e) {
				KasiteConfig.print(e.getMessage());
			}
			try {
				Thread.sleep(this.delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
