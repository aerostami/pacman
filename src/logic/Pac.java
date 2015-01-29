package logic;

import executive.PacMan;
import graphics.Sounds;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;



/**
 * 
 * @author ALI ROSTAMI - aerostami@gmail.com - main pacmans which are controlled by the
 *         players
 * 
 */
public class Pac extends Man {
	public static int pacNum = 0;
	public int pacId;
	private JLabel scoreLabel;
	private String name;
	public int score;
	private int pacSpeed = 500;
	private ArrayList<String> powers = new ArrayList<String>();
	private ArrayList<Long> powersTime = new ArrayList<Long>();
	private int multiplyed = 1;
	private int powerSpeed = 0;
	private Random rand = new Random();

	/**
	 * Constructor for the pacman according to their pacId
	 */
	public Pac() {
		pacNum += 1;
		pacId = pacNum;
		super.speed = pacSpeed;
		if (pacId % 2 == 1) {
			scoreLabel = PacMan.p1score;
			name = PacMan.p1name;
		} else {
			scoreLabel = PacMan.p2score;
			name = PacMan.p2name;
		}
		pic = new ImageIcon("src/data/images/pac" + pacId + "Right.jpg");
		chooseRandomDir();
		findEmptyBlock();
	}

	/**
	 * randomly chooses a suitable block for the power up
	 */
	private void findEmptyBlock() {
		col = 1;
		row = 1;
		while (!(PacMan.blocks[col][row].isRoad && PacMan.isAvailable[col][row] && row<9
				&& !PacMan.blocks[col][row].hasSeed && PacMan.blocks[col][row].powerUp == null)) {
			col = rand.nextInt(14);
			row = rand.nextInt(12);
		}
		go2(col,row);
//		PacMan.blocks[col][row].setIcon(pic);
	}

	/**
	 * randomly chooses a direction
	 */
	private void chooseRandomDir() {
		int random = rand.nextInt(4);
		if (random == 0)
			dir = 'R';
		else if (random == 1)
			dir = 'L';
		else if (random == 2)
			dir = 'U';
		else
			dir = 'D';
	}

	/**
	 * makes a moving sound and moves to the target block
	 */
	public void go2(int col, int row) {
		if (pacId % 2 == 1 || powerSpeed != 0)
			Sounds.play("src/data/sounds/boing.mp3");
		super.go2(col, row);
	}

