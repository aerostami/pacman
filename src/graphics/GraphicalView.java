package graphics;

import javax.swing.ImageIcon;

/**
 * 
 * @author Ali Rostami - aerostami@gmail.com - this class reads the images and has public
 *         functions to show them
 * 
 */
public class GraphicalView {
	static ImageIcon light = new ImageIcon("src/data/images/light.jpg");
	static ImageIcon freez = new ImageIcon("src/data/images/freez.jpg");
	static ImageIcon doubled = new ImageIcon("src/data/images/doubled.jpg");
	static ImageIcon empty = new ImageIcon("src/data/images/g3.jpg");

	/**
	 * hides the shown power from the given block
	 * @param block 
	 */
	public static void hidePower(Block block) {
		if (block.powerUp != null)
			block.setIcon(empty);
		else
			System.out.println(block.powerUp);
	}

	/**
	 * shows the power of the given block
	 * @param block
	 */
	public static void showPower(Block block) {
		if (block.powerUp != null)
			if (block.powerUp.equals("speed"))
				block.setIcon(light);
			else if (block.powerUp.equals("freez"))
				block.setIcon(freez);
			else if (block.powerUp.equals("multiply"))
				block.setIcon(doubled);
	}
}
