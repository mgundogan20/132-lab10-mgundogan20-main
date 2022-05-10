
public class SynchBuffer implements Buffer {

	private float value = -1f;
	private boolean occupied = false;
	
	public SynchBuffer() {
		
	}
	
	@Override
	public synchronized void addLatestPrice(float value) throws InterruptedException {
		while(occupied) {
			wait();
		}
		occupied = true;

		this.value = value;
		
		notifyAll();
	}

	@Override
	public synchronized float getLatestPrice() throws InterruptedException {
		while(!occupied) {
			wait();
		}
		
		occupied = false;
		
		notifyAll();
		
		return value;
	}

	@Override
	public synchronized float checkLatestPrice() throws InterruptedException {
		if(occupied)
			return value;
		else
			return -1f;
	}

}
