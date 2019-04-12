package pc2r_client_piece.model;

public class CommandUsageException extends Exception
{
	public CommandUsageException(String command)
	{
		super(command);
	}
}
