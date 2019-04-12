package pc2r_client_piece.model;

import pc2r_client_piece.model.interfaces.IPositionEntity;

public class PositionEntity implements IPositionEntity
{
	protected Arena arena;
	protected double x, y;
	protected double radius;
	
	public PositionEntity(Arena arena, double x, double y, double radius)
	{
		this.arena = arena;
		this.x = arena.pos_x(x);
		this.y = arena.pos_y(y);
		this.radius = radius;
	}
	
	@Override
	public double get_x()
	{
		return x;
	}

	@Override
	public double get_y()
	{
		return y;
	}
	
	@Override
	public void set_x(double x)
	{
		this.x = arena.pos_x(x);
	}

	@Override
	public void set_y(double y)
	{
		this.y = arena.pos_y(y);
	}
	
	@Override
	public double get_radius()
	{
		return radius;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + "):" + radius;
	}
}
