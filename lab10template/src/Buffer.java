

public interface Buffer {
	public void addLatestPrice(float value) throws InterruptedException;
	public float getLatestPrice() throws InterruptedException;
	public float checkLatestPrice() throws InterruptedException;
}
