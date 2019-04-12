package pc2r_client_piece.model.interfaces;

public interface IMovingEntity extends IPositionEntity
{
	public double get_vx();
	public double get_vy();
	public void set_vx(double vx);
	public void set_vy(double vy);
	public void move();
}
