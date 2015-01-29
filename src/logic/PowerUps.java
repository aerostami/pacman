package logic;

import executive.PacMan;
import graphics.Block;
import graphics.GraphicalView;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author ALI ROSTAMI - aerostami@gmail.com - this class runs to give random power ups to
 *         the game
 * 
 */
public class PowerUps extends Thread {
	int row;
	int col;
	Long lastPowerUpTime = System.currentTimeMillis() + 3000;
	Random rand = new Random();
	ArrayList<Block> runningPowerUps = new ArrayList<Block>();
	ArrayList<Block> mustRemove = new ArrayList<Block>();

	/**
	 * the run method of the thread which generates random power ups
	 */
	public void run() {

		while (PacMan.running) {
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mustRemove.clear();
			for (Block block : runningPowerUps)
				if (block.powerUp != null
						&& block.powerTime + 8500L < System.currentTimeMillis())
					mustRemove.add(block);
			for (Block block : mustRemove)
				removePowerUp(block);

			givePowerUp();

		}
	}

	/**
	 * removes the power up from given block when it has expired
	 * 
	 * @param block
	 */
	private void removePowerUp(Block block) {
		GraphicalView.hidePower(block);
		block.powerUp = null;
		runningPowerUps.remove(block);
	}

	/**
	 * gives a power up to a random block
	 */
	private void givePowerUp() {
		findEmptyBlock();// find a col n row
		String power = choosePower();
		PacMan.blocks[col][row].powerUp = power;
		PacMan.blocks[col][row].powerTime = System.currentTimeMillis();
		runningPowerUps.add(PacMan.blocks[col][row]);
		GraphicalView.showPower(PacMan.blocks[col][row]);
	}

	/**
	 * randomly chooses a power
	 * 
	 * @return
	 */
	private String choosePower() {
		int r = rand.nextInt(3);
		if (r == 0)
			return "speed";
		else if (r == 1)
			return "freez";
		return "multiply";
	}

	/**
	 * randomly chooses a suitable block for the power up
	 */
	private void findEmptyBlock() {
		col = 1;
		row = 1;
		while (!(PacMan.blocks[col][row].isRoad && PacMan.isAvailable[col][row]
				&& !PacMan.blocks[col][row].hasSeed && PacMan.blocks[col][row].powerUp == null)) {
			col = rand.nextInt(14);
			row = rand.nextInt(12);
		}
	}
}
