package com.lic.epgs.gratuity.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorUtils {

	private static ExecutorService executorSerive = Executors.newFixedThreadPool(100);

	private static ExecutorService singleExecutor = Executors.newSingleThreadExecutor();

	public static ExecutorService getExecutorService() {
		if (executorSerive.isShutdown()) {
			executorSerive = Executors.newFixedThreadPool(100);
		}
		return executorSerive;
	}

	public static ExecutorService getSingleExecutor() {
		if (singleExecutor.isShutdown()) {
			singleExecutor = Executors.newSingleThreadExecutor();
		}
		return singleExecutor;
	}

	public static ExecutorService getNewExecutorService() {
		return Executors.newSingleThreadExecutor();
	}

}
