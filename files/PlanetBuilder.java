package client_server_test;

public class PlanetBuilder
{
	private Double x;
	private Double y;
	
	public void set_x(double x)
	{
		this.x = x;
	}
	
	public void set_y(double y)
	{
		this.y = y;
	}
	
	public Planet create()
	{
		if(x == null || y == null)
			return null;
		Planet result = new Planet(x, y);
		reset();
		return result;
	}
	
	public void reset()
	{
		x = null;
		y = null;
	}
}
