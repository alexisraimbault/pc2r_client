package client_server_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client1 {
	public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println(
                "Usage: java EchoClient <host name> ");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = 45678;
        Socket firstSocket = new Socket(hostName, portNumber);
        PrintWriter out = new PrintWriter(firstSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(firstSocket.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String ligne = new String();
		char c;
		while (true) { System.out.print("?"); System.out.flush();
			
			do {
				ligne = "";
				while ((c=(char)System.in.read()) != '\n') {ligne=ligne+c;}
			}while(ligne=="");
			out.println(ligne); out.flush();
			ligne=in.readLine();
			if (ligne == null) {System.out.println("Connexion terminee"); break;}
				System.out.println("!"+ligne);
			}
        in.close();
        stdIn.close();
        firstSocket.close();

    }
}
