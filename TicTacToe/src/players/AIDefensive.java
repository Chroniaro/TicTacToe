package players;

import shell.Board;
import shell.Point;
import shell.RunInfo;

public class AIDefensive extends AISmart
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5890191271675225345L;

	@Override
	public Point getNextMove(Board board, Point lastMove)
	{
		RunInfo d = findStrongestRun(board, getVictorySize(), myLastMove, lastMove);
		
		myLastMove = findDefense(board, d);
		if(myLastMove == null)
			return planNewAttack(board, d.getEnds());
		else
			return myLastMove;
	}
}
