package services;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class Test extends JFrame {
	
	public static void main(String[] args) {
		new Test(50,30);
	}

  private Board board;

  public Test(int x, int y) {
    this.setTitle("Animation");
    this.setSize(50*20, 30*20);
    this.board = new Board(x,y);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setContentPane(board);
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
            	//space
            }
            if(ke.getKeyCode() == 37) {
            	//left
            }
            if(ke.getKeyCode() == 39) {
            	//right
            }
        }

        @Override
        public void keyReleased(KeyEvent ke) {
            System.out.println("released"+ke.getKeyCode());
        }
    });
    go();
  }
  

  private void go() {
    while (true) {
    	board.repaint();
      try {
        Thread.sleep(30);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}