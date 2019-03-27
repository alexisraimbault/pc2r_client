package client_server_test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
	protected static final int PORT=45678;
	public static void main(String[] args) { Socket s=null;
	if (args.length != 1) {
		System.err.println("Usage: java Client <hote>");
		System.exit(1); }
	try { s=new Socket (args[0],PORT);
		DataInputStream canalLecture = new DataInputStream(s.getInputStream());
		DataInputStream console = new DataInputStream (s.getInputStream());
		PrintStream canalEcriture = new PrintStream(s.getOutputStream());
		System.out.println("Connexion etablie : "+
		s.getInetAddress()+" port : "+s.getPort());
		/*String ligne = new String();
		char c;
		while (true) { System.out.print("?"); System.out.flush();
		do {
			ligne = "";
			while ((c=(char)System.in.read()) != '\n') {ligne=ligne+c;}
		}while(ligne=="");
			canalEcriture.println(ligne); canalEcriture.flush();
			ligne=canalLecture.readLine();
			if (ligne == null) {System.out.println("Connexion terminee"); break;}
				System.out.println("!"+ligne);
			}*/
		canalEcriture.println("voiciuntest\n"); canalEcriture.flush();
		while ( true )
		{
			byte[] temp1 = new byte[124];
			canalLecture.readFully(temp1, 0, 12);
			int dataLength = temp1.length;
			if ( dataLength > 0 )
			{
				String st = new String(temp1);
				System.out.println(st);
			}
		  
		}
		  
	}
	catch (IOException e) {System.err.println(e);}
	finally { try {if (s != null) s.close();} catch (IOException e2) {}
	}
	}
}