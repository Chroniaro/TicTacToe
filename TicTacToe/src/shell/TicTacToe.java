package shell;

import javax.swing.JFrame;
import players.*;

public class TicTacToe
{
	static boolean running;
	
	public static void main(String... args) throws Throwable
	{
		TicTacToeGame game = new TicTacToeGame(new Board(19, 19), 5, new PlayerHuman(), new AISmart());
		
		JFrame window = new JFrame();
		window.setTitle("Tic Tac Toe");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		window.add(game);
		
		window.pack();
		running = true;
		while(running)
			game.updateGame();
	}
}
