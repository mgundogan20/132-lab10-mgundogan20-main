import java.security.SecureRandom;

public class Investor implements Runnable{

	private Buffer buffer;
	private String name;
	private float takeProfit;
	private float stopLoss;
	private boolean invested = false;
	private float buyingPrice;
	private SecureRandom randomGen = new SecureRandom();
	
	public Investor(Buffer exchangeBuffer, String name, float takeProfit, float stopLoss) {
		this.takeProfit = takeProfit;
		this.stopLoss = stopLoss;
		this.buffer = exchangeBuffer;
		this.name = name;
	}
	
	public void investment() {
		try {
			Thread.sleep(randomGen.nextInt(1000));
			float latestPrice = buffer.checkLatestPrice();
			if(latestPrice != -1) {				
				if(!invested) {
					buyingPrice = buffer.getLatestPrice();
					Logger.displayState("[" + name + "]" + ", Bought at: " + buyingPrice);
					invested = true;
				}
				else {
					if(buyingPrice+takeProfit < latestPrice) {
						Logger.displayState(
								"[" + name + "]" + " Take profit at: " + (buyingPrice + takeProfit) + ", Bought at: " + buyingPrice + ", Sold at: " + 
										latestPrice + ", Profit: " + (latestPrice - buyingPrice));
						invested = false;
					}
					else if(buyingPrice-stopLoss > latestPrice) {
						Logger.displayState(
								"[" + name + "]" + " Stop loss at " + (buyingPrice - stopLoss) + ", Bought at: " + buyingPrice + 
								", Sold at: " + latestPrice + 
								", Loss: " + (latestPrice - buyingPrice));
						invested = false;
					}
				}
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		//while(true)
		for(int i=0; i<100 ;i++)
			investment();
	}
	
	
}
