

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		//preLab();
		inLab();
	}
	
	private static void inLab() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		Buffer exchangeBuffer = new BlockingBuffer();
		
		
		Investor investor1 = new Investor(exchangeBuffer, "Satoshi", 2000, 300);
		Investor investor2 = new Investor(exchangeBuffer, "Vitalik", 1700, 200);
		
		executorService.execute(investor1);
		executorService.execute(investor2);
		executorService.execute(new ExchangePlatform(exchangeBuffer, 49500, 2000));
		executorService.shutdown();
		
		try {
			executorService.awaitTermination(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private static void preLab() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		Buffer exchangeBuffer = new SynchBuffer();
		
		
		Investor investor1 = new Investor(exchangeBuffer, "Satoshi", 2000, 300);
		Investor investor2 = new Investor(exchangeBuffer, "Vitalik", 1700, 200);
		
		executorService.execute(investor1);
		executorService.execute(investor2);
		executorService.execute(new ExchangePlatform(exchangeBuffer, 49500, 2000));
		executorService.shutdown();
		
		try {
			executorService.awaitTermination(20, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
