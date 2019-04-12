package pc2r_client_piece.model;

import pc2r_client_piece.model.interfaces.IMovingEntity;

public class MovingEntity extends PositionEntity implements IMovingEntity
{
	protected double vx, vy;
	
	public MovingEntity(Arena arena, double x, double y, double vx, double vy, double radius)
	{
		super(arena, x, y, radius);
		this.vx = vx;
		this.vy = vy;
	}

	@Override
	public double get_vx()
	{
		return vx;
	}

	@Override
	public double get_vy()
	{
		return vy;
	}

	@Override
	public void set_vx(double vx)
	{
		this.vx = vx;
	}

	@Override
	public void set_vy(double vy)
	{
		this.vy = vy;
	}

	@Override
	public void move()
	{
		set_x(x + vx);
		set_y(y + vy);
	}
	
	@Override
	public String toString()
	{
		return "Entity = " + super.toString() + " -> (" + vx + ", " + vy + ")";
	}
}
