import java.util.concurrent.ArrayBlockingQueue;

public class BlockingBuffer implements Buffer{
	
	private final ArrayBlockingQueue<Float> buffer = new ArrayBlockingQueue<Float>(1);
	
	@Override
	public void addLatestPrice(float value) throws InterruptedException {
		buffer.put(value);
	}

	@Override
	public float getLatestPrice() throws InterruptedException {
		float value = buffer.take();
		return value;
	}

	@Override
	public float checkLatestPrice() throws InterruptedException {
		if(buffer.peek() == null)
			return -1;
		else {			
			float output = buffer.peek();
			return output;
		}
	}
	
}
