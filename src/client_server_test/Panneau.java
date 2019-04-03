package client_server_test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

public class Panneau extends JPanel {
	protected HashMap<String,Player> playerMap = new HashMap<String, Player>();
	protected String name;
	protected Objective obj;
	public Player p = null;
	protected final double turnit = 0.3;
	protected final double thrustit = 0.5;
	public void paintComponent(Graphics g) {
		// On décide d'une couleur de fond pour notre rectangle
		g.setColor(Color.black);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
  		g.setColor(Color.red);
  		for(Player p : playerMap.values())
  		{
  			if(p.name != this.name)
  				g.fillOval((int)p.x,(int) p.y, 30, 30);
  			/*g.drawString(p.name, (int)p.x ,(int)p.y + 50);
  			g.drawString(Integer.toString(p.score), (int)p.x ,(int)p.y - 30);*/
  		}
  		if(p!=null) {
  			g.fillOval((int)getPosX(), (int)getPosY(), 50, 50);
  			g.drawLine((int)getPosX()+25, (int)getPosY()+25, (int)(getPosX()+40*Math.cos(p.dir))+25, (int)(getPosY()+40*Math.sin(p.dir))+25);
  			g.drawString(p.name, (int)p.x ,(int)p.y + 70);
  			g.drawString(Integer.toString(p.score) , (int)p.x ,(int)p.y - 30);
  			
  		}
  		if(obj != null) {
  			g.setColor(Color.gray);
  			g.fillOval((int)obj.getX(), (int)obj.getY(), 20, 20);
  		}
	}
	public void thrust()
	{
		if(p!=null) {
			p.vx = p.vx + thrustit*Math.cos(p.dir);
			p.vy = p.vy + thrustit*Math.sin(p.dir);

		}
	}
	public void clock() {
		if(p!=null) {
			p.dir = (p.dir-turnit)%360;
		}
	}
	public void anticlock() {
		if(p!=null) {
			p.dir = (p.dir+turnit)%360;
		}
	}
	public void addMap(String name, double x, double y) {
		if (playerMap.containsKey(name)){
			Player newp =playerMap.get(name);
			newp.x = x;
			newp.y = y;
		}else {
			Player newp = new Player(name, x, y, 0, 0, 0, 0);
			if(name == this.name) {
				this.p = newp;
			}
			playerMap.put(name, newp );
		}
		
	}
	public void updateScore(String namesc, int score) {
		if (playerMap.containsKey(namesc)){
			playerMap.get(namesc).score = score;
		}
	}
	public int getScore() {
		return p.score;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setObjective(Objective o) {
		obj = o;
	}
	public void setPlayer(Player p) {
		this.p = p;
	}
	public Player getPlayer() {
		return p;
	}
	public double getPosX() {
		return this.p.x;
	}
	public void setPosX(double posX) {
	    this.p.x = posX;
	}
	public double getPosY() {
	    return this.p.y;
	}
	public void setPosY(double posY) {
	    this.p.y = posY;
	}
}