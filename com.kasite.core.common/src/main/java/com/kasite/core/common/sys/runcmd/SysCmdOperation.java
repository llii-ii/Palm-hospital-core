package com.kasite.core.common.sys.runcmd;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author hekuiwei
 * @company yihu.com 2013-11-8上午11:19:24
 */
public abstract class SysCmdOperation {
	public static final Log LOG = LogFactory.getLog(SysCmdOperation.class);
	/** 超时时间 */
	protected long timeOutInterval = 0L;
	/** 是否超时 */
	private AtomicBoolean timedOut;
	/** 是否windows系统 */
	public static final boolean WINDOWS = System.getProperty("os.name")
			.startsWith("Windows");
	private long interval; // 刷新间隔
	private long lastTime; // 最后时间
	private Map<String, String> environment; // 系统配置
	private File dir;
	private Process process;
	private int exitCode;
	
	/** 脚本是否执行完 */
	private volatile AtomicBoolean completed;

	public SysCmdOperation() {
		this(0L);
	}

	public SysCmdOperation(long interval) {
		this.interval = interval;
		this.lastTime = (interval < 0) ? 0 : -interval;
	}

	protected void setEnvironment(Map<String, String> env) {
		this.environment = env;
	}

	protected void setWorkingDirectory(File dir) {
		this.dir = dir;
	}

	/** 判断是否超时 */
	protected void run() throws IOException {
		if (lastTime + interval > System.currentTimeMillis())
			return;
		exitCode = 0; // reset for next run
		runCommand();
	}

	/** 命令执行 */
	private void runCommand() throws IOException {
		ProcessBuilder builder = new ProcessBuilder(getExecString());
		Timer timeOutTimer = null;
		ShellTimeoutTimerTask timeoutTimerTask = null;
		timedOut = new AtomicBoolean(false);
		completed = new AtomicBoolean(false);

		if (environment != null) {
			builder.environment().putAll(this.environment);
		}
		if (dir != null) {
			builder.directory(this.dir);
		}
		builder.redirectErrorStream(true);
		process = builder.start();
		if (timeOutInterval > 0) {
			timeOutTimer = new Timer();
			timeoutTimerTask = new ShellTimeoutTimerTask(this);
			// One time scheduling.
			timeOutTimer.schedule(timeoutTimerTask, timeOutInterval);
		}
		BufferedReader inReader = new BufferedReader(new InputStreamReader(
				process.getInputStream(),Charset.forName("UTF-8")));

		try {
			parseExecResult(inReader); // 解析结果
			// 等待完成检查的退出代码
			process.waitFor();
			completed.set(true);
		} catch (InterruptedException ie) {
			throw new IOException(ie.toString());
		} finally {
			if ((timeOutTimer != null) && !timedOut.get()) {
				timeOutTimer.cancel();
			}
			try {
				inReader.close();
			} catch (IOException ioe) {
				LOG.warn("Error while closing the input stream", ioe);
			}
			
			process.destroy();
			lastTime = System.currentTimeMillis();
		}
	}

	/** 返回一个数组，包含命令的名称和参数 */
	protected abstract String[] getExecString();

	/** 解析执行结果 */
	protected abstract void parseExecResult(BufferedReader lines)
			throws IOException;

	
	public Process getProcess() {
		return process;
	}

	/**
	 * 获取退出代码
	 * 
	 */
	public int getExitCode() {
		return exitCode;
	}

	/**
	 * 是否超时
	 * 
	 * 
	 */
	public boolean isTimedOut() {
		return timedOut.get();
	}

	/**
	 * 设置超时
	 * 
	 */
	private void setTimedOut() {
		this.timedOut.set(true);
	}

	public static String execCommand(String... cmd) throws IOException {
		return execCommand(null, cmd, 0L);
	}

	public static String execCommand(Map<String, String> env, String[] cmd,
			long timeout) throws IOException {
		CommandExecutor exec = new CommandExecutor(cmd, null, env,
				timeout);
		exec.execute();
		return exec.getOutput();
	}

	public static String execCommand(Map<String, String> env, String... cmd)
			throws IOException {
		return execCommand(env, cmd, 0L);
	}

	/**
	 * 计算超时.
	 */
	private static class ShellTimeoutTimerTask extends TimerTask {

		private SysCmdOperation sysCmdOperation;

		public ShellTimeoutTimerTask(SysCmdOperation sysCmdOperation) {
			this.sysCmdOperation = sysCmdOperation;
		}

		@Override
		public void run() {
			Process p = sysCmdOperation.getProcess();
			try {
				p.exitValue();
			} catch (Exception e) {
				// Process has not terminated.
				// So check if it has completed
				// if not just destroy it.
				if (p != null && !sysCmdOperation.completed.get()) {
					sysCmdOperation.setTimedOut();
					p.destroy();
				}
			}
		}
	}
}