	/**
	 * moves the pacman which is controlled by the player
	 */
	synchronized public void run() {
		while (PacMan.running) {
			if (col != -1)
				PacMan.isAvailable[col][row] = true;
			if (dir == 'R')
				if (col + 1 < PacMan.isAvailable.length
						&& PacMan.isAvailable[col + 1][row]) {
					pic = new ImageIcon("src/data/images/pac" + pacId + "Right.jpg");
					go2(col + 1, row);
				} else {
					dir = 'L';
					pic = new ImageIcon("src/data/images/pac" + pacId + "Left.jpg");
					if (0 <= col - 1 && PacMan.isAvailable[col - 1][row])
						go2(col - 1, row);
				}
			else if (dir == 'L')
				if (0 <= col - 1 && PacMan.isAvailable[col - 1][row]) {
					pic = new ImageIcon("src/data/images/pac" + pacId + "Left.jpg");
					go2(col - 1, row);
				} else {
					dir = 'R';
					pic = new ImageIcon("src/data/images/pac" + pacId + "Right.jpg");
					if (col + 1 < PacMan.isAvailable.length
							&& PacMan.isAvailable[col + 1][row])
						go2(col + 1, row);
				}
			else if (dir == 'U')
				if (0 <= row - 1 && PacMan.isAvailable[col][row - 1]) {
					pic = new ImageIcon("src/data/images/pac" + pacId + "Up.jpg");
					go2(col, row - 1);
				} else {
					dir = 'D';
					pic = new ImageIcon("src/data/images/pac" + pacId + "Down.jpg");
					if (row + 1 < PacMan.isAvailable[0].length
							&& PacMan.isAvailable[col][row + 1])
						go2(col, row + 1);
				}
			else if (dir == 'D')
				if (row + 1 < PacMan.isAvailable[0].length
						&& PacMan.isAvailable[col][row + 1]) {
					pic = new ImageIcon("src/data/images/pac" + pacId + "Down.jpg");
					go2(col, row + 1);
				} else {
					dir = 'U';
					if (0 <= row - 1 && PacMan.isAvailable[col][row - 1]) {
						pic = new ImageIcon("src/data/images/pac" + pacId + "Up.jpg");
						go2(col, row - 1);
					}
				}
			PacMan.isAvailable[col][row] = false;
			if (PacMan.blocks[col][row].hasSeed) {
				score += multiplyed;
				PacMan.seedNum -= 1;
				if (PacMan.seedNum == 0)
					endGame();
			} else if (PacMan.blocks[col][row].powerUp != null) {
				getPower(PacMan.blocks[col][row].powerUp);
			}

			PacMan.blocks[col][row].hasSeed = false;

			if (multiplyed != 1)
				scoreLabel.setText("X" + multiplyed + " " + name + " = "
						+ score);
			else
				scoreLabel.setText(name + " = " + score);

			removeExpirerdPowers();
			try {
				sleep(speed - powerSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		Thread.currentThread().interrupt();
	}

	int powerLimitTime = 7000;

	/**
	 * removes the power from this pac
	 */
	private void removeExpirerdPowers() {
		for (int i = powersTime.size() - 1; 0 <= i; i -= 1)
			if (powersTime.get(i) + powerLimitTime < System.currentTimeMillis()) {
				String power = powers.get(i);
				powers.remove(i);
				powersTime.remove(i);
				endPowerIfNotAgain(power);
			}
	}

	/**
	 * checks if this power hasn't have repeated again and if not ends it
	 * 
	 * @param power
	 *            mentioned power
	 */
	private void endPowerIfNotAgain(String power) {
		for (String pow : powers)
			if (pow.equals(power))
				return;

		PacMan.jp.setBackground(Color.black);
		if (power.equals("speed")) {
			Sounds.play("src/data/sounds/mirror.mp3");
			powerSpeed = 0;
		} else if (power.equals("freez")) {
			Sounds.play("src/data/sounds/sneeze.mp3");
			if (pacId % 2 == 1) {
				if (PacMan.pac2 != null)
					PacMan.pac2.speed = PacMan.pac2.pacSpeed;
			} else
				PacMan.pac.speed = PacMan.pac.pacSpeed;
			if (PacMan.pinky != null)
				PacMan.pinky.speed = PacMan.pinky.inputGhostSpeed;
			if (PacMan.blue != null)
				PacMan.blue.speed = PacMan.blue.inputGhostSpeed;
			if (PacMan.red != null)
				PacMan.red.speed = PacMan.red.inputGhostSpeed;
		} else if (power.equals("multiply")) {
			Sounds.play("src/data/sounds/kid_laugh.mp3");
			multiplyed = 1;
		}
		// TODO
	}

	/**
	 * Acquires the mentioned power
	 * 
	 * @param power
	 */
	private void getPower(String power) {
		powersTime.add(System.currentTimeMillis());
		powers.add(power);

		PacMan.blocks[col][row].powerUp = null;
		if (power.equals("speed")) {
			Sounds.play("src/data/sounds/mip.mp3");
			PacMan.jp.setBackground(Color.red);
			powerSpeed = 200;
		} else if (power.equals("freez")) {
			Sounds.play("src/data/sounds/freeze.mp3");
			PacMan.jp.setBackground(Color.blue);
			if (pacId % 2 == 1) {
				if (PacMan.pac2 != null)
					PacMan.pac2.speed = 1150;
			} else
				PacMan.pac.speed = 1150;
			if (PacMan.pinky != null)
				PacMan.pinky.speed = 1300;
			if (PacMan.blue != null)
				PacMan.blue.speed = 1300;
			if (PacMan.red != null)
				PacMan.red.speed = 1300;
		} else if (power.equals("multiply")) {
			Sounds.play("src/data/sounds/chain.mp3");
			multiplyed *= 2;
		}
	}

}
