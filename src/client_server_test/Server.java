package client_server_test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
protected static final int PORT =45678;
protected ServerSocket ecoute;
Server () {
	try {ecoute = new ServerSocket(PORT);}
	catch (IOException e){
	System.err.println(e.getMessage());
	System.exit(1);
}
System.out.println("Serveur en ecoute sur le port : "+PORT);
this.start();
}
public void run (){
	try {
		while (true){
			Socket client=ecoute.accept();
			Connexion c = new Connexion (client);
	}}
	catch (IOException e){System.err.println(e.getMessage());System.exit(1);}
}
public static void main (String[] args){new Server();}
}