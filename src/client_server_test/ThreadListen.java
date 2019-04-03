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
			System.out.println("Connexion etablie : "+
			s.getInetAddress()+" port : "+s.getPort());
			//NAME SIZE <= 12
			canalEcriture.print("CONNECT/"+nameP+"/"); canalEcriture.flush();
			String st;
			String[] st2;
			char[] pl;
			String name;
			String score;
			String tmpx;
			String tmpy;
			double x;
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
					System.out.println(st);
					if(st2[0].contains("OBJECTIVE")) {
						canalLecture.readFully(temp1, 0, 24);
						st = new String(temp1);
						System.out.println(st);
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
						Objective o = new Objective(x*20,y*20);
						pan.setObjective(o);
						System.out.println("NEW OBJECTiVE :" + x + " - " + y);
						
				        //canalEcriture.flush();
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
							
							newPlayer = new Player(name,x*20,y*20,0,0,2,0);
							if(welcome) {
								System.out.println("WELCOMING : name : "+ name + " myname : "+ this.nameP);
								if(Objects.equals(name, this.nameP)) {
									System.out.println(" 1st UPDATE");
									pan.setPlayer(newPlayer);
									welcome = false;
								}else {
									System.out.println("NEW PLAYER : " + name + " - " + x + " - " + y);
									pan.addMap(name, x*20, y*20);
								}
							}else {
								if(name != this.nameP) {
									System.out.println("NEW PLAYER : " + name + " - " + x + " - " + y);
									pan.addMap(name, x*20, y*20);
								}
							}
							playersToRead --;
						}else {
							
							if(scoresToRead > 0 ) {
								pl = st.toCharArray();
								cpt = 0;
								name = "";
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
								/*Pattern regex = Pattern.compile("\\D*(\\d*)");
						        Matcher matcher = regex.matcher(score);
						        if (matcher.matches() && matcher.groupCount() == 1) {
						            String digitStr = matcher.group(1);
						            Integer digit = Integer.parseInt(digitStr);
						            scoreInt = digit;   
						        }*/
						        scoreInt = 0;
						        for (int i=0; i < score.length(); i++) {
						            char c = score.charAt(i);
						            if (c < '0' || c > '9') continue;
						            scoreInt = scoreInt * 10 + c - '0';
						        }
						        System.out.println("SCOR : " + name + " : " + scoreInt);
								if(name != this.nameP) {
									pan.updateScore(name, scoreInt);
								}
								scoresToRead --;
							}
						}
					}
					    
					if(st2[0].contains("WELCOME")) {
						Pattern regex = Pattern.compile("\\D*(\\d*)");
				        Matcher matcher = regex.matcher(st2[2]);
				        if (matcher.matches() && matcher.groupCount() == 1) {
				            String digitStr = matcher.group(1);
				            Integer digit = Integer.parseInt(digitStr);
				            playersToRead = digit; 
				              
				        }
				        welcome = true;
					}
					if(st2[0].contains("UPDATE")) {
						Pattern regex = Pattern.compile("\\D*(\\d*)"); 
				        Matcher matcher = regex.matcher(st2[1]);
				        if (matcher.matches() && matcher.groupCount() == 1) {
				            String digitStr = matcher.group(1);
				            Integer digit = Integer.parseInt(digitStr);
				            playersToRead = digit;  
				            scoresToRead = digit;
				        }
				        /*if(pan.playerMap.containsKey(this.nameP) ) {
				        	canalEcriture.print(pan.getPosX()/20.0+"/"+pan.getPosY()/20.0+"/"+pan.playerMap.get(this.nameP).score+"/");
					        System.out.println("sending UPDATE/x"+pan.getPosX()/20.0+"y"+pan.getPosY()/20.0+"/"+pan.playerMap.get(this.nameP).score+"/");
				        }*/
			        	canalEcriture.print(pan.getPosX()/20.0+"/"+pan.getPosY()/20.0+"/"+pan.getScore()+"/");
				        System.out.println("sending UPDATE/x"+pan.getPosX()/20.0+"y"+pan.getPosY()/20.0+"/"+pan.getScore()+"/");
			       
				        //canalEcriture.flush();
					}
					
					
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
