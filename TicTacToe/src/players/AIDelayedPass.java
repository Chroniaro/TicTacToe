package players;

import shell.*;

public class AIDelayedPass extends Player
{
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
