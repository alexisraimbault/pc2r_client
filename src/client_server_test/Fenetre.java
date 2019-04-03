package client_server_test;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Fenetre extends JFrame {
	public String name = "clouc";
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: java Client <hote>");
			System.exit(1); }
		
		new Fenetre(args[0]);
	}

  private Panneau pan = new Panneau();

  public Fenetre(String host) {
    this.setTitle("Animation");
    this.setSize(1000, 1000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setContentPane(pan);
    this.setVisible(true);
    this.addKeyListener(new KeyListener(){
    	 
        @Override
        public void keyTyped(KeyEvent ke) {
            System.out.println("typed"+ke.getKeyCode());
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
            /*if(ke.getKeyCode() == 83) {
            	
            	pan.p.score ++;
            }*/
        } 

        @Override
        public void keyReleased(KeyEvent ke) {
            System.out.println("released"+ke.getKeyCode());
        }
    });
    ThreadListen tl = new ThreadListen(pan, host, name);
	tl.setPriority(10);
	tl.start();
    go();
  }
  

  private void go() {
    while (true) {
    	if(pan.getPlayer() != null) {
	      pan.getPlayer().x = ((pan.getPlayer().x + pan.getPlayer().vx)+1000)%1000;
	      pan.getPlayer().y = ((pan.getPlayer().y + pan.getPlayer().vy)+1000)%1000;
	      if(pan.obj != null) {
		      double distance = Math.sqrt((pan.obj.x-pan.getPlayer().x)*(pan.obj.x-pan.getPlayer().x) + (pan.obj.y-pan.getPlayer().y)*(pan.obj.y-pan.getPlayer().y));
		      if (distance<25) {
		    	  pan.p.score ++;
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