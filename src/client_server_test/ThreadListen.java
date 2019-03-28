package client_server_test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadListen extends Thread{
	protected Panneau pan;
	protected String host;
	int playersToRead;
	protected static final int PORT=45678;
	public ThreadListen(Panneau pan, String host) {
		this.pan = pan;
		this.host = host;
		this.playersToRead = 0;
	}
	public void run() {
		Socket s=null;
		try { s=new Socket (host,PORT);
			DataInputStream canalLecture = new DataInputStream(s.getInputStream());
			DataInputStream console = new DataInputStream (s.getInputStream());
			PrintStream canalEcriture = new PrintStream(s.getOutputStream());
			System.out.println("Connexion etablie : "+
			s.getInetAddress()+" port : "+s.getPort());
			//NAME SIZE <= 12
			canalEcriture.print("CONNECT/alexis/"); canalEcriture.flush();
			String st;
			String[] st2;
			char[] pl;
			String name;
			String tmpx;
			String tmpy;
			double x;
			double y;
			int cpt;
			while ( true )
			{
				byte[] temp1 = new byte[1024];
				canalLecture.readFully(temp1, 0, 24);
				int dataLength = temp1.length;
				if ( dataLength > 0 )
				{
					st = new String(temp1);
					System.out.println(st);
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
						System.out.println("NEW PLAYER : " + name + " - " + x + " - " + y);//TODO actually add the new plaer in the panel
						playersToRead --;
					}
					st2=st.split("/");          
					if(st2[0].contains("WELCOME")) {
						Pattern regex = Pattern.compile("\\D*(\\d*)");
						System.out.println("2nd param :"+ st2[2]);   
				        Matcher matcher = regex.matcher(st2[2]);
				        if (matcher.matches() && matcher.groupCount() == 1) {
				            String digitStr = matcher.group(1);
				            Integer digit = Integer.parseInt(digitStr);
				            playersToRead = digit;          
				        }
					}
					
				}
			  
			}
			  
		}
		catch (IOException e) {System.err.println(e);}
		finally { try {if (s != null) s.close();} catch (IOException e2) {}
		}
		yield();
	}
}
