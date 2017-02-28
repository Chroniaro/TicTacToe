package players;

import shell.*;

public class AIDelayedPass extends Player
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3944020240112270325L;

	@Override
	public boolean needsInput()
	{
		return true;
	}
	
	@Override
	public Point getNextMove(Board board, Point lastMove)
	{
		return pass;
	}
}
