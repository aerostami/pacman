package graphics;

import javax.swing.JButton;

/**
 * 
 * @author ALI ROSTAMI - aerostami@gmail.com - each block of the game which can hold a
 *         unit, a power or a seed on it
 * 
 */
public class Block extends JButton {
	private static final long serialVersionUID = 2663179038958554785L;
	public volatile boolean hasSeed = false;
	public volatile String powerUp = null;
	public volatile Long powerTime = 0L;
	public boolean isRoad = true;
}
