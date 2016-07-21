package com.crossfive.framework.exec;

import java.util.concurrent.atomic.AtomicInteger;

public class TestThread2 implements Runnable {

	public AtomicInteger ids = new AtomicInteger(0);
	
	@Override
	public void run() {
		ids.incrementAndGet();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ids.incrementAndGet();
		System.out.println(ids.get());
	}

}
