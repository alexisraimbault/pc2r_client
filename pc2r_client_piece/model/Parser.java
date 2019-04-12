package pc2r_client_piece.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import pc2r_client_piece.model.interfaces.IMovingEntity;


public class Parser
{
	public static String section_regex = "/";
	public static String some_regex = Pattern.quote("|");
	public static String map_regex = Pattern.quote(":");
	public static String coord_regex = "X|Y";
	public static String vcoord_regex = "X|Y|VX|VY|T";
	public static String comm_regex = "A|T";

	public static double default_dir = 0;
	public static double turnit = 0.5;
	public static double thrustit = 0.3;

	public static double obj_radius = 1;
	public static double ob_radius = 5;
	public static double ve_radius = 3;
	
	public Game game = null;
	
	public void run(InputStream istream, OutputStream ostream, String user) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(istream));
		PrintWriter pw = new PrintWriter(ostream);
		

		pw.write("CONNECT/" + user + "/");
		
		// note : split with -1 accepts empty string, and we retrieve last / to avoid last empty string
		
		while (true)
		{
			String command = br.readLine();
			if(command == null)
				throw new Exception("Server disconnected");
			
			System.out.println(command);
			
			if(command.charAt(command.length() - 1) != '/')
				throw new CommandUsageException("Expected / at the end of the command");
			
			command = command.substring(0, command.length() - 1);
			
			String[] sections = command.split(section_regex, -1);
			
			String command_name = sections[0];
			
			if(game == null && !command_name.equals("WELCOME"))
				throw new Exception("WELCOME should be the first command that receives the client");
			
			if(command_name.equals("WELCOME"))
			{
				if(sections.length != 5)
					throw new CommandUsageException("WELCOME/phase/scores/coord/coords/");
				
				Arena arena = new Arena();
				
				boolean state = false;
				{
					String phase = sections[1];
					if(phase.equals("attente"))
						state = false;
					else if(phase.equals("jeu"))
						state = true;
					else
						throw new CommandUsageException("jeu or attente");
						
				}

				Set<String> others = new HashSet<>();
				Map<String, Integer> mscores = new HashMap<>();
				{
					String scores = sections[2];
					String[] ascores = scores.split(some_regex, -1);
					
					for(String namescore : ascores)
					{
						String[] name_score = namescore.split(map_regex, -1);

						if(name_score.length != 2)
							throw new CommandUsageException("<name>:<score>");
						
						String name = name_score[0];
						String score = name_score[1];
						
						if(mscores.containsKey(name))
							throw new Exception("At least two players with same name");
						
						mscores.put(name, Integer.parseInt(score));
						if(!name.equals(user))
							others.add(name);
					}
				}
				if(!mscores.containsKey(user))
					mscores.put(user, 0);
				
				PositionEntity objective = null;
				if(!sections[3].isEmpty()) // if an objective is defined
				{
					String coord = sections[3];
					String[] x_y = coord.split(coord_regex, -1);
					
					if(x_y.length != 3)
						throw new CommandUsageException("X<value>Y<value>");
					
					objective = new PositionEntity(arena, 
							Double.parseDouble(x_y[1]),
							Double.parseDouble(x_y[2]), obj_radius);
				}
				
				Map<String, IMovingEntity> entities = new HashMap<>();
				if(!sections[4].isEmpty()) // if some obstacles are defined
				{
					String coords = sections[4];
					String[] acoords = coords.split(some_regex, -1);
					
					for(String namecoord : acoords)
					{
						String[] name_coord = namecoord.split(map_regex, -1);
						
						if(name_coord.length != 2)
							throw new CommandUsageException("<name>:<coord>");
						
						String name = name_coord[0];
						
						if(entities.containsKey(name))
							throw new Exception("At least two entities with same name");

						String coord = name_coord[1];
						String[] x_y = coord.split(coord_regex, -1);
						
						if(x_y.length != 3)
							throw new CommandUsageException("X<value>Y<value>");
						
						MovingEntity entity = new MovingEntity(arena,
									Double.parseDouble(x_y[1]),
									Double.parseDouble(x_y[2]), 0, 0, ob_radius);
						entities.put(name, entity);
					}
				}
				
				game = new Game(user, others, arena, entities, new HashMap<>(), mscores, objective, state);
			}
			else if(command_name.equals("DENIED"))
			{
				if(sections.length != 1)
					throw new CommandUsageException("DENIED/");
				return;
			}
			else if(command_name.equals("NEWPLAYER"))
			{
				if(sections.length != 2)
					throw new CommandUsageException("NEWPLAYER/user/");
				String name = sections[1];
				if(name.equals(user))
					throw new Exception("Expected other player than client");
				if(game.others.contains(name))
					throw new Exception("At least two entities with same name");
				game.addPlayer(name);
			}
			else if(command_name.equals("PLAYERLEFT"))
			{
				if(sections.length != 2)
					throw new CommandUsageException("PLAYERLEFT/user/");
				String name = sections[1];
				if(name.equals(user))
					throw new Exception("User can't leave his own game");
				if(!game.others.contains(name))
					throw new Exception("The leaving player actually does not exist");
				game.removePlayer(name);
			}
			else if(command_name.equals("SESSION"))
			{
				if(sections.length != 4)
					throw new CommandUsageException("SESSION/coords/coord/coords/");

				Map<String, Vehicle> vehicles = game.vehicles;
				assert(vehicles.isEmpty());
				{
					String coords = sections[1];
					
					String[] acoords = coords.split(some_regex, -1);
					
					for(String namecoord : acoords)
					{
						String[] name_coord = namecoord.split(map_regex, -1);
						
						if(name_coord.length != 2)
							throw new CommandUsageException("<name>:<coord>");
						
						String name = name_coord[0];
						
						if(!name.equals(user) && !game.others.contains(name))
							throw new Exception("Player " + name + " unknown");
						
						if(vehicles.containsKey(name))
							throw new Exception("At least two players with same name");

						String coord = name_coord[1];
						String[] x_y = coord.split(coord_regex, -1);
						
						if(x_y.length != 3)
							throw new CommandUsageException("X<value>Y<value>");
						
						MovingEntity entity = new MovingEntity(game.arena,
									Double.parseDouble(x_y[1]),
									Double.parseDouble(x_y[2]), 0, 0, ve_radius);
						vehicles.put(name, new Vehicle(entity, default_dir));
					}
					
					if(acoords.length != game.others.size() + 1)
						throw new Exception("Some players vehicles are missing (or too many)");
				}
				game.vehicles = vehicles;
				
				PositionEntity objective = null;
				{
					String coord = sections[2];
					String[] x_y = coord.split(coord_regex, -1);
					
					if(x_y.length != 3)
						throw new CommandUsageException("X<value>Y<value>");
					
					objective = new PositionEntity(game.arena, 
							Double.parseDouble(x_y[1]),
							Double.parseDouble(x_y[2]), obj_radius);
				}
				game.objective = objective;
				
				Map<String, IMovingEntity> entities = game.entities;
				if(!sections[3].isEmpty()) // if there are obstacles
				{
					String coords = sections[3];
					
					String[] acoords = coords.split(some_regex, -1);
					
					for(String namecoord : acoords)
					{
						String[] name_coord = namecoord.split(map_regex, -1);
						
						if(name_coord.length != 2)
							throw new CommandUsageException("<name>:<coord>");
						
						String name = name_coord[0];
						
						if(entities.containsKey(name))
							throw new Exception("At least two entities with same name");

						String coord = name_coord[1];
						String[] x_y = coord.split(coord_regex, -1);
						
						if(x_y.length != 3)
							throw new CommandUsageException("X<value>Y<value>");
						
						MovingEntity entity = new MovingEntity(game.arena,
									Double.parseDouble(x_y[1]),
									Double.parseDouble(x_y[2]), 0, 0, ob_radius);
						entities.put(name, entity);
					}
				}
				game.playing = true;
			}
			else if(command_name.equals("WINNER"))
			{
				if(sections.length != 2)
					throw new CommandUsageException("WINNER/scores/");
				Map<String, Integer> mscores = game.scores;
				{
					String scores = sections[1];
					String[] ascores = scores.split(some_regex, -1);
					
					for(String namescore : ascores)
					{
						String[] name_score = namescore.split(map_regex, -1);
						
						if(name_score.length != 2)
							throw new CommandUsageException("<name>:<score>");
						
						String name = name_score[0];
						String score = name_score[1];
						
						if(!mscores.containsKey(name))
							throw new Exception("Player unexpected in scores review");
						
						mscores.put(name, Integer.parseInt(score));
					}
				}
				game.reset();
			}
			else if(command_name.equals("TICK"))
			{
				if(sections.length != 2)
					throw new CommandUsageException("TICK/coords/");

				{
					String coords = sections[1];
					
					String[] acoords = coords.split(some_regex, -1);
					
					for(String namecoord : acoords)
					{
						String[] name_coord = namecoord.split(map_regex, -1);
						
						if(name_coord.length != 2)
							throw new CommandUsageException("<name>:<coord>");
						
						String name = name_coord[0];
						
							
						String coord = name_coord[1];
						String[] x_y_vx_vy_t = coord.split(vcoord_regex, -1);
						
						if(x_y_vx_vy_t.length != 3)
							throw new CommandUsageException("X<value>Y<value>");
						
						Vehicle vehicle = game.vehicles.get(name);
						vehicle.set_x(Double.parseDouble(x_y_vx_vy_t[1]));
						vehicle.set_y(Double.parseDouble(x_y_vx_vy_t[2]));
						//vehicle.set_vx(Double.parseDouble(x_y_vx_vy_t[3]));
						//vehicle.set_vy(Double.parseDouble(x_y_vx_vy_t[4]));
						//vehicle.set_dir(Double.parseDouble(x_y_vx_vy_t[5]));
					}
					
				}
			}
			else if(command_name.equals("NEWOBJ"))
			{
				if(sections.length != 3)
					throw new CommandUsageException("NEWOBJ/coord/scores/");
				
				PositionEntity objective = null;
				{
					String coord = sections[1];
					String[] x_y = coord.split(coord_regex, -1);
					
					if(x_y.length != 3)
						throw new CommandUsageException("X<value>Y<value>");
					
					objective = new PositionEntity(game.arena, 
							Double.parseDouble(x_y[1]),
							Double.parseDouble(x_y[2]), obj_radius);
				}
				game.objective = objective;
				
				Map<String, Integer> mscores = game.scores;
				{
					String scores = sections[2];
					String[] ascores = scores.split(some_regex, -1);
					
					for(String namescore : ascores)
					{
						String[] name_score = namescore.split(map_regex, -1);
						
						if(name_score.length != 2)
							throw new CommandUsageException("<name>:<score>");
						
						String name = name_score[0];
						String score = name_score[1];
						
						if(!mscores.containsKey(name))
							throw new Exception("Player unexpected in scores review");
						
						mscores.put(name, Integer.parseInt(score));
					}
				}
			}
			else
				throw new CommandUsageException("Unknown command " + command_name);
			System.out.println(game);
		}
	}
}
