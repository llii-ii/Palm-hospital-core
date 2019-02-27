package com.kasite.core.common.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.config.KasiteConfig;


public class LogAppender implements LogStorage {
	//// 文件的最大长度，单位是字节
	private final static int MAX_FILE_SIZE = Integer.getInteger(
			"yihu.log.MAX_FILE_SIZE", 200);
	// 文件的存在时间，超过这个时间就要使用新文件
	private final static long ALIVE_TIME = Integer.getInteger(
			"yihu.log.ALIVE_TIME", 1000 * 30);

	private final static long MAX_FILE_LENGTH = Integer.getInteger(
			"yihu.log.MAX_FILE_LENGTH", 190 * 1024);
	// 当前log文件的创建时间
	private long createTime = -1;
	// 当前log的记录数
	private volatile int fileSize;
	private volatile int fileLength;
	private OutputStream out;
	private File logFile;

	/* (non-Javadoc)
	 * @see com.coreframework.log.LogStorage#append(java.lang.Object)
	 */
	@Override
	public void append(Object obj) {
		if (obj == null) {
			return;
		}
		if (String.class.isInstance(obj)) {
			this.appendString((String) obj);
			return;
		}
		JSONObject jo = (JSONObject) JSON.toJSON(obj);
		String text = jo.toString();
		this.appendString(text);
	}

	/* (non-Javadoc)
	 * @see com.coreframework.log.LogStorage#appendList(java.util.List)
	 */
	@Override
	public void appendList(List<LogInfo> objs) {
		if (objs == null || objs.isEmpty()) {
			return;
		}
		for (LogInfo obj : objs) {
			if (obj == null) {
				continue;
			}
			JSONObject jo = (JSONObject) JSON.toJSON(obj);
			String text = jo.toString();
			this.appendString(text);
		}
	}

	/**
	 * 将文本写入到文件中，不会自动添加换行符
	 * 
	 * @param text
	 * @param recordSize
	 * @throws IOException
	 */
	private void _append(String text, int recordSize) throws IOException {
		byte[] bs = text.getBytes(LogFileUtils.charset);
		out.write(bs);
		fileSize += recordSize;
		this.fileLength += bs.length;
		// 需要重新dump一个文件
		if (fileSize >= MAX_FILE_SIZE
				|| System.currentTimeMillis() - createTime >= ALIVE_TIME
				|| this.fileLength > MAX_FILE_LENGTH) {
			reset();
		}
	}

	/**
	 * 创建失败就返回null
	 * 
	 * @return
	 */
	private OutputStream createOutput() {
		for (int i = 0; i < 10; i++) {
			try {
				String name = LogFileUtils.buildLogFileName();
				File f = new File(LogFileUtils.getLogingPath(), name);
				if (!f.getParentFile().exists()) {
					File parent = f.getParentFile();
					if (!parent.mkdirs()) {
						KasiteConfig.print("create folder "
								+ parent.getAbsolutePath() + " failed!!!");
						return null;
					}
				}
				if (!f.createNewFile()) {
					KasiteConfig.print("create file " + f.getAbsolutePath()
					+ " failed !!!");
					return null;
				}
				logFile = f;
				return new InnerBufferedOutputStream(new FileOutputStream(f));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void appendString(String text) {
		if (out == null) {
			out = createOutput();
			createTime = System.currentTimeMillis();
			this.fileSize = 0;
		}
		if (out == null) {
			System.err.println("###discard test:" + text);
			return;
		}
		try {
			if (this.fileSize > 0) {
				out.write(LogFileUtils.LINE_SPLIT);
			}
			_append(text, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		LogFileUtils.buildLogFileName();
	}

	@Override
	public void reset() throws IOException {
		if (out == null || this.fileSize == 0) {
			return;
		}
		out.flush();
		out.close();
		out = null;
		LogFileUtils.move2Logged(logFile);
		logFile = null;
		this.fileSize = 0;
		this.fileLength = 0;
	}

	private static class InnerBufferedOutputStream extends FilterOutputStream {
		static byte[] buf = new byte[1024 * 200];
		static int count;


		public InnerBufferedOutputStream(OutputStream out) {
			super(out);
			count=0;
		}

		private void flushBuffer() throws IOException {
			if (count > 0) {
				out.write(buf, 0, count);
				count = 0;
			}
		}

		public void write(int b) throws IOException {
			if (count >= buf.length) {
				flushBuffer();
			}
			buf[count++] = (byte) b;
		}

		public void write(byte b[], int off, int len) throws IOException {
			if (len >= buf.length) {
				flushBuffer();
				out.write(b, off, len);
				return;
			}
			if (len > buf.length - count) {
				flushBuffer();
			}
			System.arraycopy(b, off, buf, count, len);
			count += len;
		}

		public void flush() throws IOException {
			flushBuffer();
			out.flush();
		}
	}

	@Override
	public void stop() {
		try {
			this.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
