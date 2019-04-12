package client_server_test;

public class Vector2Builder
{
	private Double x = null;
	private Double y = null;
	
	public void set_x(double x)
	{
		this.x = x;
	}
	
	public void set_y(double y)
	{
		this.y = y;
	}
	
	public Vector2 create()
	{
		if(x == null || y == null)
			return null;
		Vector2 result = new Vector2(x, y);
		reset();
		return result;
	}
	
	public void reset()
	{
		x = null;
		y = null;
	}
}
