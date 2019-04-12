package pc2r_client_piece;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import pc2r_client_piece.model.Parser;
import pc2r_client_piece.model.interfaces.IMovingEntity;

public class TestConsole
{
	public static void main(String[] args)
	{
		Parser parser = new Parser();
		if(args.length != 2)
		{
			System.out.println("Usage : java TestConsole <file> <user>");
			
			return;
		}
		try
		{
			String user = args[1];
			parser.run(new FileInputStream(new File(args[0])), System.out, user);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println(parser.game);
	}
}
