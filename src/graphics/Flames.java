package graphics;

import java.util.Random;

import javax.swing.ImageIcon;

import executive.PacMan;


/**
 * 
 * @author ALI ROSTAMI - aerostami@gmail.com - Gives an animated view to the walls with
 *         flames of fire and changes their iconImage in it's thread
 * 
 */
public class Flames extends Thread {
	Random r = new Random();
	
	/**
	 * chooses a random flame image and changes the icon images rapidly
	 */
	public void run() {
		boolean h = PacMan.running;
		while (h) {
			h = PacMan.running;
			for (Block j : PacMan.walls) {
				int i = r.nextInt(12) + 1;
				j.setIcon(new ImageIcon("src/data/fire/fire" + i + ".jpg"));
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}