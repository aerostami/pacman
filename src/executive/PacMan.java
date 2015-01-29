package executive;

import graphics.Block;
import graphics.Board;
import graphics.Flames;
import graphics.MenuColor;
import graphics.Play;
import graphics.Sounds;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import logic.Ghost;
import logic.Man;
import logic.Pac;
import logic.PowerUps;


/**
 * 
 * @author ALI ROSTAMI - aerostami@gmail.com - The main class of the project which includes
 *         the main function to execute the game
 * 
 */
public class PacMan {
	public static JLabel p1score;
	public static JLabel p2score;
	public static JFrame jf;
	public static Block[][] blocks;
	public static int colNum = 14;
	public static int rowNum = 12;
	public static volatile int seedNum = 0;

	public static String p1name = "";
	public static String p2name = "";
	public static Pac pac;
	public static Pac pac2;
	public static Ghost red, pinky, blue;
	public static JPanel jp;

	static Play music;
	public static volatile boolean isAvailable[][];
	public static ArrayList<Block> walls = new ArrayList<Block>();

	public static int playersNum;
	public static int difficulty;
	static JPanel playerMenu = new JPanel();
	static JPanel difficultyMenu = new JPanel();
	static PowerUps powerUps;

	public static volatile boolean running = true;

	/**
	 * the main method of the project
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		jf = new Board(colNum, rowNum);
		jf.setLayout(null);
		makePlayerSelectMenu();
		makeDifficultySelectMenu();
		go2playerMenu();
		System.out.println("done");
	}

	/**
	 * restarts the game when called
	 */
	public static void restart() {
		int ans = JOptionPane.showConfirmDialog(null,
				"Do you want to play again?", "replay", 0);
		if (ans == 0) {
			Pac.pacNum=0;
			PacMan.blue = null;
			PacMan.pac2 = null;
			PacMan.pinky = null;
			PacMan.red = null;
			PacMan.seedNum = 0;
			PacMan.running = true;
//			jf = new Board(colNum, rowNum);
//			jf.setLayout(null);
			jf.remove(jp);
			jp.setVisible(false);
			go2playerMenu();
		} else
			System.exit(0);
	}

	/**
	 * starts the game and initializes all the parameters
	 */
	private static void startGame() {
		music.close();

		difficultyMenu.setVisible(false);
		jf.remove(difficultyMenu);

		running = true;
		makeMap();
		Man.hasShownMsg = false;
		Flames flames = new Flames();
		flames.start();
		jf.repaint();

		Sounds.playAndRepeat(("src/data/sounds/fire.mp3"), 25000);
		powerUps = new PowerUps();
		powerUps.start();
		addPac();
		if (playersNum == 2)
			addPac2();

		addListener();
		if (1 < difficulty) {
			addBlue();
			if (2 < difficulty) {
				addPinky();
				if (3 < difficulty)
					addRed();
			}
		}
		addListener();
	}

	/**
	 * asks the player's names and saves them @ p1name and @ p2name
	 */
	private static void getPlayersNames() {

		boolean incorect = true;
		while (incorect) {
			p1name = JOptionPane.showInputDialog("First player's name",
					"player1");

			if(p1name == null){
				p1name="";
				return;
			}
			
			if (p1name.equals("")
					|| p1name.substring(0, 1).matches("[0-9]"))
				JOptionPane.showMessageDialog(jf,
						"Invalid format for player's name!");
			else
				incorect = false;
		}
		
		if (1 < playersNum) {
			incorect = true;
			while (incorect) {
				p2name = JOptionPane.showInputDialog("Second player's name",
						"player2");

				if(p2name == null){
					p1name="";
					p2name="";
					return;
				}
				
				if ( p2name.equals("")
						|| p2name.substring(0, 1).matches("[0-9]"))
					JOptionPane.showMessageDialog(jf,
							"Invalid format for player's name!");
				else
					incorect = false;
			}

		}
		go2difficultyMenu();
	}

	// private static void killDifMenuColors() {
	//
	// }

