package pc2r_client_piece.model;

public class Arena
{
	public double l, h;
	
	public Arena(double l, double h)
	{
		this.l = l;
		this.h = h;
	}
	
	public Arena()
	{
		this(50, 50);
	}
	
	public double pos_x(double x)
	{
		return tore(x, l);
	}
	
	public double pos_y(double y)
	{
		return tore(y, h);
	}
	
	private double tore(double pos, double d)
	{
		if(pos > d)
		{
			return (pos + d) % (2 * d) - d;
		}
		else if (pos < -d)
		{
			return -((-pos + d) % (2 * d) - d);
		}
		else
			return pos;
	}
	
	@Override
	public String toString()
	{
		return "Arena = (" + l + ", " + h + ")";
	}
}
