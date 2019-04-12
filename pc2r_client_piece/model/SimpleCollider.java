package pc2r_client_piece.model;

import pc2r_client_piece.model.interfaces.IMovingEntity;

public class SimpleCollider
{
	void collide(IMovingEntity lhs, IMovingEntity rhs)
	{
		lhs.set_vx(-lhs.get_vx());
		lhs.set_vy(-lhs.get_vy());
		rhs.set_vx(-rhs.get_vx());
		rhs.set_vy(-rhs.get_vy());
	}
}
