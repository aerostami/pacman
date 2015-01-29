package logic;

import executive.PacMan;
import graphics.Sounds;

import java.util.Random;

import javax.swing.ImageIcon;

/**
 * 
 * @author ALI ROSTAMI - aerostami@gmail.com - Ghost robots which chase the players
 * 
 */
public class Ghost extends Man {
	public static Pac p1;
	public static Pac p2 = null;
	int inputGhostSpeed = 650;

	/**
	 * Constructor creates a ghost
	 * 
	 * @param col
	 *            for the block
	 * @param row
	 *            for the block
	 * @param img
	 *            the image for the ghost
	 * @param speed
	 *            the input speed for the ghost robot
	 */
	public Ghost(int col, int row, ImageIcon img, int speed) {
		super();
		inputGhostSpeed = speed;
		super.speed = inputGhostSpeed;
		this.row = row;
		this.col = col;
		this.pic = img;
	}

	Random rand = new Random();
	boolean[][] isAv;
	int lookedForThisPac;
	Pac chasedPac = p1;

	/**
	 * Sets the direction that the ghost has to go in order to catch the players
	 */
	void findPath() {
		isAv = PacMan.isAvailable;
		// if(col<0 || row<0 || 13<col || 11<row)
		// return;
		isAv[p1.col][p1.row] = true;
		if (1 < PacMan.playersNum) {
			isAv[p2.col][p2.row] = true;
			if (lookedForThisPac % 7 == 0) {
				int r = rand.nextInt(2);
				if (r == 1)// TODO (... && p1 is not in jail)
					chasedPac = p1;
				else
					chasedPac = p2;
			}
			lookedForThisPac += 1;
		}

		dir = PacMan.findPathPrivate(isAv, this.col, this.row, chasedPac.col,
				chasedPac.row).charAt(0);
		if (dir == 'r')
			dir = 'D';
		else if (dir == 'l')
			dir = 'U';
		else if (dir == 'd')
			dir = 'R';
		else if (dir == 'u')
			dir = 'L';

	}

	/**
	 * this Thread moves the ghost and controls it's actions
	 */
	synchronized public void run() {
		while (PacMan.running) {

			findPath();
			PacMan.isAvailable[col][row] = true;
			if (dir == 'R')
				if (col + 1 < PacMan.isAvailable.length
						&& PacMan.isAvailable[col + 1][row])
					go2(col + 1, row);
				else {
					dir = 'L';
					if (0 <= col - 1 && PacMan.isAvailable[col - 1][row])
						go2(col - 1, row);
				}
			else if (dir == 'L')
				if (0 <= col - 1 && PacMan.isAvailable[col - 1][row])
					go2(col - 1, row);
				else {
					dir = 'R';
					if (col + 1 < PacMan.isAvailable.length
							&& PacMan.isAvailable[col + 1][row])
						go2(col + 1, row);
				}
			else if (dir == 'U')
				if (0 <= row - 1 && PacMan.isAvailable[col][row - 1])
					go2(col, row - 1);
				else {
					dir = 'D';
					if (row + 1 < PacMan.isAvailable[0].length
							&& PacMan.isAvailable[col][row + 1])
						go2(col, row + 1);
				}
			else if (dir == 'D')
				if (row + 1 < PacMan.isAvailable[0].length
						&& PacMan.isAvailable[col][row + 1])
					go2(col, row + 1);
				else {
					dir = 'U';
					if (0 <= row - 1 && PacMan.isAvailable[col][row - 1])
						go2(col, row - 1);
				}
			PacMan.isAvailable[col][row] = false;
			if (!hasShownMsg
					&& (this.row == p1.row && this.col == p1.col)
					|| (1 < PacMan.playersNum && this.row == p2.row && this.col == p2.col)) {
				Sounds.play("src/data/sounds/scream.mp3");
				endGame();
			}
			try {
				sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		Thread.currentThread().interrupt();
	}
}