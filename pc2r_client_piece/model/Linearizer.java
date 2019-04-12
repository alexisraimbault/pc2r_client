package pc2r_client_piece.model;

public class Linearizer
{
	public double min;
	public double max;
	public double new_min;
	public double new_max;
	
	public Linearizer(double min, double max, double new_min, double new_max)
	{
		this.min = min;
		this.max = max;
		this.new_min = new_min;
		this.new_max = new_max;
	}
	
	public double linearize(double value)
	{
		return ((value - min) / (max - min)) * (new_max - new_min) + new_min;
	}
}
