package shell;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import javax.swing.JComponent;

public class TicTacToeGame extends JComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7537037170211491967L;

	Board board;
	int runLen;
	Player p1, p2;

	boolean playable = true;
	Player nextPlayer;
	RunInfo vInfo;
	Point lastMove;
	long timer = System.currentTimeMillis();
	private final static DecimalFormat TimerFormat = new DecimalFormat("#0.00");
	public static boolean HighlightRuns = false;

	public TicTacToeGame(Board board, int victoryCondition, Player player1, Player player2)
	{
		setPreferredSize(new Dimension(600, 600));

		this.board = board;
		this.runLen = victoryCondition;
		p1 = player1;
		p1.tileKey = Board.Key_X;
		p2 = player2;
		p2.tileKey = Board.Key_O;
		nextPlayer = player1;

		this.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{}

			@Override
			public void mousePressed(MouseEvent e)
			{}

			@Override
			public void mouseExited(MouseEvent e)
			{}

			@Override
			public void mouseEntered(MouseEvent e)
			{}

			@Override
			public void mouseClicked(MouseEvent e)
			{
				double scale = Math.min(getWidth() / (board.getWidth() + 20), getHeight() / (Math.max(board.getHeight(), 14) + 2));

				java.awt.Point clickLocation = new java.awt.Point(e.getPoint());

				clickLocation.x -= getWidth() / 2 - scale * board.getWidth() / 2;
				clickLocation.y -= getHeight() / 2 - scale * board.getHeight() / 2;

				clickLocation.x /= scale;
				clickLocation.y /= scale;

				if (clickLocation.x < 0 || clickLocation.y < 0) return;
				if (clickLocation.x > board.getWidth() || clickLocation.y > board.getHeight()) return;

				nextPlayer.eventClick(new Point(clickLocation));
			}
		});
	}

	public void updateGame()
	{
		if (board.getEmptySquares() <= 0) playable = false;
		else if (playable)
		{
			if (getTime() <= 0)
			{
				skip();
			}
			else if (!nextPlayer.needsInput() || nextPlayer.hasInput())
			{
				Point p = Player.pass;
				try
				{
					p = nextPlayer.move(board.clone(), lastMove, runLen);
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}

				if (p == Player.pass)
				{
					skip();
				}
				else if (p != null && board.isLegal(p))
				{
					lastMove = new Point(p.x, p.y);

					board.place(nextPlayer.tileKey, p);

					vInfo = board.checkVictory(p, runLen);
					if (vInfo != null)
					{
						playable = false;
						return;
					}
					changeTurn();
				}
			}
		}
		repaint();
	}

	private void changeTurn()
	{
		timer = System.currentTimeMillis();

		if (nextPlayer == p1) nextPlayer = p2;
		else if (nextPlayer == p2) nextPlayer = p1;
	}

	private void skip()
	{
		lastMove = Player.pass;
		changeTurn();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform initialMatrix = g2d.getTransform();

		double scale = Math.min(getWidth() / (board.getWidth() + 20), getHeight() / (Math.max(board.getHeight(), 14) + 2));

		g2d.translate(getWidth() / 2 - scale * board.getWidth() / 2, getHeight() / 2 - scale * board.getHeight() / 2);
		g2d.scale(scale / 10, scale / 10);

		g2d.setStroke(new BasicStroke(1));

		g2d.setColor(new Color(1.f, .9f, .6f));
		g2d.fillRect(0, 0, 10 * board.getWidth(), 10 * board.getHeight());

		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, 10 * board.getWidth(), 10 * board.getHeight());

		for (int x = 1; x < board.getWidth(); x++)
			g2d.drawLine(10 * x, 0, 10 * x, 10 * board.getHeight());

		for (int y = 1; y < board.getHeight(); y++)
			g2d.drawLine(0, 10 * y, 10 * board.getWidth(), 10 * y);

		if (HighlightRuns && lastMove != null && lastMove != Player.pass)
		{
			g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
			for (byte i = 0; i < 4; i++)
			{
				RunInfo ri = board.getRun(lastMove, i, 1);
				Point[] run = ri.getEnds();
				g2d.setColor(new Color(1.f, 0.f, 0.2f, 1f));
				g2d.drawLine(10 * run[0].x + 5, 10 * run[0].y + 5, 10 * run[1].x + 5, 10 * run[1].y + 5);
				g2d.setColor(new Color(0.f, 1.f, 0.f, .3f));
				for (int n = 0; n < ri.getNumberOfGaps(); n++)
					g2d.drawLine(10 * ri.getGap(n).x + 5, 10 * ri.getGap(n).y + 5, 10 * ri.getGap(n).x + 5, 10 * ri.getGap(n).y + 5);

				for (int e = 1; e <= 3; e++)
				{
					g2d.setColor(new Color(.8f, 1f, .2f, .5f / e));
					Point p = ri.getExtension(e);
					g2d.drawLine(10 * p.x + 5, 10 * p.y + 5, 10 * p.x + 5, 10 * p.y + 5);
					p = ri.getExtension(-e);
					g2d.drawLine(10 * p.x + 5, 10 * p.y + 5, 10 * p.x + 5, 10 * p.y + 5);
				}
			}
			for (byte i = 0; i < 4; i++)
			{
				g2d.setColor(new Color(.8f, .8f, 0.f, 1f));
				Point[] run = board.getRun(lastMove, i).getEnds();
				g2d.drawLine(10 * run[0].x + 5, 10 * run[0].y + 5, 10 * run[1].x + 5, 10 * run[1].y + 5);
			}
		}

		if (vInfo != null)
		{
			g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.CAP_ROUND));
			g2d.setColor(new Color(0.f, 0.f, 1.f, HighlightRuns ? 1f : .3f));
			g2d.drawLine(vInfo.getEnds()[0].x * 10 + 5, vInfo.getEnds()[0].y * 10 + 5, vInfo.getEnds()[1].x * 10 + 5, vInfo.getEnds()[1].y * 10 + 5);
		}

		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(1));

		for (int x = 0; x < board.getWidth(); x++)
			for (int y = 0; y < board.getHeight(); y++)
			{
				if (board.getTile(x, y) == Board.Key_X)
				{
					g2d.drawLine(x * 10 + 2, y * 10 + 2, x * 10 + 8, y * 10 + 8);
					g2d.drawLine(x * 10 + 8, y * 10 + 2, x * 10 + 2, y * 10 + 8);
				}
				else if (board.getTile(x, y) == Board.Key_O)
				{
					g2d.drawOval(x * 10 + 2, y * 10 + 2, 6, 6);
				}
			}

		RoundRectangle2D border = new RoundRectangle2D.Double(nextPlayer == p1 ? -98 : board.getWidth() * 10 + 12, 5 * board.getHeight() - 55, 86, 130, 10, 10);
		g2d.setColor(Color.red);
		g2d.fill(border);
		g2d.setColor(Color.red.darker());
		g2d.draw(border);

		g2d.setColor(Color.black);
		g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
		g2d.drawString("X", -80, 5 * board.getHeight());
		g2d.drawString("O", board.getWidth() * 10 + 30, 5 * board.getHeight());

		AffineTransform preMatrix = g2d.getTransform();

		g2d.translate(-95, 5 * board.getHeight() + 25);
		g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		String val = p1.getClass().getSimpleName();
		double w = g2d.getFontMetrics().stringWidth(val);
		g2d.scale(80 / w, 1);
		g2d.drawString(val, 0, 0);

		g2d.setTransform(preMatrix);

		g2d.translate(10 * board.getWidth() + 15, 5 * board.getHeight() + 25);
		g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		val = p2.getClass().getSimpleName();
		w = g2d.getFontMetrics().stringWidth(val);
		g2d.scale(80 / w, 1);
		g2d.drawString(val, 0, 0);

		g2d.setTransform(preMatrix);

		g2d.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		if (playable) g2d.drawString(TimerFormat.format(getTime()), nextPlayer == p1 ? -87 : board.getWidth() * 10 + 23, 5 * board.getHeight() + 50);
		else if (vInfo != null) g2d.drawString("Winner!", nextPlayer == p1 ? -95 : board.getWidth() * 10 + 15, 5 * board.getHeight() + 50);

		g2d.setTransform(initialMatrix);
	}

	private double getTime()
	{
		return (nextPlayer.needsInput() ? 60 : 5) - ((System.currentTimeMillis() - timer) / 1000.0);
	}
}
