package com.lic.epgs.gratuity.quotation.member.config;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author Ismail Khan A
 *
 */

@Component
public class ThreadPoolExecutorUtil {
	private Logger logger = LogManager.getLogger(ThreadPoolExecutorUtil.class);

	private ThreadPoolExecutor threadPoolExecutor;

	public ThreadPoolExecutorUtil() {

		BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue(10000);
		threadPoolExecutor = new ThreadPoolExecutor(2, 10, 20, TimeUnit.SECONDS, blockingQueue);
		threadPoolExecutor.setRejectedExecutionHandler((r, executor) -> {
			try {
				Thread.sleep(1000);
				logger.error("Exception occurred while adding task, Waiting for some time");
			} catch (InterruptedException e) {
				logger.error("Thread interrupted:  ()", e.getCause());
				Thread.currentThread().interrupt();
			}
			threadPoolExecutor.execute(r);
		});
	}

	public void executeTask(TaskThread taskThread) {
		Future<?> future = threadPoolExecutor.submit(taskThread);
		System.out.println("Queue Size: " + threadPoolExecutor.getQueue().size());
		System.out.println("Number of Active Threads: " + threadPoolExecutor.getActiveCount());

		/*
		 * while(future.isDone()) { try { future.get();
		 * logger.info("TaskThread.employees: {}",taskThread.employees); } catch
		 * (Exception e) { logger.error("Thread interrupted:  ()",e.getCause()); } }
		 */
	}
}
