package graphics;

import javax.swing.JFrame;

public class Board extends JFrame {

	/**
	 * The main frame of the game which extends JFrame
	 */
	private static final long serialVersionUID = 1L;

	public Board(int row, int col) {
		this.setBounds(0, 0, 47 * row + 56, 47 * col + 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);// kills the program when
														// closed
		this.setLocationRelativeTo(null);// takes the window in the middle of
											// desktop
		this.setVisible(true);
	}
}
