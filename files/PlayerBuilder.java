package client_server_test;

public class PlayerBuilder
{
	private String name = null;
	private Double x = null;
	private Double y = null;
	private Double vx = null;
	private Double vy = null;
	private Integer score = null;
	private Double dir = null;
	
	public void set_name(String name)
	{
		this.name = name;
	}
	
	public void set_x(double x)
	{
		this.x = x;
	}
	
	public void set_y(double y)
	{
		this.y = y;
	}
	
	public void set_vx(double vx)
	{
		this.vx = vx;
	}
	
	public void set_vy(double vy)
	{
		this.vy = vy;
	}
	
	public void set_score(int score)
	{
		this.score = score;
	}
	
	public void set_dir(double dir)
	{
		this.dir = dir;
	}
	
	Player create()
	{
		if(name == null || x == null || y == null || vx == null || vy == null || score == null || dir == null)
			return null;
		Player result = new Player(name, x, y, vx, vy, score, dir);
		reset();
		return result;
	}
	
	void reset()
	{
		x = null;
		y = null;
		vx = null;
		vy = null;
		dir = null;
		score = null;
		name = null;
	}
}
