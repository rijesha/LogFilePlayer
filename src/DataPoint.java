
public class DataPoint {
	public float absoluteTime;
	public int index;
	public float time;
	public String data;
	public DataPoint(int index, float time, String data, float absoluteTime){
		this.index = index;
		this.time = time;
		this.data = data;
		this.absoluteTime = absoluteTime;
	}		
}