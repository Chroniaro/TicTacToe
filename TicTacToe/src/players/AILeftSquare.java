package players;

import shell.Board;
import shell.Player;
import shell.Point;

public class AILeftSquare extends Player
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2574221159237548700L;

	@Override
	public Point getNextMove(Board board, Point lastMove)
	{
		if(lastMove == null || lastMove == pass)
			return pass;
		
		lastMove = lastMove.translate(-1, 0);
		if(board.isLegal(lastMove))
			return lastMove;
		else
			return pass;
	}
}
