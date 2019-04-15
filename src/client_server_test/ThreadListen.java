package client_server_test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadListen extends Thread{
	protected Panneau pan;
	protected String nameP;
	protected String host;
	int playersToRead;
	int scoresToRead;
	boolean ignore = false;
	boolean welcome;
	protected static final int PORT=45678;
	public ThreadListen(Panneau pan, String host, String name) {
		this.pan = pan;
		this.host = host;
		this.playersToRead = 0;
		this.scoresToRead = 0;
		this.nameP = name;
		this.welcome = false;
		pan.setName(name);
	}
	public void run() {
		Socket s=null;
		try { s=new Socket (host,PORT);
			DataInputStream canalLecture = new DataInputStream(s.getInputStream());
			PrintStream canalEcriture = new PrintStream(s.getOutputStream());
			System.out.println("Connexion etablie : "+s.getInetAddress()+" port : "+s.getPort());
			//NAME SIZE <= 12
			canalEcriture.print("CONNECT/"+nameP+"/"); canalEcriture.flush();
			String st;
			String[] st2;
			char[] pl;
			String name;
			String score;
			String tmpx;
			String tmpy, tmpvx, tmpvy;
			double x, vx, vy;
			double y;
			int scoreInt;
			int cpt;
			Player newPlayer;
			while ( true )
			{
				byte[] temp1 = new byte[1024];
				canalLecture.readFully(temp1, 0, 24);
				int dataLength = temp1.length;
				if ( dataLength > 0 )
				{
					st = new String(temp1);
					st2=st.split("/");      
					//System.out.println("RECEIVING : "+st);
					if(st2[0].contains("NEWOBJ")) {
						canalLecture.readFully(temp1, 0, 24);
						st = new String(temp1);
						pl = st.toCharArray();
						cpt = 1;
						tmpx = "";
						tmpy = "";
						while((cpt < pl.length) &&(pl[cpt]!='y'))
						{
							tmpx+=pl[cpt];
							cpt++;
						}
						x=Double.parseDouble(tmpx);
						cpt++;
						while((cpt < pl.length) &&(pl[cpt]!='/'))
						{
							tmpy+=pl[cpt];
							cpt++;
						}
						y=Double.parseDouble(tmpy);
						Objective o = new Objective(x,y);
						pan.setObjective(o);
						
				        //canalEcriture.flush();
					}else {
						if(st2[0].contains("PLANETES")) {
							canalLecture.readFully(temp1, 0, 24);
							st = new String(temp1);
							pl = st.toCharArray();
							cpt = 0;
							tmpx = "";
							tmpy = "";
							while((cpt < pl.length) &&(pl[cpt]!='/'))
							{
								tmpx+=pl[cpt];
								cpt++;
							}
							x=Double.parseDouble(tmpx);
							pan.plx1 = x;
							cpt++;
							while((cpt < pl.length) &&(pl[cpt]!='/'))
							{
								tmpy+=pl[cpt];
								cpt++;
							}
							y=Double.parseDouble(tmpy);
							pan.ply1 = y;
							
							canalLecture.readFully(temp1, 0, 24);
							st = new String(temp1);
							pl = st.toCharArray();
							cpt = 0;
							tmpx = "";
							tmpy = "";
							while((cpt < pl.length) &&(pl[cpt]!='/'))
							{
								tmpx+=pl[cpt];
								cpt++;
							}
							x=Double.parseDouble(tmpx);
							pan.plx2 = x;
							cpt++;
							while((cpt < pl.length) &&(pl[cpt]!='/'))
							{
								tmpy+=pl[cpt];
								cpt++;
							}
							y=Double.parseDouble(tmpy);
							pan.ply2 = y;
					        //canalEcriture.flush();
						}else {
							if(st2[0].contains("COLLV")) {
								cpt = 0;
								vx = 0;
								vy = 0;
								tmpvx =  "";
								tmpvy = "";
								pl = st2[1].toCharArray();
								name = "";
								while((cpt < pl.length) &&(pl[cpt]!='/'))
								{
									tmpvx+=pl[cpt];
									cpt++;
								}
								vx = Double.parseDouble(tmpvx);
								pl = st2[2].toCharArray();
								cpt = 0;
								while((cpt < pl.length) &&(pl[cpt]!='/'))
								{
									tmpvy+=pl[cpt];
									cpt++;
								}
								vy = Double.parseDouble(tmpvy);
						        if(pan.getPlayer() != null) {
					        		pan.getPlayer().vx = vx;
					        		pan.getPlayer().vy = vy;
						        }
							}else {
								if(st2[0].contains("COLL")) {
									cpt = 0;
									pl = st2[1].toCharArray();
									name = "";
									while((cpt < pl.length) &&(pl[cpt]!='/'))
									{
										name+=pl[cpt];
										cpt++;
									}
									System.out.println("COLLISION : " + name);
							        if(pan.getPlayer() != null) {
							        	if((!Objects.equals(name, pan.getPlayer().name)) && pan.playerMap.containsKey(name)) {
							        		vx = pan.getPlayer().vx;
							        		vy = pan.getPlayer().vy;
							        		pan.getPlayer().vx = pan.playerMap.get(name).vx;
							        		pan.getPlayer().vy = pan.playerMap.get(name).vy;
							        		pan.playerMap.get(name).vx = vx;
							        		pan.playerMap.get(name).vy = vy;
							        		
							        	}
							        }
									pan.getPlayer().vx = -pan.getPlayer().vx;
									pan.getPlayer().vy = -pan.getPlayer().vy;
								}else {
									if(playersToRead > 0)
									{
										pl = st.toCharArray();
										cpt = 0;
										name = "";
										tmpx = "";
										tmpy = "";
										while((cpt < pl.length) &&(pl[cpt]!='X'))
										{
											name+=pl[cpt];
											cpt++;
										}
										cpt++;
										while((cpt < pl.length) &&(pl[cpt]!='Y'))
										{
											tmpx+=pl[cpt];
											cpt++;
										}
										x=Double.parseDouble(tmpx);
										cpt++;
										while((cpt < pl.length) &&(pl[cpt]!='|'))
										{
											tmpy+=pl[cpt];
											cpt++;
										}
										y=Double.parseDouble(tmpy);
										
										newPlayer = new Player(name,x,y,0,0,0,0);
										if(welcome) {
											if(Objects.equals(name, this.nameP)) {
												pan.setPlayer(newPlayer);
												welcome = false;
											}else {
												pan.addMap(name, x, y);
											}
										}else {
											if(!Objects.equals(name, this.nameP) ) {
												pan.addMap(name, x, y);
											}
											else {
												//if(!ignore) {
													pan.getPlayer().x = x;
													pan.getPlayer().y = y;
													pan.addMap(name, x, y);
													
												/*}
												else {
													ignore = false;
												}*/
												
											}
										}
										playersToRead --;
									}else {
										if(scoresToRead > 0 ) {
											pl = st.toCharArray();
											cpt = 0;
											name = "";
											tmpvx = "";
											tmpvy = "";
											score = "";
											while((cpt < pl.length) &&(pl[cpt]!=':'))
											{
												name+=pl[cpt];
												cpt++;
											}
											cpt++;
											while((cpt < pl.length) &&(pl[cpt]!='|'))
											{
												score+=pl[cpt];
												cpt++;
											}
											cpt++;
											while((cpt < pl.length) &&(pl[cpt]!='|'))
											{
												tmpvx+=pl[cpt];
												cpt++;
											}
											cpt++;
											while((cpt < pl.length) &&(pl[cpt]!='|'))
											{
												tmpvy+=pl[cpt];
												cpt++;
											}
											vx=Double.parseDouble(tmpvx);
											vy=Double.parseDouble(tmpvy);
									        scoreInt = 0;
									        for (int i=0; i < score.length(); i++) {
									            char c = score.charAt(i);
									            if (c < '0' || c > '9') continue;
									            scoreInt = scoreInt * 10 + c - '0';
									        }
											if(name != this.nameP) {
												pan.updateScore(name, scoreInt, vx, vy);
											}
											scoresToRead --;
										}
									}
								}
							}
						}
					}
					
					if(st2[0].contains("WELCOME")) {
						if(st2[1].contains("attente")) {
							pan.setAttente(true);
						}
						else {
							pan.setAttente(false);
						}
						Pattern regex = Pattern.compile("\\D*(\\d*)");
				        Matcher matcher = regex.matcher(st2[2]);
				        if (matcher.matches() && matcher.groupCount() == 1) {
				            String digitStr = matcher.group(1);
				            Integer digit = Integer.parseInt(digitStr);
				            playersToRead = digit; 
				              
				        }
				        welcome = true;
					}
					if(st2[0].contains("NEW PLAYER")) {
						System.out.println("NEW PLAYER : "+st2[1]);
					}
					if(st2[0].contains("DENIED")) {
						System.out.println("CONNEXION DENIED");
					}
					
					if(st2[0].contains("TICK")) {
						Pattern regex = Pattern.compile("\\D*(\\d*)"); 
				        Matcher matcher = regex.matcher(st2[1]);
				        if (matcher.matches() && matcher.groupCount() == 1) {
				            String digitStr = matcher.group(1);
				            Integer digit = Integer.parseInt(digitStr);
				            playersToRead = digit;  
				            scoresToRead = digit;
				        }
				        synchronized (pan.lock) {
				        	canalEcriture.print("TICK/"+pan.getPosX()/20.0+"/"+pan.getPosY()/20.0+"/"+pan.getScore()+"/"+pan.getPlayer().vx+"/"+pan.getPlayer().vy+"/"+"COMMS/A"+pan.rotations+"T"+pan.nbPoussees+"/");
					        pan.rotations = 0;
					        pan.nbPoussees = 0;
				        }

					}
					if(st2[0].contains("TEL")) {//teleporter extension
						if(st2[1].contains("N")) {
							pan.isTeleporter = false;
						}
						else {
							canalLecture.readFully(temp1, 0, 24);
							st = new String(temp1);
							pl = st.toCharArray();
							cpt = 0;
							tmpx = "";
							tmpy = "";
							while((cpt < pl.length) &&(pl[cpt]!='/'))
							{
								tmpx+=pl[cpt];
								cpt++;
							}
							x=Double.parseDouble(tmpx);
							pan.teleportx1 = x*20;
							cpt++;
							while((cpt < pl.length) &&(pl[cpt]!='/'))
							{
								tmpy+=pl[cpt];
								cpt++;
							}
							y=Double.parseDouble(tmpy);
							pan.teleporty1 = y*20;
							//System.out.println("teleporter 1 : " + pan.teleportx1 + " / " + pan.teleporty1);
							
							canalLecture.readFully(temp1, 0, 24);
							st = new String(temp1);
							pl = st.toCharArray();
							cpt = 0;
							tmpx = "";
							tmpy = "";
							while((cpt < pl.length) &&(pl[cpt]!='/'))
							{
								tmpx+=pl[cpt];
								cpt++;
							}
							x=Double.parseDouble(tmpx);
							pan.teleportx2 = x*20;
							cpt++;
							while((cpt < pl.length) &&(pl[cpt]!='/'))
							{
								tmpy+=pl[cpt];
								cpt++;
							}
							y=Double.parseDouble(tmpy);
							pan.teleporty2 = y*20;
							
							pan.isTeleporter = true;
						}
					}
					if(st2[0].contains("WINNER")) {
						cpt = 0;
						pl = st2[1].toCharArray();
						name = "";
						while((cpt < pl.length) &&(pl[cpt]!='/'))
						{
							name+=pl[cpt];
							cpt++;
						}
						
						
						
						System.out.println("WINNER WINNER : " + name);
						pan.setWinner(name);
						pan.getPlayer().score = 0;
						pan.getPlayer().vx = 0;
						pan.getPlayer().vy = 0;
						pan.setAttente(true);
					}
					if(st2[0].contains("CRASHED")) {
						pan.crashed = true;

					}
					if(st2[0].contains("REPAIRED")) {
						pan.justUnstuck = true;
						pan.crashed = false;

					}
					if(st2[0].contains("PLAYERLEFT")) {
						if(pan.playerMap.containsKey(st2[1]))
							pan.playerMap.remove(st2[1]);

					}
					if(st2[0].contains("SESSION")) {
						pan.setAttente(false);
					}
					/*if(st2[0].contains("IGNORE")) {
						this.ignore = true;
					}*/
					
				}
			  
			}
			  
		}
		catch (IOException e) {System.err.println(e);}
		finally { try {if (s != null) s.close();} catch (IOException e2) {}
		}
		yield();
		/*try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
