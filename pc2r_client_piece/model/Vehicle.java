package pc2r_client_piece.model;

import pc2r_client_piece.model.interfaces.IMovingEntity;

public class Vehicle implements IMovingEntity
{
	public Vehicle(MovingEntity entity, double dir)
	{
		this.entity = entity;
		this.dir = dir;
	}
	
	public IMovingEntity entity;
	public double dir;
	
	@Override
	public double get_x()
	{
		return entity.get_x();
	}
	@Override
	public double get_y()
	{
		return entity.get_y();
	}
	@Override
	public void set_x(double x)
	{
		entity.set_x(x);
	}
	@Override
	public void set_y(double y)
	{
		entity.set_y(y);
	}
	@Override
	public double get_radius()
	{
		return entity.get_radius();
	}
	@Override
	public double get_vx()
	{
		return entity.get_vx();
	}
	@Override
	public double get_vy()
	{
		return entity.get_vy();
	}
	@Override
	public void set_vx(double vx)
	{
		entity.set_vx(vx);
	}
	@Override
	public void set_vy(double vy)
	{
		entity.set_vy(vy);
	}
	@Override
	public void move()
	{
		entity.move();
	}
	
	public void clock(double turnit)
	{
		dir -= turnit;
	}
	
	public void anticlock(double turnit)
	{
		dir += turnit;
	}
	
	public void thrust(double thrustit)
	{
		set_vx(get_vx() + thrustit * Math.cos(dir));
		set_vy(get_vy() + thrustit * Math.sin(dir));
	}
	
	double get_dir()
	{
		return dir;
	}
	
	void set_dir(double dir)
	{
		this.dir = dir;
	}
	
	@Override
	public String toString()
	{
		return entity.toString();
	}
	
}
