package client_server_test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadListen extends Thread
{
	protected Panneau pan;
	protected String nameP;
	protected String host;
	int playersToRead;
	int scoresToRead;
	boolean welcome;
	
	protected static final int PORT=45678;
	
	public ThreadListen(Panneau pan, String host, String name)
	{
		this.pan = pan;
		this.host = host;
		this.playersToRead = 0;
		this.scoresToRead = 0;
		this.nameP = name;
		this.welcome = false;
		pan.setName(name);
	}
	
	public String parse(String buffer, int pos, char sep)
	{
		StringBuilder builder = new StringBuilder();
		while((pos < buffer.length()) && (buffer.charAt(pos) != sep))
		{
			builder.append(buffer.charAt(pos));
			++pos;
		}
		return builder.toString();
	}
	
	public void run()
	{
		Socket s = null;
		try
		{
			s = new Socket (host,PORT);
			DataInputStream istream = new DataInputStream(s.getInputStream());
			PrintStream canalEcriture = new PrintStream(s.getOutputStream());
			System.out.println("Connexion etablie : "+s.getInetAddress()+" port : "+s.getPort());
			//NAME SIZE <= 12
			canalEcriture.print("CONNECT/"+nameP+"/"); canalEcriture.flush();
			
			ObjectiveBuilder obuilder = new ObjectiveBuilder();
			PlanetBuilder pntbuilder = new PlanetBuilder();
			Vector2Builder vbuilder = new Vector2Builder();
			
			while (true)
			{
				byte[] buffer = new byte[1024];
				istream.readFully(buffer, 0, 24);
				
				String sbuffer = new String(buffer);
				
				if (sbuffer.length() > 0)
				{
					String[] sections = sbuffer.split("/");
					
					if(sections[0].contains("OBJECTIVE"))
					{
						istream.readFully(buffer, 0, 24);
						sbuffer = new String(buffer);
						
						String tmpx = parse(sbuffer, 1, 'y');
						obuilder.set_x(Double.parseDouble(tmpx) * 20);
						
						String tmpy = parse(sbuffer, tmpx.length() + 1, '/');
						obuilder.set_y(Double.parseDouble(tmpy) * 20);
						
						pan.setObjective(obuilder.create());
					}
					else if(sections[0].contains("PLANETES"))
					{
						{
							istream.readFully(buffer, 0, 24);
							sbuffer = new String(buffer);
							
							String tmpx = parse(sbuffer, 0, '/');
							pntbuilder.set_x(Double.parseDouble(tmpx));
							
							String tmpy = parse(sbuffer, tmpx.length() + 1, '/');
							pntbuilder.set_y(Double.parseDouble(tmpy));
							
							pan.pl1 = pntbuilder.create();
						}
						{
							istream.readFully(buffer, 0, 24);
							sbuffer = new String(buffer);
							
							String tmpx = parse(sbuffer, 0, '/');
							pntbuilder.set_x(Double.parseDouble(tmpx));
							
							String tmpy = parse(sbuffer, tmpx.length() + 1, '/');
							pntbuilder.set_y(Double.parseDouble(tmpy));
							
							pan.pl2 = pntbuilder.create();
						}
						
					}
					else if(sections[0].contains("COLLV"))
					{
						String tmpx = parse(sections[1], 0, '/');
						vbuilder.set_x(Double.parseDouble(tmpx));
						
						String tmpy = parse(sections[2], 0, '/');
						vbuilder.set_y(Double.parseDouble(tmpy));
						
						Vector2 v = vbuilder.create();
				        if(pan.getPlayer() != null)
				        {
			        		pan.getPlayer().vx = v.x;
			        		pan.getPlayer().vy = v.y;
				        }
					}
					else if(sections[0].contains("COLL"))
					{
						String name = parse(sections[1], 0, '/');
						
						System.out.println("COLLISION : " + name);
						
				        if(pan.getPlayer() != null) 
				        {
				        	if((!Objects.equals(name, pan.getPlayer().name)) && pan.playerMap.containsKey(name))
				        	{
				        		double vx = pan.getPlayer().vx;
				        		double vy = pan.getPlayer().vy;
				        		pan.getPlayer().vx = pan.playerMap.get(name).vx;
				        		pan.getPlayer().vy = pan.playerMap.get(name).vy;
				        		pan.playerMap.get(name).vx = vx;
				        		pan.playerMap.get(name).vy = vy;
				        	}
				        }
						pan.getPlayer().vx = -pan.getPlayer().vx;
						pan.getPlayer().vy = -pan.getPlayer().vy;
					}
					else if(playersToRead > 0)
					{
						String name = parse(sbuffer, 0, 'X');
						
						String tmpx = parse(sbuffer, name.length() + 1, 'Y');
						vbuilder.set_x(Double.parseDouble(tmpx) * 20);
						
						String tmpy = parse(sbuffer, name.length() + 1 + tmpx.length() + 1, '|');
						vbuilder.set_y(Double.parseDouble(tmpy) * 20);
						
						Vector2 pos = vbuilder.create();
						
						if(!Objects.equals(name, this.nameP))
						{
							pan.addMap(name, pos.x, pos.y);
						}
						else if(welcome)
						{
							pan.setPlayer(new Player(name, pos.x, pos.y, 0, 0, 0, 0));
							welcome = false;
						}
						
						--playersToRead;
					}
					else if(scoresToRead > 0)
					{
						String name = parse(sbuffer, 0, ':');
						
						String scorestr = parse(sbuffer, name.length() + 1, '|');
						// plus simple ?
						int score = 0;
				        for (int i=0; i < scorestr.length(); i++) {
				            char c = scorestr.charAt(i);
				            if (c < '0' || c > '9') continue;
				            score = score * 10 + c - '0';
				        }
						
						String tmpx = parse(sbuffer, name.length() + 1 + scorestr.length() + 1, '|');
						vbuilder.set_x(Double.parseDouble(tmpx));
						
						String tmpy = parse(sbuffer, name.length() + 1 + scorestr.length() + 1 + tmpx.length() + 1, '|');
						vbuilder.set_y(Double.parseDouble(tmpy));
						
						Vector2 v = vbuilder.create();
						
						if(!Objects.equals(name, this.nameP))
							pan.updateScore(name, score, v.x, v.y);
						--scoresToRead;
					} 
					else if(sections[0].contains("WELCOME"))
					{
						if(sections[1].contains("attente")) {
							pan.setAttente(true);
						}
						else {
							pan.setAttente(false);
						}
						Pattern regex = Pattern.compile("\\D*(\\d*)");
				        Matcher matcher = regex.matcher(sections[2]);
				        if (matcher.matches() && matcher.groupCount() == 1) {
				            String digitStr = matcher.group(1);
				            Integer digit = Integer.parseInt(digitStr);
				            playersToRead = digit; 
				              
				        }
				        welcome = true;
					}
					else if(sections[0].contains("ERR"))
					{
						System.out.println("USERNAME ALREADY TAKEN");
					}
					else if(sections[0].contains("UPDATE"))
					{
						Pattern regex = Pattern.compile("\\D*(\\d*)"); 
				        Matcher matcher = regex.matcher(sections[1]);
				        if (matcher.matches() && matcher.groupCount() == 1) {
				            String digitStr = matcher.group(1);
				            Integer digit = Integer.parseInt(digitStr);
				            playersToRead = digit;  
				            scoresToRead = digit;
				        }
				        
			        	canalEcriture.print(pan.getPosX()/20.0+"/"+pan.getPosY()/20.0+"/"+pan.getScore()+"/"+pan.getPlayer().vx+"/"+pan.getPlayer().vy+"/");
				        //System.out.println("SENDING : UPDATE/x"+pan.getPosX()/20.0+"y"+pan.getPosY()/20.0+"/"+pan.getScore()+"/"+pan.getPlayer().vx+"/"+pan.getPlayer().vy+"/");

					}
					else if(sections[0].contains("TEL"))
					{
						if(sections[1].contains("N"))
						{
							pan.isTeleporter = false;
						}
						else
						{
							{
								istream.readFully(buffer, 0, 24);
								sbuffer = new String(buffer);

								String tmpx = parse(sbuffer, 0, '/');
								vbuilder.set_x(Double.parseDouble(tmpx));
								
								String tmpy = parse(sbuffer, tmpx.length() + 1, '/');
								vbuilder.set_y(Double.parseDouble(tmpy));
								
								Vector2 pos = vbuilder.create();
								
								pan.teleportx1 = pos.x;
								pan.teleporty1 = pos.y;
							}
							
							{
								istream.readFully(buffer, 0, 24);
								sbuffer = new String(buffer);

								String tmpx = parse(sbuffer, 0, '/');
								vbuilder.set_x(Double.parseDouble(tmpx));
								
								String tmpy = parse(sbuffer, tmpx.length() + 1, '/');
								vbuilder.set_y(Double.parseDouble(tmpy));
								
								Vector2 pos = vbuilder.create();
								
								pan.teleportx2 = pos.x;
								pan.teleporty2 = pos.y;
							}
							pan.isTeleporter = true;
						}
					}
					if(sections[0].contains("WIN"))
					{
						String name = parse(sections[1], 0, '/');
						
						System.out.println("WINNER WINNER : " + name);
						pan.setWinner(name);
						pan.getPlayer().score = 0;
						pan.getPlayer().vx = 0;
						pan.getPlayer().vy = 0;
						pan.setAttente(true);
					}
					if(sections[0].contains("START"))
					{
						pan.setAttente(false);
					}
					
				}
			  
			}
		}
		catch (IOException e) {System.err.println(e);}
		finally
		{
			try { if (s != null) s.close(); }
			catch (IOException e2) {}
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

