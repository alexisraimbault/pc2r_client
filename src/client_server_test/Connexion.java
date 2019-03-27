package client_server_test;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

class Connexion extends Thread {
	protected Socket client; protected DataInputStream in; protected PrintStream out;
	public Connexion(Socket client_soc){
	client=client_soc;
	try {
		in=new DataInputStream(client.getInputStream());
		out=new PrintStream(client.getOutputStream()); }
	catch (IOException e){
		try {client.close();} catch (IOException e1){}
		System.err.println(e.getMessage());
		return;}
	this.start();
	}
	public void run(){
		try {
			while (true) {
				String ligne=in.readLine();
				if (ligne.toUpperCase().compareTo("FIN")==0) break;
					System.out.println(ligne.toUpperCase());
					out.println(ligne.toUpperCase()); out.flush();
			}}
		catch (IOException e) {System.out.println("connexion : "+e.toString());}
		finally {try {client.close();} catch (IOException e) {}}
	}
}
