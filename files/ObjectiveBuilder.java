package client_server_test;

public class ObjectiveBuilder
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
	
	public Objective create()
	{
		if(x == null || y == null)
			return null;
		Objective result = new Objective(x, y);
		reset();
		return result;
	}
	
	public void reset()
	{
		x = null;
		y = null;
	}
}
