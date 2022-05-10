import java.security.SecureRandom;

public class ExchangePlatform implements Runnable{
	
	private SecureRandom randomGen = new SecureRandom();
	private float midPrice;
	private float variance;
	private Buffer buffer;
	
	public ExchangePlatform(Buffer exchangeBuffer, float midPrice, float variance) {
		buffer = exchangeBuffer;
		this.midPrice = midPrice;
		this.variance = variance;
	}
	
	private void generateValues() {
		for(int i=0; i<10 ;i++) {
			try {
				Thread.sleep(randomGen.nextInt(2000));
				
				float latest = midPrice - variance + 2*variance*randomGen.nextFloat();				
				buffer.addLatestPrice(latest);
				
				Logger.displayState("Latest price set by the crypto exchange platform: " + latest);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void run() {
		generateValues();
	}
	
	
}
