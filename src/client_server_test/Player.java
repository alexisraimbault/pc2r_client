package client_server_test;

public class Player {
	protected double x,y,vx,vy,dir;
	protected int score;
	protected String name;
	
	public Player(String name, double x, double y, double vx, double vy, int score, double dir) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.score = score;
		this.dir = dir;
	}
}
