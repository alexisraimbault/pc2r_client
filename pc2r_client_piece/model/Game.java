package pc2r_client_piece.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pc2r_client_piece.model.interfaces.IMovingEntity;

public class Game
{
	public String user;
	public Set<String> others;
	public Arena arena;
	public Map<String, IMovingEntity> entities;
	public Map<String, Vehicle> vehicles;
	public Map<String, Integer> scores;
	public PositionEntity objective;
	public boolean playing;
	
	public Game(String user, Set<String> others, Arena arena, Map<String, IMovingEntity> entities, Map<String, Vehicle> vehicles, Map<String, Integer> scores, PositionEntity objective, boolean playing)
	{
		this.user = user;
		this.others = others;
		this.arena = arena;
		this.entities = entities;
		this.vehicles = vehicles;
		this.scores = scores;
		this.objective = objective;
		this.playing = playing;
	}
	
	void addPlayer(String name)
	{
		others.add(name);
		scores.put(name, 0); // 0 default score or wait server tick to know it ?
	}

	public void removePlayer(String name)
	{
		others.remove(name);
		entities.remove(name);
		vehicles.remove(name);
		scores.remove(name);
	}
	
	public void removeEntity(String name)
	{
		entities.remove(name);
	}
	
	public int nbPlayers()
	{
		return scores.size();
	}
	
	public void reset()
	{
		entities.clear();
		vehicles.clear();
		objective = null;
		playing = false;
	}
	
	public Map.Entry<String, Integer> getWinner()
	{
		Map.Entry<String, Integer> maxEntry = null;

		for (Map.Entry<String, Integer> entry : scores.entrySet())
		{
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
		    {
		        maxEntry = entry;
		    }
		}
		return maxEntry;
	}
	
	@Override
	public String toString()
	{
		return "User = " + user + "\nOther players = " + others.toString() + "\n" + arena.toString()  + "\nEntities = " + entities.toString()  + "\nVehicles = " + vehicles.toString() + "\nScores = " + scores.toString() + "\nObjective = " + (objective == null ? "no objective" : objective.toString()) + "\nState = " + playing + "\n";
	}
}
