package graphics;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Play extends Thread {
	String dir;
	boolean repeat;
	int delay;

	/**
	 * Play the constructor for the class Play
	 * 
	 * @param dir
	 *            the directory for the sound
	 * @param repeat
	 *            sets if the sound should be repeated or not
	 * @param delay
	 *            if the sound should be repeated it clarifies the time of each
	 *            play
	 */
	public Play(String dir, boolean repeat, int delay) {
		this.dir = dir;
		this.repeat = repeat;
		this.delay = delay;
	}

	/**
	 * makePlayer generates the Player for the sound in directory
	 * 
	 * @param dir
	 *            the directory for the sound
	 * @return returns the generated Player
	 */
	static Player makePlayer(String dir) {
		try {
			File file = new File(dir);
			FileInputStream fis;
			fis = new FileInputStream(file);

			BufferedInputStream bis = new BufferedInputStream(fis);
			return new Player(bis);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	Player mp3;

	/**
	 * the run method for the thread which plays the sound and repeats it
	 */
	public void run() {
		do {
			try {
				mp3 = makePlayer(dir);
				mp3.play();
				sleep(delay);
				mp3.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (repeat);
	}

	public void close() {
		mp3.close();
		repeat = false;
	}
}
