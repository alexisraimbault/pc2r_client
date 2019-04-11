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
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Panneau extends JPanel {
	protected HashMap<String,Player> playerMap = new HashMap<String, Player>();
	protected String name;
	private BufferedImage vaisseau;
	private Image chest;
	private Image ennemy;
	private Image space;
	private List<Image> bg;
	private Image planet1;
	public double plx1,plx2,ply1,ply2;
	private Image planet2;
	protected Objective obj;
	protected String winner;
	public Player p = null;
	protected final double turnit = 0.3;
	protected final double thrustit = 0.5;
	protected int cptBG;
	protected int cptBGint;
	protected boolean attente = true;
	protected boolean scoreboard = false;
	public Panneau() {
		try {
			Image tmp;
			bg = new ArrayList<Image>();
			cptBG = 0;
			cptBGint = 0;
			for(int i=0; i<1; i++) {
				if(i<10)
					tmp = ImageIO.read(new File("background_0000"+i+".png")).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH );
				else
					tmp = ImageIO.read(new File("background_000"+i+".png")).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH );
				while(tmp.getWidth(null)==-1) {
				
				}
				bg.add(tmp);
				System.out.println("IMAGE "+(i+1)+"/72 LOADED.");
			}
			chest = ImageIO.read(new File("chest.png")).getScaledInstance(100, 122, Image.SCALE_SMOOTH );
			ennemy = ImageIO.read(new File("ennemy.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH );
			//space = ImageIO.read(new File("space.png")).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH );
			planet1 = ImageIO.read(new File("planet1.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH );
			planet2 = ImageIO.read(new File("planet2.png")).getScaledInstance(160, 160, Image.SCALE_SMOOTH );
			BufferedImage tmpVaisseau = ImageIO.read(new File("vaisseau.png"));
			int w = tmpVaisseau.getWidth();
			int h = tmpVaisseau.getHeight();
			
			double scaleX = (double)w/140;
			double scaleY = (double)h/140;
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
		if(!attente) {
			/*g.setColor(Color.black);//couleur de fond
			g.fillRect(0, 0, this.getWidth(), this.getHeight());*/
			g.drawImage(bg.get(cptBG), 0, 0, null);
			cptBGint++;
			if(cptBGint%2 == 0) {
				cptBG= (cptBG+1)%bg.size();
				cptBGint = 0;
			}
			
			g.drawImage(planet1, (int)(plx1*20), (int)(ply1*20), null);
			g.drawImage(planet2, (int)(plx2*20)-80, (int)(ply2*20)-80, null);
			
	  		g.setColor(Color.red);
	  		for(Player p : playerMap.values())
	  		{
	  			if(!Objects.equals(p.name ,this.name)) {
	  				g.drawImage(ennemy, (int)p.x - 30, (int)p.y - 30, null);
	  				//g.fillOval((int)p.x,(int) p.y, 30, 30);
	  			}
	  				
	  			
	  			/*g.drawString(p.name, (int)p.x ,(int)p.y + 50);
	  			g.drawString(Integer.toString(p.score), (int)p.x ,(int)p.y - 30);*/
	  		}
	  		if(p!=null) {
	  			//g.fillOval((int)getPosX(), (int)getPosY(), 50, 50);
	  			
	  			//g.drawLine((int)getPosX()+25, (int)getPosY()+25, (int)(getPosX()+40*Math.cos(p.dir))+25, (int)(getPosY()+40*Math.sin(p.dir))+25);
	  			
	  			
	  			AffineTransform tx = AffineTransform.getRotateInstance(p.dir+90, 70, 70);
	  			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	  			g.drawImage(op.filter(vaisseau ,null),(int)p.x - 70,(int)p.y - 70,null);
	  			if(playerMap.containsKey(p.name)) {
	  				Player pl = playerMap.get(p.name);
	  				g.drawString(Integer.toString(pl.score) , (int)p.x ,(int)p.y - 70);
	  			}
	  			
	  			g.drawString(p.name, (int)p.x ,(int)p.y + 80);
	  			//g.drawString(Integer.toString(p.score) , (int)p.x ,(int)p.y - 70);
	  			
	  		}
	  		if(obj != null) {
	  			g.setColor(Color.gray);
	  			g.drawImage(chest, (int)obj.getX() - 50, (int)obj.getY() - 61, null);
	  			//g.fillOval((int)obj.getX(), (int)obj.getY(), 20, 20);
	  		}
	  		if(scoreboard) {
	  			g.setColor(new Color((float)0.8,(float)0.8,(float)0.8,(float)0.5));
	  			
	  			g.fillRect(20, 20, 400, 50*(playerMap.size()) + 20);
	  			
	  			g.setColor(Color.black);
	  			int cpt = 1;
	  			for(Player p : playerMap.values())
		  		{
		  			g.drawString(p.name + "\t" + Integer.toString(p.score), 100 ,50*cpt);
		  			g.drawString(Integer.toString(p.score), 300 ,50*cpt);
	  				cpt++;
		  		}
	  		}
		}
		else {
			g.setColor(Color.black);//couleur de fond
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.white);
			g.drawString("ATTENTE ...", 200 ,200);
			if(winner != null) {
				g.drawString("WINNER : " + winner, 200 ,400);
				
			}
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
	public void updateScore(String namesc, int score, double vx, double vy) {
		if (playerMap.containsKey(namesc)){
			playerMap.get(namesc).score = score;
			playerMap.get(namesc).vx = vx;
			playerMap.get(namesc).vy = vy;
		}
	}
	public void stepPlayers() {
		double d, tmpx, tmpy;
		for(Player pl : playerMap.values())
  		{
			if(!Objects.equals(pl.name, p.name)) {
				pl.x = ((pl.x + pl.vx)+1000)%1000;
				pl.y = ((pl.y + pl.vy)+1000)%1000;
				/*for(Player pl1 : playerMap.values())
		  		{
					d = Math.sqrt((pl.x - pl1.x)*(pl.x - pl1.x) + (pl.y - pl1.y)*(pl.y - pl1.y));
			    	  if(d<100) {
			    		  tmpx=pl1.vx;
			    		  tmpy=pl1.vx;
			    		  pl1.vx = pl.vx;
			    		  pl1.vy = pl.vy;
			    		  pl.vx = tmpx;
			    		  pl.vy = tmpy;
			    	  }
		  		}*/
			}
  		}
	}
	public void setWinner(String w) {
		winner = w;
	}
	public void setAttente( boolean b) {
		attente = b;
	}
	public void setScores( boolean b) {
		scoreboard = b;
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