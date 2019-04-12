package pc2r_client_piece;

import pc2r_client_piece.model.Arena;
import pc2r_client_piece.model.Linearizer;

public class ArenaView
{
	private Linearizer linearize_x;
	private Linearizer linearize_y;
	
	public ArenaView(Arena arena, double sx, double sy)
	{
		linearize_x = new Linearizer(-arena.l, arena.l, 0, sx);
		linearize_y = new Linearizer(-arena.h, arena.h, 0, sy);
	}
	
	public ArenaView(Arena arena)
	{
		this(arena, 1000, 1000);
	}
	
	public double getPixelX(double ax)
	{
		return linearize_x.linearize(ax);
	}
	
	public double getPixelY(double ay)
	{
		return linearize_y.linearize(ay);
	}
}
