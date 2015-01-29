package graphics;

/**
 * 
 * @author Ali Rostami - aerostami@gmail.com - This class has functions to play the sound
 *         from directory
 * 
 */
public class Sounds {
	// public static Player kik = makePlayer("sounds/boing.mp3");

	/**
	 * plays sound from
	 * 
	 * @param dir
	 *            directory
	 */
	public static void play(String dir) {
		Play player = new Play(dir, false, 0);
		player.start();
	}

	/**
	 * plays the sound from directory and repeats it in each delay
	 * @param dir 
	 * @param delay
	 */
	public static void playAndRepeat(String dir, int delay) {
		Play player = new Play(dir, true, delay);
		player.start();
	}

}
