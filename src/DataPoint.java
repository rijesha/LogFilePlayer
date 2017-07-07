
public class DataPoint {
	public float absoluteTime;
	public int index;
	public float time;
	public String data;
	public boolean hasGPS;
	public double lat;
	public double lon;
	public double alt;

	public DataPoint(int index, float time, String data, float absoluteTime, boolean hasGPS, double lat, double lon, double alt){
		this.index = index;
		this.time = time;
		this.data = data;
		this.absoluteTime = absoluteTime;
		this.hasGPS = hasGPS;
		if (this.hasGPS){
			this.lat = lat;
			this.lon = lon;
			this.alt = alt;
		}
	}		
}