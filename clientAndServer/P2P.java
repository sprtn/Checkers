package clientAndServer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import gameLogic.Logic;
import graphics.Display;
import graphics.EndScreen;
import graphics.Playboard;

/**
 * @author bizgraf16
 * 
 * This class connects to the server and is
 * responsible for sending and receiving
 * data, as well as implementing them locally.
 * 
 * If there is no available connection
 * this class will create the server
 */

public class P2P implements Runnable{
	
	public static String ip = "localhost";
	public static int port = 14010;
	private Thread thread;
	
	private Socket socket;
	private static ObjectInputStream ois;

	public static ObjectOutputStream oos;
	private ServerSocket serverSocket;
	public static boolean gameLost;
	
	public static boolean host= false;
	public static boolean yourTurn = true;
	public static boolean accepted = false;
	public static boolean christmas = false;
	public static boolean unableToCommunicateWithOpponent = false;

	/**
	 * Constructor for PeerToPeer.
	 * If we aren't connected, we connect.
	 * Also starts up a new thread and starts 
	 * the JFrame for the game (Display)
	 * which in turn starts the actual game.
	 */
	
	public P2P() {
		if(!connect()) // If you do not connect to a server
			serverInitializer(); // You host the server yourself
		thread = new Thread(this, "Checkers"); 
		thread.start(); // Initializing a thread
		new Display(); // Starting the game
	}
	
	/**
	 * The run function listens for server requests
	 * and treats the received data on the client side
	 */

	@Override
	public void run() {
		if (!accepted)
			serverRequestListener();
		int[][] s;
		try {
			while ((s = (int[][]) ois.readObject()) != null) { // Validating that the received object is not null
				updateArray(s);
				gameLogic.Logic.changePlayer();
				if (!gameLogic.Logic.win) {
					yourTurn = true;
					displayTextChanger(Display.txt1, "Your turn", Color.green);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			notification();
		}
	}
	
	/**
	 * updateArray updates the received array
	 * if it is not equal to the array that
	 * already exists in Logic.
	 * 
	 * @param s		the int array retrieved from the server
	 */

	public void updateArray(int[][] s) {
		if (Logic.arr != s) {// If the array is not equal to the current array
			Logic.arr = s; // Updating the array
			gameLogic.Logic.checkWinCondition(); // Check if a player has won
			
			if (gameLogic.Logic.curPlayer == 2 && !gameLogic.Logic.arrContains(2, 4))
				gameLost = true;
			else if (gameLogic.Logic.curPlayer == 1 && !gameLogic.Logic.arrContains(1, 3))
				gameLost = true;
			if (gameLost == true) {
				if (gameLogic.Logic.curPlayer == 1)
					EndScreen.infoWindow.setText("<html><center><b><font size=25 color=black> Black player won! </font></b></center></html>");
				else
					EndScreen.infoWindow.setText("<html><center><b><font size=25 color=black> Red player won! </font></b></center></html>");
			}
			if (gameLogic.Logic.win) {
				gameLogic.Logic.gameEnder();
				if (gameLogic.Logic.curPlayer == 1)
					EndScreen.infoWindow.setText("<html><center><b><font size=25 color=black> Red player won! </font></b></center></html>");
				else
					EndScreen.infoWindow.setText("<html><center><b><font size=25 color=black> Black player won! </font></b></center></html>");
				P2P.yourTurn = false;
				displayTextChanger(Display.txt1, "Game has ended!", Color.lightGray);
			}
			pieceMoveChanger();
		}
	}

	/**
	 * Gives an error message if the connection is lost.
	 */
	
	public void notification() {
		displayTextChanger(Display.txt3, "Connection lost.", Color.red);
		accepted = false;
	}

	/**
	 * Changes the labels of the component array cArr
	 * depending on their id in the Logic.arr.
	 * 
	 * Also checks which skin is currently enabled.
	 */
	
	public static void pieceMoveChanger() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y <8; y++) {
				Component q = Playboard.cArr[x][y];
				if (q != null)
					switch(Logic.arr[x][y]) {
					case 0:
						setLabel(q, Playboard.empty);
						break;
					case 1:
						if (P2P.christmas)
							setLabel(q, Playboard.redSanta);
						else
							setLabel(q, Playboard.red);
						break;
					case 2:
						if (P2P.christmas)
							setLabel(q, Playboard.blackSanta);
						else
							setLabel(q, Playboard.black);
						break;
					case 3:
						if (P2P.christmas)
							setLabel(q, Playboard.redSKing);
						else
							setLabel(q, Playboard.rKing);
						break;
					case 4:
						if (P2P.christmas)
							setLabel(q, Playboard.blackSKing);
						else
							setLabel(q, Playboard.bKing);
						break;
				}
			}
		}
	}

	/**
	 * setLabel updates the label with the image inserted
	 * as an argument (img), and sets the background color to null.
	 * 
	 * @param q			The component that is to be effected
	 * @param img		The image to be set as the icon of the q 
	 */
	
	
	public static void setLabel(Component q, ImageIcon img) {
		((JLabel) q).setIcon(img);
		((JLabel) q).setBackground(null);
	}
	
	/**
	 * Listens for server requests and changes display text
	 */
	
	private void serverRequestListener() {
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			accepted = true;
			displayTextChanger(Display.txt3, "Connected.", Color.green);
			if (host) {
				displayTextChanger(Display.txt1, "Your turn!", Color.green);
			}
			System.out.println("Client has connected.");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Changes the display text depending on the arguments
	 * 
	 * @param t			The text pane which is to be changed
	 * @param string	The text to be set
	 * @param clr		The color of the text
	 */

	public static void displayTextChanger(JTextPane t, String string, Color clr) {
		t.setFont(new Font("Helvetica", Font.BOLD, 14));
		t.setText(string);
		t.setBackground(clr);
	}
	
	/**
	 * Connects to the server
	 * 
	 * @return		returns false if a server is not accessible, and true if the player connects.
	 */
	
	private boolean connect() {
		try {
			socket = new Socket(ip, port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			accepted = true;
			yourTurn = false;
		} catch (IOException e) {
			System.out.println("Unable to connect to the address: " + ip + ":" + port + " | Starting a server");
			return false;
		}
		System.out.println("Successfully connected to the server.");	
		return true;		
	}
	
	/**
	 * Initializes the server
	 */
	
	private void serverInitializer() { // If you do not connect, you host the server.
		try {
			serverSocket = new ServerSocket(port, 8, InetAddress.getByName(ip));
			yourTurn = true;
			host = true;
			System.out.println("You are hosting the server: " + host);
			System.out.println("Waiting for opponent");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

	

