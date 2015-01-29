package graphics;

import java.awt.Color;
import java.awt.Component;
import java.util.Random;

/**
 * 
 * @author ALI ROSTAMI - aerostami@gmail.com - Generates random color and gives it a
 *         component
 * 
 */
public class MenuColor extends Thread {
	Component cmp;
	public boolean end = false;
	Random rand = new Random();

	/**
	 * The constructor of the class which takes a component to change it's color
	 * 
	 * @param cmp
	 *            given component
	 */
	public MenuColor(Component cmp) {
		this.cmp = cmp;
	}

	/**
	 * generates a random color
	 * 
	 * @return random color
	 */
	private Color randColor() {
		int r = rand.nextInt(200);
		int g = rand.nextInt(200);
		int b = rand.nextInt(200);
		return new Color(r, g, b);
	}

	/**
	 * run	changes the color of the component in random time
	 */
	public void run() {
		while (!end) {
			cmp.setBackground(randColor());

			try {
				sleep(rand.nextInt(4000) + 50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
