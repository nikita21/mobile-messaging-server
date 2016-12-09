package com.app.mobile.messaging.server.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorUtils
{
    private static final Logger logger = LoggerFactory.getLogger(ExecutorUtils.class);

    public static ThreadPoolExecutor getNewThreadPoolExecutor(int numThreads)
    {
	return getNewThreadPoolExecutor(numThreads, 10, TimeUnit.SECONDS, numThreads * 5,
		new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static ThreadPoolExecutor getNewThreadPoolExecutor(int numThreads,
							      int timeoutVal,
							      TimeUnit timeoutUnit,
							      int queueSize,
							      RejectedExecutionHandler rejectedHandler)
    {
	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(numThreads, numThreads, timeoutVal, timeoutUnit,
								       new LinkedBlockingQueue<Runnable>(queueSize),
								       rejectedHandler);
	threadPoolExecutor.allowCoreThreadTimeOut(true);
	return threadPoolExecutor;
    }

    public static void shutdownAndWait(ThreadPoolExecutor threadPoolExecutor, int waitInterval, TimeUnit waitTimeUnit)
    {
	threadPoolExecutor.shutdown();
	int count = 0;
	boolean wait = true;
	while (wait)
	{
	    try
	    {
		wait = (!threadPoolExecutor.awaitTermination(waitInterval, waitTimeUnit));
		if (wait && ++count >= 10)
		{
		    logger.info("Executor is still executing after {} {}", (waitInterval * 10), waitTimeUnit);
		    count = 0;
		}
	    }
	    catch (InterruptedException e)
	    {
		// do Nothing
	    }
	}
    }
}