	private static void go2difficultyMenu(){
		playerMenu.setVisible(false);
		jf.remove(playerMenu);
		jf.add(difficultyMenu);
		difficultyMenu.setVisible(true);
		jf.repaint();
	}
	/**
	 * shows the difficulty select menu and asks the difficulty 
	 * and stores it @ difficulty
	 */
	private static void makeDifficultySelectMenu() {
		
		
		difficultyMenu.setFocusable(false);
		difficultyMenu.setBounds(0, 0, 750, 800);
		difficultyMenu.setLayout(null);

		MenuColor difColor = new MenuColor(difficultyMenu);
		difColor.start();

		JButton easy = new JButton("Easy");
		easy.setBounds(215, 100, 250, 60);
		easy.setFocusable(false);
		MenuColor easColor = new MenuColor(easy);
		easColor.start();
		difficultyMenu.add(easy);
		easy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				difficulty = 1;
				startGame();
			}
		});

		JButton normal = new JButton("Normal");
		normal.setBounds(215, 160, 250, 60);
		normal.setFocusable(false);
		MenuColor normColor = new MenuColor(normal);
		normColor.start();
		difficultyMenu.add(normal);
		normal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				difficulty = 2;
				startGame();
			}
		});

		JButton hard = new JButton("Hard");
		hard.setBounds(215, 220, 250, 60);
		hard.setFocusable(false);
		MenuColor hardColor = new MenuColor(hard);
		hardColor.start();
		difficultyMenu.add(hard);
		hard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				difficulty = 3;
				startGame();
			}
		});

		JButton insane = new JButton("Insane");
		insane.setBounds(215, 280, 250, 60);
		insane.setFocusable(false);
		MenuColor insaneColor = new MenuColor(insane);
		insaneColor.start();
		difficultyMenu.add(insane);
		insane.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				difficulty = 4;
				startGame();
			}
		});

		
	}
	
	private static void go2playerMenu(){
		music = new Play("src/data/sounds/music.mp3", true, 60000);
		music.start();
		
	
		playerMenu.setVisible(true);
		jf.add(playerMenu);
		playerMenu.setFocusable(false);
		jf.repaint();
	}
	/**
	 * displays the player number select menu and saves the players number @
	 * playersNum
	 */
	private static void makePlayerSelectMenu() {

		JButton twoPlayer = new JButton("2 Players");
		JButton onePlayer = new JButton("1 Player");
		onePlayer.setBounds(95, 230, 250, 170);
		twoPlayer.setBounds(345, 230, 250, 170);
		onePlayer.setFocusable(false);
		twoPlayer.setFocusable(false);
		twoPlayer.setFocusable(true);
		onePlayer.setFocusable(true);
		playerMenu.setBounds(0, 0, 750, 800);
		playerMenu.setLayout(null);

		MenuColor playerMenuColor = new MenuColor(playerMenu);
		playerMenuColor.start();
		MenuColor onePColor = new MenuColor(onePlayer);
		onePColor.start();
		MenuColor twoPColor = new MenuColor(twoPlayer);
		twoPColor.start();

		onePlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playersNum = 1;
				getPlayersNames();
			}
		});
		twoPlayer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				playersNum = 2;
				getPlayersNames();
			}
		});
		playerMenu.add(onePlayer);
		playerMenu.add(twoPlayer);
		playerMenu.repaint();
		
		// startGame();
	}

	/**
	 * adds the second pacman to the game for the second player
	 */
	private static void addPac2() {
		pac2 = new Pac();
		Ghost.p2 = pac2;
		pac2.start();
	}

	/**
	 * adds the first pacman for the first player
	 */
	private static void addPac() {
		pac = new Pac();
		Ghost.p1 = pac;
		pac.start();
	}

	/**
	 * adds the red ghost to the game
	 */
	private static void addRed() {
		ImageIcon redImg = new ImageIcon("src/data/images/redImg.jpg");
		red = new Ghost(7, 11, redImg, 600);
		red.start();
	}

	/**
	 * adds the blue ghost to the game
	 */
	private static void addBlue() {
		ImageIcon blueImg = new ImageIcon("src/data/images/blueImg.jpg");
		blue = new Ghost(10, 11, blueImg, 700);
		blue.start();
	}

	/**
	 * adds the pink ghost to the game
	 */
	private static void addPinky() {
		ImageIcon pinkyImg = new ImageIcon("src/data/images/ghost.jpg");
		pinky = new Ghost(13, 11, pinkyImg, 800);
		pinky.start();
	}

	/**
	 * adds the listener to the JPanel and sets the direction for the pac of
	 * each player
	 */
	private static void addListener() {
		// jp.setFocusable(false);
		// jf.setFocusable(true);
		jf.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}

			// @Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_LEFT)
					pac.dir = 'L';
				else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT)
					pac.dir = 'R';
				else if (arg0.getKeyCode() == KeyEvent.VK_UP)
					pac.dir = 'U';
				else if (arg0.getKeyCode() == KeyEvent.VK_DOWN)
					pac.dir = 'D';
				if (playersNum > 1) {
					if (arg0.getKeyChar() == 'd')
						pac2.dir = 'R';
					else if (arg0.getKeyChar() == 'a')
						pac2.dir = 'L';
					else if (arg0.getKeyChar() == 'w')
						pac2.dir = 'U';
					else if (arg0.getKeyChar() == 's')
						pac2.dir = 'D';
				}
			}
		});

	}

	/**
	 * generates the main map
	 */
	private static void makeMap() {

		jp = new JPanel();
		jp.setLayout(null);
		jp.setBackground(Color.black);
		// jl.setLocation(null);
		jp.setBounds(0, 0, 750, 700);
		jf.add(jp);

		p1score = new JLabel();
		p1score.setForeground(Color.green);
		p1score.setText(p1name + " = 0");
		p1score.setBounds(450, 14, 100, 30);
		jp.add(p1score);

		if (1 < playersNum) {
			p2score = new JLabel();
			p2score.setForeground(Color.green);
			p2score.setText(p2name + " = 0");
			p2score.setBounds(165, 14, 100, 30);
			jp.add(p2score);
		}

		// ///////////////////////////////

		// menue:
		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("File");
		JMenuItem jmi1 = new JMenuItem("Save");
		JMenuItem jmi2 = new JMenuItem("close");
		jm.add(jmi1);
		jm.add(jmi2);
		jmb.add(jm);
//		jf.setJMenuBar(jmb);

		// //////////////////////////////
		jf.setLayout(null);
		jf.setResizable(false);
		isAvailable = new boolean[colNum][rowNum];
		blocks = new Block[colNum][rowNum];
		Random rand = new Random();

		for (int i = 0; i < colNum; i += 1)
			for (int j = 0; j < rowNum; j += 1) {
				Block jb = new Block();
				int r = rand.nextInt(2);
				if (r == 0) {
					seedNum += 1;
					jb.setIcon(new ImageIcon("src/data/images/seed.jpg"));
					jb.hasSeed = true;
				} else {
					jb.hasSeed = false;
					jb.setIcon(new ImageIcon("src/data/images/g3.jpg"));
				}

				blocks[i][j] = jb;
				jb.setBounds(24 + i * 47, j * 47 + 50, 47, 47);
				jp.add(jb);
				jb.setFocusable(false);
				isAvailable[i][j] = true;
			}

		/*
		 * making the walls here:
		 */

		// Img img = new Img();
		// img.addWall(blocks[1][1]);
		blocks[6][5].setIcon(new ImageIcon("src/data/images/g3.jpg"));
		blocks[6][6].setIcon(new ImageIcon("src/data/images/g3.jpg"));
		blocks[7][5].setIcon(new ImageIcon("src/data/images/g3.jpg"));
		blocks[7][6].setIcon(new ImageIcon("src/data/images/g3.jpg"));
		blocks[6][5].isRoad = false;
		blocks[6][6].isRoad = false;
		blocks[7][5].isRoad = false;
		blocks[7][6].isRoad = false;

		if (blocks[7][6].hasSeed) {
			blocks[7][6].hasSeed = false;
			seedNum -= 1;
		}

		if (blocks[6][5].hasSeed) {
			blocks[6][5].hasSeed = false;
			seedNum -= 1;
		}

		if (blocks[7][5].hasSeed) {
			blocks[7][5].hasSeed = false;
			seedNum -= 1;
		}

		if (blocks[6][6].hasSeed) {
			blocks[6][6].hasSeed = false;
			seedNum -= 1;
		}

		// blocks[1][1].setIcon(new ImageIcon("pac.png"));
		jf.setFocusable(true);
		jf.repaint();
		walls.add(blocks[1][1]);
		// walls.get(0).setIcon(new ImageIcon("rock.jpg"));
		// blocks[1][1].setIcon(new ImageIcon("rock.jpg"));
		walls.add(blocks[1][3]);
		walls.add(blocks[1][4]);
		walls.add(blocks[1][6]);
		walls.add(blocks[1][7]);
		walls.add(blocks[1][8]);
		walls.add(blocks[1][10]);
		walls.add(blocks[2][3]);
		walls.add(blocks[2][8]);
		walls.add(blocks[3][3]);
		walls.add(blocks[3][8]);
		walls.add(blocks[3][3]);
		walls.add(blocks[3][8]);
		walls.add(blocks[3][1]);
		walls.add(blocks[3][2]);
		walls.add(blocks[3][9]);
		walls.add(blocks[3][10]);
		walls.add(blocks[4][1]);
		walls.add(blocks[5][1]);
		walls.add(blocks[5][2]);
		walls.add(blocks[5][4]);
		walls.add(blocks[5][5]);
		walls.add(blocks[5][6]);
		walls.add(blocks[5][7]);
		walls.add(blocks[5][9]);
		walls.add(blocks[5][10]);
		walls.add(blocks[6][2]);
		walls.add(blocks[6][4]);
		walls.add(blocks[6][7]);
		walls.add(blocks[6][9]);
		walls.add(blocks[7][2]);
		walls.add(blocks[7][4]);
		walls.add(blocks[7][7]);
		walls.add(blocks[7][9]);
		walls.add(blocks[8][1]);
		walls.add(blocks[8][2]);
		walls.add(blocks[8][4]);
		walls.add(blocks[8][5]);
		walls.add(blocks[8][6]);
		walls.add(blocks[8][7]);
		walls.add(blocks[8][10]);
		walls.add(blocks[9][10]);
		walls.add(blocks[8][9]);
		walls.add(blocks[3][4]);
		walls.add(blocks[3][5]);
		walls.add(blocks[3][6]);
		walls.add(blocks[10][2]);
		walls.add(blocks[10][3]);
		walls.add(blocks[10][6]);
		walls.add(blocks[10][5]);
		walls.add(blocks[10][7]);
		walls.add(blocks[10][8]);
		walls.add(blocks[10][9]);
		walls.add(blocks[10][10]);
		walls.add(blocks[10][1]);
		walls.add(blocks[11][3]);
		walls.add(blocks[11][8]);
		walls.add(blocks[12][1]);
		walls.add(blocks[12][3]);
		walls.add(blocks[12][4]);
		walls.add(blocks[12][5]);
		walls.add(blocks[12][8]);
		walls.add(blocks[12][7]);
		walls.add(blocks[12][10]);

		isAvailable[1][1] = false;
		isAvailable[1][3] = false;
		isAvailable[1][4] = false;
		isAvailable[1][6] = false;
		isAvailable[1][7] = false;
		isAvailable[1][8] = false;
		isAvailable[1][10] = false;
		isAvailable[2][3] = false;
		isAvailable[2][8] = false;
		isAvailable[3][3] = false;
		isAvailable[3][8] = false;
		isAvailable[3][3] = false;
		isAvailable[3][8] = false;
		isAvailable[3][1] = false;
		isAvailable[3][2] = false;
		isAvailable[3][9] = false;
		isAvailable[3][10] = false;
		isAvailable[4][1] = false;
		isAvailable[5][1] = false;
		isAvailable[5][2] = false;
		isAvailable[5][4] = false;
		isAvailable[5][5] = false;
		isAvailable[5][6] = false;
		isAvailable[5][7] = false;
		isAvailable[5][9] = false;
		isAvailable[5][10] = false;
		isAvailable[6][2] = false;
		isAvailable[6][4] = false;
		isAvailable[6][7] = false;
		isAvailable[6][9] = false;
		isAvailable[7][2] = false;
		isAvailable[7][4] = false;
		isAvailable[7][7] = false;
		isAvailable[7][9] = false;
		isAvailable[8][1] = false;
		isAvailable[8][2] = false;
		isAvailable[8][4] = false;
		isAvailable[8][5] = false;
		isAvailable[8][6] = false;
		isAvailable[8][7] = false;
		isAvailable[8][10] = false;
		isAvailable[9][10] = false;
		isAvailable[8][9] = false;
		isAvailable[3][4] = false;
		isAvailable[3][5] = false;
		isAvailable[3][6] = false;
		isAvailable[10][2] = false;
		isAvailable[10][3] = false;
		isAvailable[10][6] = false;
		isAvailable[10][5] = false;
		isAvailable[10][7] = false;
		isAvailable[10][8] = false;
		isAvailable[10][9] = false;
		isAvailable[10][10] = false;
		isAvailable[10][1] = false;
		isAvailable[11][3] = false;
		isAvailable[11][8] = false;
		isAvailable[12][1] = false;
		isAvailable[12][3] = false;
		isAvailable[12][4] = false;
		isAvailable[12][5] = false;
		isAvailable[12][8] = false;
		isAvailable[12][7] = false;
		isAvailable[12][10] = false;

		for (Block jb : walls) {
			jb.isRoad = false;
			if (jb.hasSeed) {
				jb.hasSeed = false;
				seedNum -= 1;
			}

		}
		jf.repaint();
		jf.setFocusable(true);

	}

	public static String findPathPrivate(boolean[][] map, int sRow, int sCol,
			int dRow, int dCol) {

		class Point {
			public int row;
			public int col;

			public Point(int row, int col) {
				this.row = row;
				this.col = col;
			}
		}

		int rowCount = map.length;
		int colCount = map[0].length;

		String[][] directions = new String[rowCount][colCount];
		Point[][] parents = new Point[rowCount][colCount];
		boolean[][] visited = new boolean[rowCount][colCount];
		boolean found = false;

		LinkedList<Point> queue = new LinkedList<Point>();
		queue.add(new Point(sRow, sCol));

		while (!found && queue.size() > 0) {
			Point point = queue.poll();
			if (point.row == dRow && point.col == dCol)
				found = true;

			String[] dirList = { "up", "right", "down", "left" };
			int[] deltaRow = { -1, 0, 1, 0 };
			int[] deltaCol = { 0, 1, 0, -1 };

			for (int i = 0; i < 4; i++) {
				int cRow = point.row + deltaRow[i];
				int cCol = point.col + deltaCol[i];
				if (cRow < 0 || cRow >= rowCount || cCol < 0
						|| cCol >= colCount)
					continue;
				if (visited[cRow][cCol] || !map[cRow][cCol])
					continue;
				queue.add(new Point(cRow, cCol));
				directions[cRow][cCol] = dirList[i];
				parents[cRow][cCol] = point;
				visited[cRow][cCol] = true;
				if (cRow == dRow && cCol == dCol)
					found = true;
			}
		}

		if (!found)
			return "none";

		Point point = new Point(dRow, dCol);
		Point parent = parents[dRow][dCol];
		while (parent != null && !(parent.row == sRow && parent.col == sCol)) {
			point = parent;
			parent = parents[point.row][point.col];
		}

		String dir = directions[point.row][point.col];
		if (dir == null)
			return "none";
		else
			return dir;
	}

}
