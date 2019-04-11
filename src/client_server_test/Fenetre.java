package client_server_test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

import javax.swing.JFrame;

public class Fenetre extends JFrame {
	public String name = "clou";
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java Client <hote>");
			System.exit(1); }
		
		new Fenetre(args[0]);
	}

  private Panneau pan;

  public Fenetre(String host) {
	pan = new Panneau();
    this.setTitle("Animation");
    this.setSize(1000, 1000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setContentPane(pan);
    this.setVisible(true);
    this.addKeyListener(new KeyListener(){
    	 
        @Override
        public void keyTyped(KeyEvent ke) {
            //System.out.println("typed"+ke.getKeyCode());
        }
        
        @Override
        public void keyPressed(KeyEvent ke) {
            System.out.println("pressed"+ke.getKeyCode());
            if(ke.getKeyCode() == 32) {
            	pan.thrust();
            }
            if(ke.getKeyCode() == 37) {
            	pan.clock();
            }
            if(ke.getKeyCode() == 39) {
            	pan.anticlock();
            }
            if(ke.getKeyCode() == 83) {
            	pan.setScores(true);
            }
        } 

        @Override
        public void keyReleased(KeyEvent ke) {
            //System.out.println("released"+ke.getKeyCode());
            if(ke.getKeyCode() == 83) {
            	pan.setScores(false);
            }
        }
    });
    ThreadListen tl = new ThreadListen(pan, host, name);
	//tl.setPriority(10);
	tl.start();
    go();
  }
  

  private void go() {
	double d1,d2,d, tmpx, tmpy;
	double force, angle;
	
    while (true) {
    	pan.stepPlayers();
    	if(pan.getPlayer() != null) {
	      pan.getPlayer().x = ((pan.getPlayer().x + pan.getPlayer().vx)+1000)%1000;
	      pan.getPlayer().y = ((pan.getPlayer().y + pan.getPlayer().vy)+1000)%1000;
	      d1 = Math.sqrt((pan.plx1*20 - pan.getPlayer().x)*(pan.plx1*20 - pan.getPlayer().x) + (pan.ply1*20 - pan.getPlayer().y)*(pan.ply1*20 - pan.getPlayer().y));
	      d2 = Math.sqrt((pan.plx2*20 - pan.getPlayer().x)*(pan.plx2*20 - pan.getPlayer().x) + (pan.ply2*20 - pan.getPlayer().y)*(pan.ply2*20 - pan.getPlayer().y));
	      if(d1 < 250) {
	    	force = 150/(d1*d1);
	    	angle = Math.atan2(Math.abs(pan.plx1*20-pan.getPlayer().x), Math.abs(pan.ply1*20-pan.getPlayer().y));
	    	 
	    	pan.getPlayer().vx += force * Math.cos(angle); 
	    	pan.getPlayer().vy += force * Math.sin(angle);
	    	  
	      }
	      if(d2 < 250) {
	    	force = 300/(d2*d2);
	    	angle = Math.atan2(Math.abs(pan.plx2*20-pan.getPlayer().x), Math.abs(pan.ply2*20-pan.getPlayer().y));
	    	 
	    	pan.getPlayer().vx += force * Math.cos(angle); 
	    	pan.getPlayer().vy += force * Math.sin(angle);
	      }
	      for(Player pl : pan.playerMap.values()) {
	    	  if(!Objects.equals(pl.name,pan.getPlayer().name)) {
		    	  d = Math.sqrt((pl.x - pan.getPlayer().x)*(pl.x - pan.getPlayer().x) + (pl.y - pan.getPlayer().y)*(pl.y - pan.getPlayer().y));
		    	  if(d<25 /*&& (Math.sqrt((pl.x - ((pan.getPlayer().x + pan.getPlayer().vx)+1000)%1000)*(pl.x - ((pan.getPlayer().x + pan.getPlayer().vx)+1000)%1000) + (pl.y - ((pan.getPlayer().y + pan.getPlayer().vy)+1000)%1000)*(pl.y -((pan.getPlayer().y + pan.getPlayer().vy)+1000)%1000)))<d*/) {
		    		  /*double v1 =  Math.sqrt(pan.getPlayer().vx* pan.getPlayer().vx + (pan.getPlayer().vy* pan.getPlayer().vy));
		    		  double v2 = Math.sqrt(pl.vx* pl.vx + (pl.vy* pl.vy));
		    		  if(v1 >= v2) {
		    			  pl.vx += pan.getPlayer().vx;
		    			  pl.vy += pan.getPlayer().vy;
		    			  pan.getPlayer().vx = 0;
		    			  pan.getPlayer().vy = 0;
		    		  }
		    		  else {
		    			  pan.getPlayer().vx += pl.vx;
		    			  pan.getPlayer().vy += pl.vy;
		    			  pl.vx = 0;
		    			  pl.vy = 0;
		    		  }*/
		    		  /*tmpx=pan.getPlayer().vx;
		    		  tmpy=pan.getPlayer().vy;
		    		  pan.getPlayer().vx = pl.vx;
		    		  pan.getPlayer().vy = pl.vy;
		    		  pl.vx = tmpx;
		    		  pl.vy = tmpy;*/
		    		  //System.out.println("collision");
		    		  //pan.getPlayer().vx = -pan.getPlayer().vx;
		    		  //pan.getPlayer().vy = -pan.getPlayer().vy;
		    	  }
	    	  }
	      }
	      pan.repaint();
    	}
      try {
        Thread.sleep(30);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}