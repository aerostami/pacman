package logic;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import executive.PacMan;
import graphics.GraphicalView;


/**
 * 
 * @author ALI ROSTAMI - aerostami@gmail.com - the super class of all units
 * 
 */
public class Man extends Thread {
	public ImageIcon pic;
	// public String pic;
	public int row = 0;
	public int col = -1;
	public char dir = 'R';
	ImageIcon seed = new ImageIcon("src/data/images/seed.jpg");
	ImageIcon empty = new ImageIcon("src/data/images/g3.jpg");
	int speed;

	/**
	 * moves the unit to the located block
	 * 
	 * @param y
	 *            row of the block
	 * @param x
	 *            column of the block
	 */
	protected void go2(int y, int x) {

		if (0 <= col && col < PacMan.isAvailable.length && 0 <= row
				&& row < PacMan.isAvailable[0].length
				&& PacMan.isAvailable[col][row]) {
			if (PacMan.blocks[col][row].hasSeed)
				PacMan.blocks[col][row].setIcon(seed);
			else
				PacMan.blocks[col][row].setIcon(empty);
			GraphicalView.showPower(PacMan.blocks[col][row]);
		}

		row = x;
		col = y;
		PacMan.blocks[col][row].setIcon(pic);
	}

	public static boolean hasShownMsg = false;

	/**
	 * Stops all the running threads and displays a message to show who won the
	 * game
	 */
	protected void endGame() {
		PacMan.running = false;
		if (!hasShownMsg) {
			hasShownMsg = true;
			if (1 < PacMan.playersNum)
				if (PacMan.pac.score == PacMan.pac2.score)
					JOptionPane.showMessageDialog(null, "It's a tie!");
				else if (PacMan.pac.score < PacMan.pac2.score)
					JOptionPane.showMessageDialog(null, PacMan.p2name
							+ " won!!", null, 1, new ImageIcon("src/data/images/pac2.png"));
				else
					JOptionPane.showMessageDialog(null, PacMan.p1name
							+ " won!!", null, 1, new ImageIcon("src/data/images/pac.png"));
			else if (0 < PacMan.seedNum)
				JOptionPane.showMessageDialog(null, "You lost!!");
			else
				JOptionPane.showMessageDialog(null, "You won!!");

			 PacMan.restart();
		}
	}
}
