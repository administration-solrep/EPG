package fr.dila.solon.birt;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MonitoringThread implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(MonitoringThread.class);

	private final AtomicBoolean running = new AtomicBoolean(false);
	private long interval; // Interval (ms) between two ticks

	public MonitoringThread(long interval) {
		this.interval = interval;
	}

	public boolean start() {
		LOGGER.info("Starting monitoring thread");
		Thread worker = new Thread(this);
		worker.start();
		return running.get();
	}

	public void stop() {
		running.set(false);
		LOGGER.info("Stopping monitoring thread");
	}

	/**
	 * Wait given time then stop thread.
	 * 
	 * @param interval time in ms
	 */
	public void waitThenStop(long interval) {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException(e); // NOSONAR throw generic exception to make sure it doesn't get caught
		} finally {
			stop();
		}
	}

	private void logMemoryUsage() {
		long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		LOGGER.info("Memory usage: {}", usedMemory);
	}

	@Override
	public void run() {
		running.set(true);
		while (running.get()) {
			logMemoryUsage();

			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				LOGGER.debug(e);
			}
		}
	}
}
