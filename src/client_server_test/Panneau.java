package client_server_test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

public class Panneau extends JPanel {
	protected List<Player> players = new ArrayList<Player>();
	protected HashMap<String,Player> playerMap = new HashMap<String, Player>();
	private int posX = -50;
	private int posY = -50;
	public void paintComponent(Graphics g) {
		// On d�cide d'une couleur de fond pour notre rectangle
		g.setColor(Color.white);
		// On dessine celui-ci afin qu'il prenne tout la surface
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
  		// On red�finit une couleur pour notre rond
  		g.setColor(Color.red);
  		// On le dessine aux coordonn�es souhait�es
  		g.fillOval(posX, posY, 50, 50);
	}
	
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
	    this.posX = posX;
	}
	public int getPosY() {
	    return posY;
	}
	public void setPosY(int posY) {
	    this.posY = posY;
	}
}