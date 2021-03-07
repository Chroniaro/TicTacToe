package shell;

import java.io.Serial;
import java.io.Serializable;

public abstract class Player implements Serializable
{
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -7399124533860674212L;

	final protected static Point pass = new Point(0, 0)
			{
				@Override
				public String toString()
				{
					return "pass";
				}
			};
	
	private Point lastClick;
	protected byte tileKey;
	private int victorySize;

	public abstract Point getNextMove(Board board, Point lastMove);

	final Point move(Board board, Point lastMove, int victorySize)
	{
		this.victorySize = victorySize;
		final Point p = getNextMove(board, lastMove);
		lastClick = null;
		return p;
	}

	public boolean needsInput()
	{
		return false;
	}

	final void eventClick(Point location)
	{
		lastClick = location;
	}

	final boolean hasInput()
	{
		return lastClick != null;
	}

	protected final Point getInputLocation()
	{
		if (!needsInput() || !hasInput())
			throw new InvalidInputRequestException(
					"This player is non interactive. To change this, make needsInput() return true.");

		return lastClick;
	}
	
	public final int getVictorySize()
	{
		return victorySize;
	}
}

class InvalidInputRequestException extends Error
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2587905137889394183L;

	public InvalidInputRequestException(String message)
	{
		super(message);
	}
}