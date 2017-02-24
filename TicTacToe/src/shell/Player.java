package shell;

import java.io.Serializable;

public abstract class Player implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7399124533860674212L;

	final protected static Point pass = new Point(0, 0);
	
	private Point lastClick;
	protected byte tileKey;

	public abstract Point getNextMove(Board board, Point lastMove);

	final Point move(Board board, Point lastMove)
	{
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