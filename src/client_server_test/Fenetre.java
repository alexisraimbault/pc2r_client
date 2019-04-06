package client_server_test;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class Fenetre extends JFrame {
	public String name = "Al";
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java Client <hote>");
			System.exit(1); }
		
		new Fenetre(args[0]);
	}

  private Panneau pan;;

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
        } 

        @Override
        public void keyReleased(KeyEvent ke) {
            //System.out.println("released"+ke.getKeyCode());
        }
    });
    ThreadListen tl = new ThreadListen(pan, host, name);
	//tl.setPriority(10);
	tl.start();
    go();
  }
  

  private void go() {
	double d1,d2;
	double force, angle;
	
    while (true) {
    	pan.stepPlayers();
    	if(pan.getPlayer() != null) {
	      pan.getPlayer().x = ((pan.getPlayer().x + pan.getPlayer().vx)+1000)%1000;
	      pan.getPlayer().y = ((pan.getPlayer().y + pan.getPlayer().vy)+1000)%1000;
	      d1 = Math.sqrt((pan.plx1*20 - pan.getPlayer().x)*(pan.plx1*20 - pan.getPlayer().x) + (pan.ply1*20 - pan.getPlayer().y)*(pan.ply1*20 - pan.getPlayer().y));
	      d2 = Math.sqrt((pan.plx2*20 - pan.getPlayer().x)*(pan.plx2*20 - pan.getPlayer().x) + (pan.ply2*20 - pan.getPlayer().y)*(pan.ply2*20 - pan.getPlayer().y));
	      if(d1 < 300) {
	    	force = 150/(d1*d1);
	    	angle = Math.atan2(Math.abs(pan.plx1*20-pan.getPlayer().x), Math.abs(pan.ply1*20-pan.getPlayer().y));
	    	 
	    	pan.getPlayer().vx += force * Math.cos(angle); 
	    	pan.getPlayer().vy += force * Math.sin(angle);
	    	  
	      }
	      if(d2 < 300) {
	    	force = 300/(d2*d2);
	    	angle = Math.atan2(Math.abs(pan.plx2*20-pan.getPlayer().x), Math.abs(pan.ply2*20-pan.getPlayer().y));
	    	 
	    	pan.getPlayer().vx += force * Math.cos(angle); 
	    	pan.getPlayer().vy += force * Math.sin(angle);
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