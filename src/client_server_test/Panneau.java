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
	private Image portal;
	private List<Image> bg;
	private Image planet1;
	public double plx1,plx2,ply1,ply2;
	public boolean crashed = false;
	private Image planet2;
	private Image warning1;
	private Image warning2;
	private Image repairing;
	private Image repared;
	protected Objective obj;
	protected String winner;
	public boolean justUnstuck = false;
	public boolean closeToCrash = false;
	public boolean isTeleporter = false;
	public boolean justTeleported = false;
	public double teleportx1,teleportx2,teleporty1,teleporty2;
	public Player p = null;
	protected final double turnit = 0.3;
	protected final double thrustit = 0.5;
	public final Object lock = new Object();
	public int nbPoussees = 0;
	public double rotations = 0;
	protected int cptBG;
	protected int cptWarning;
	protected int cptBGint;
	protected int cptPortal;
	protected int cptPortalInt;
	protected boolean attente = true;
	protected boolean scoreboard = false;
	protected List<Image> animPortal;
	public Panneau() {
		try {
			Image tmp;
			bg = new ArrayList<Image>();
			animPortal = new ArrayList<Image>();
			cptBG = 0;
			cptBGint = 0;
			cptPortalInt = 0;
			cptPortal = 0;
			cptWarning = 0;
			for(int i=0; i<1; i++) {//replace 1 by 72 to get the full animation !
				if(i<10)
					tmp = ImageIO.read(new File("background_0000"+i+".png")).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH );
				else
					tmp = ImageIO.read(new File("background_000"+i+".png")).getScaledInstance(1000, 1000, Image.SCALE_SMOOTH );
				while(tmp.getWidth(null)==-1) {
				
				}
				bg.add(tmp);
				System.out.println("IMAGE "+(i+1)+"/72 LOADED.");
			}
			for(int i=1; i<15; i++) {//replace 1 by 72 to get the full animation !
				tmp = ImageIO.read(new File("portal"+i+".png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH );
				while(tmp.getWidth(null)==-1) {
				
				}
				animPortal.add(tmp);
			}
			chest = ImageIO.read(new File("chest.png")).getScaledInstance(100, 122, Image.SCALE_SMOOTH );
			ennemy = ImageIO.read(new File("ennemy.png")).getScaledInstance(80, 60, Image.SCALE_SMOOTH );
			portal = ImageIO.read(new File("portal.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH );
			warning1 = ImageIO.read(new File("warning_1.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH );
			warning2 = ImageIO.read(new File("warning_2.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH );
			repairing = ImageIO.read(new File("repair.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH );
			repared = ImageIO.read(new File("go.png")).getScaledInstance(60, 60, Image.SCALE_SMOOTH );
			planet1 = ImageIO.read(new File("planet1.png")).getScaledInstance(120, 120, Image.SCALE_SMOOTH );
			planet2 = ImageIO.read(new File("planet2.png")).getScaledInstance(200, 200, Image.SCALE_SMOOTH );
			BufferedImage tmpVaisseau = ImageIO.read(new File("vaisseau.png"));
			/*BufferedImage tmpPortal = ImageIO.read(new File("portal.png"));
			int wPortal = tmpPortal.getWidth();
			int hPortal = tmpPortal.getHeight();
			
			double scaleXPortal = (double)wPortal/60;
			double scaleYPortal = (double)hPortal/60;
			BufferedImage tmpPortalBuff = new BufferedImage(wPortal, wPortal, BufferedImage.TYPE_INT_ARGB);
			AffineTransform atp = new AffineTransform();
			atp.scale(1/scaleXPortal, 1/scaleYPortal);
			AffineTransformOp scaleOpp = 
			   new AffineTransformOp(atp, AffineTransformOp.TYPE_BILINEAR);
			tmpPortalBuff = scaleOpp.filter(tmpPortal, tmpPortalBuff);
			
			AffineTransform txp;
			AffineTransformOp opp;
			for(int i = 0; i<360; i+=20) {
				txp = AffineTransform.getRotateInstance(i, 30, 30);
	  			opp = new AffineTransformOp(txp, AffineTransformOp.TYPE_BILINEAR);
	  			animPortal.add(opp.filter(tmpPortalBuff ,null));
			}
			*/
  			
			int w = tmpVaisseau.getWidth();
			int h = tmpVaisseau.getHeight();
			
			double scaleX = (double)w/140;
			double scaleY = (double)h/140;
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
			g.drawImage(bg.get(cptBG), 0, 0, null);
			cptBGint++;
			if(cptBGint%2 == 0) {
				cptBG= (cptBG+1)%bg.size();
				cptBGint = 0;
			}
			g.setColor(Color.red);
			g.drawImage(planet1, (int)(plx1)-60, (int)(ply1)-60, null);
			g.drawImage(planet2, (int)(plx2)-100, (int)(ply2)-100, null);
	  		g.setColor(Color.red);
	  		for(Player p : playerMap.values())
	  		{
	  			if(!Objects.equals(p.name ,this.name)) {
	  				g.drawImage(ennemy, (int)p.x - 40, (int)p.y - 30, null);
	  			}
	  		}
	  		if(p!=null) {
	  			
	  			AffineTransform tx = AffineTransform.getRotateInstance(p.dir+45, 70, 70);
	  			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	  			g.drawImage(op.filter(vaisseau ,null),(int)p.x - 70,(int)p.y - 70,null);
	  			
	  		}
	  		if(!justUnstuck && (Math.sqrt((plx1 - p.x)*(plx1 - p.x) + (ply1 - p.y)*(ply1 - p.y))<120 || Math.sqrt((plx2 - p.x)*(plx2 - p.x) + (ply2 - p.y)*(ply2 - p.y))<120)) {
	  			if(cptWarning<10)
	  				g.drawImage(warning2,(int)p.x + 70,(int)p.y - 90,null);
	  			else
	  				g.drawImage(warning1,(int)p.x + 70,(int)p.y - 90,null);
	  			cptWarning++;
	  			if(cptWarning >= 20) {
	  				cptWarning = 0;
	  			}
	  		}
	  		if(crashed) {
	  			g.drawImage(repairing,(int)p.x + 70,(int)p.y - 90,null);
	  		}
	  		if(justUnstuck) {
	  			if(Math.sqrt((plx1 - p.x)*(plx1 - p.x) + (ply1 - p.y)*(ply1 - p.y))>90 && Math.sqrt((plx2 - p.x)*(plx2 - p.x) + (ply2 - p.y)*(ply2 - p.y))>90)
	  				justUnstuck = false;
	  			else
	  				g.drawImage(repared,(int)p.x + 70,(int)p.y - 90,null);
	  		}
	  		
	  		if(obj != null) {
	  			g.drawImage(chest, (int)obj.getX() - 50, (int)obj.getY() - 61, null);
	  			
	  		}
	  		if(isTeleporter) {
	  			g.drawImage(animPortal.get(cptPortal), (int)(teleportx1)-30, (int)(teleporty1)-30, null);
	  			g.drawImage(animPortal.get((cptPortal + 3)%animPortal.size()), (int)(teleportx2)-30, (int)(teleporty2)-30, null);
	  			cptPortalInt++;
	  			if(cptPortalInt%2 ==0){
	  				cptPortal = (cptPortal +1)%animPortal.size();
	  				cptPortalInt = 0;
	  			}
	  		}
	  		if(scoreboard) {
	  			g.setColor(new Color((float)0.8,(float)0.8,(float)0.8,(float)0.5));
	  			
	  			g.fillRect(20, 20, 400, 50*(playerMap.size()) + 20);
	  			
	  			g.setColor(Color.black);
	  			int cpt = 1;
	  			for(Player p : playerMap.values())
		  		{
		  			g.drawString(p.name, 100 ,50*cpt);
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
			synchronized (lock) {
				nbPoussees++;
			}
		}
		
	}
	public void clock() {
		if(p!=null) {
			synchronized (lock) {
				rotations = (rotations - turnit) %360;
			}
			p.dir = (p.dir-turnit)%360;
			System.out.println("dir : " + p.dir);
			
		}
	}
	public void anticlock() {
		if(p!=null) {
			synchronized (lock) {
				rotations = (rotations + turnit) %360;
			}
			p.dir = (p.dir+turnit)%360;
			System.out.println("dir : " + p.dir);
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