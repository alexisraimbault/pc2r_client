package client_server_test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Panneau extends JPanel {
	protected HashMap<String,Player> playerMap = new HashMap<String, Player>();
	protected String name;
	private BufferedImage vaisseau;
	private Image chest;
	private Image ennemy;
	protected Objective obj;
	public Player p = null;
	protected final double turnit = 0.3;
	protected final double thrustit = 0.5;
	public Panneau() {
		try {
			chest = ImageIO.read(new File("chest.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH );
			ennemy = ImageIO.read(new File("ennemy.png")).getScaledInstance(40, 40, Image.SCALE_SMOOTH );
			BufferedImage tmpVaisseau = ImageIO.read(new File("vaisseau.png"));
			int w = tmpVaisseau.getWidth();
			int h = tmpVaisseau.getHeight();
			
			double scaleX = (double)w/80.0;
			double scaleY = (double)h/80.0;
			System.out.println(scaleX +"-"+ scaleY);
			vaisseau = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			AffineTransform at = new AffineTransform();
			at.scale(1/scaleX, 1/scaleY);
			AffineTransformOp scaleOp = 
			   new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			vaisseau = scaleOp.filter(tmpVaisseau, vaisseau);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public void paintComponent(Graphics g) {
		
		g.setColor(Color.black);//couleur de fond
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
  		g.setColor(Color.red);
  		for(Player p : playerMap.values())
  		{
  			if(p.name != this.name) {
  				g.drawImage(ennemy, (int)p.x - 10, (int)p.y - 10, null);
  				//g.fillOval((int)p.x,(int) p.y, 30, 30);
  			}
  				
  			
  			/*g.drawString(p.name, (int)p.x ,(int)p.y + 50);
  			g.drawString(Integer.toString(p.score), (int)p.x ,(int)p.y - 30);*/
  		}
  		if(p!=null) {
  			//g.fillOval((int)getPosX(), (int)getPosY(), 50, 50);
  			
  			//g.drawLine((int)getPosX()+25, (int)getPosY()+25, (int)(getPosX()+40*Math.cos(p.dir))+25, (int)(getPosY()+40*Math.sin(p.dir))+25);
  			g.drawString(p.name, (int)p.x ,(int)p.y + 80);
  			g.drawString(Integer.toString(p.score) , (int)p.x ,(int)p.y - 70);
  			
  			AffineTransform tx = AffineTransform.getRotateInstance(p.dir+90, 40, 40);
  			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
  			g.drawImage(op.filter(vaisseau ,null),(int)p.x - 40,(int)p.y - 40,null);
  			
  		}
  		if(obj != null) {
  			g.setColor(Color.gray);
  			g.drawImage(chest, (int)obj.getX() - 10, (int)obj.getY() - 10, null);
  			//g.fillOval((int)obj.getX(), (int)obj.getY(), 20, 20);
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