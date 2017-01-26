package graphics;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;

import clientAndServer.P2P;
import gameLogic.Logic;

/**
 * @author bizgraf16
 * 
 * The class Display is a JFrame that contains the play board,
 * the text areas, menu, buttons and uses the listeners for
 * interaction with the menu.
 * 
 */

public class Display extends JFrame implements ActionListener, MouseListener{
	
	private static final long serialVersionUID = 1L;
	static Container playBoard;
	Playboard pb = new Playboard();
	int[][] arr = Logic.arr;
	int rows = Playboard.rows;
	int cols = Playboard.cols;
	String title = "Checkers";
	public static JTextPane txt1;
	public static JTextPane txt2;
	public static JTextPane txt3;
	static boolean christmas = false;
	
	/**
	 * The constructor function for the Display class.
	 * 
	 * Readies the frame of the board, implements action listeners,
	 * menu's and text panes.
	 */
	
	public Display() {
		playBoard = this.getContentPane();
		playBoard.setLayout(new GridLayout(rows, cols));
		pb.createBoard(playBoard);
		javax.swing.border.Border border = BorderFactory.createLineBorder(Color.black, 1);
		
		JMenuBar mb;
		JMenu m;
		JMenu m1;
		JMenu rules;
		
		rules = new JMenu("Rules");
		txt1 = new JTextPane();
		txt2 = new JTextPane();
		txt3 = new JTextPane();
		setTextPanes(border);
		mb = new JMenuBar();	
		m = new JMenu("Menu");
		m1 = new JMenu("Skins");
		editMenuItems(mb, m, m1, rules);
		windowHandler(mb, m, m1, rules);
	}
	
	/**
	 * Edits the menu items.
	 * 
	 * @param mb		The menu bar the menues are to be implemented in
	 * @param m			The first menu argument
	 * @param m1		The second menu argument
	 * @param rules		A JButton
	 */

	public void editMenuItems(JMenuBar mb, JMenu m, JMenu m1, JMenu rules) {
		m.setMnemonic(KeyEvent.VK_M);	
		m.getAccessibleContext().setAccessibleDescription("This is the main menu.");
		m1.getAccessibleContext().setAccessibleDescription("Choose skin!");
		rules.getAccessibleContext().setAccessibleDescription("Check the rules.");
		mb.add(m);
		mb.add(m1);
		mb.add(rules);
		mb.add(Box.createRigidArea(new Dimension(5,0)));
		mb.add(txt1);
		mb.add(txt2);
		mb.add(txt3);
		mb.add(Box.createHorizontalGlue());
	}
	
	/**
	 * Sets the text panes' settings.
	 * 
	 * @param border		Sets the specified argument as the border of the text panes.
	 */

	public void setTextPanes(javax.swing.border.Border border) {
		txt1.setEditable(false);
		txt1.setContentType("text/html");
		txt1.setText("<html><center><font size=4 color=black> Your turn/Opponents turn </font></center></html>");
		txt2.setEditable(false);
		txt2.setContentType("text/html");
		txt2.setText("<html><center><font size=4 color=black> You're the host! </font></center></html>");
		txt3.setEditable(false);
		txt3.setContentType("text/html");
		txt3.setText("<html><center><font size=4 color=black> Textpane 3 </font></center></html>");
		txt1.setBorder(border);
		txt2.setBorder(border);
		txt3.setBorder(border);
	}

	/**
	 * Adds the menu items and calls underlying functions
	 * 
	 * @param mb		Menu bar
	 * @param m			Menu
	 * @param m1		Menu
	 */
	
	private void windowHandler(JMenuBar mb, JMenu m, JMenu m1, JMenu rules) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.width;
		
		JMenuItem m2 = null;
		JMenuItem m3 = null;
		JMenuItem m4 = null;
		JMenuItem m5 = null;
		
		addMenuItem(m, m2, "Exit Game", "Exit Game");
		addMenuItem(m1, m3, "Normal", "Set normal pieces");
		addMenuItem(m1, m4, "Christmas", "Set christmas pieces");
		addMenuItem(rules, m5, "Check rules", "Check the rules");
		
		setMenu(mb, screenHeight, screenWidth);
		editMenuText();
	}
	
	/**
	 * Sets the specified settings for the menu
	 * 
	 * @param mb			Menu bar
	 * @param screenHeight	Screen height
	 * @param screenWidth	Screen width
	 */

	public void setMenu(JMenuBar mb, int screenHeight, int screenWidth) {
		this.setJMenuBar(mb);
		this.setLocation(screenWidth / 4, screenHeight / 4);
		this.setSize(600, 600);
		this.setResizable(false);
		this.setTitle(title);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Adds a menu item into the specified menu
	 * 
	 * @param m				The menu that is to be appended to
	 * @param menuItem		The menu item that is to be appended into the menu
	 * @param string		The argument for the new JMenuItem
	 * @param string2		The argument for setAccessibleDescription
	 */

	public void addMenuItem(JMenu m, JMenuItem menuItem, String string, String string2) {
		menuItem = new JMenuItem(string);
		menuItem.getAccessibleContext().setAccessibleDescription(string2);
		menuItem.addActionListener(this);
		m.add(menuItem);
	}
	
	/**
	 * Edits the text in the menu text-panes.
	 */

	private void editMenuText() {
		if (P2P.host)
			clientAndServer.P2P.displayTextChanger(Display.txt2, "You are hosting!\n" + "You have the red pieces!", new Color(127, 255, 212));
		else
			clientAndServer.P2P.displayTextChanger(Display.txt2, "You have the black pieces!", new Color(127, 255, 212));
		if (P2P.yourTurn)
			clientAndServer.P2P.displayTextChanger(Display.txt1, "Your turn!" + "You are the red pieces!", Color.green);
		else
			clientAndServer.P2P.displayTextChanger(Display.txt1, "Opponent's turn!", Color.lightGray);
		if (P2P.accepted)
			clientAndServer.P2P.displayTextChanger(Display.txt3, "Connected!", Color.green);
		else {
			clientAndServer.P2P.displayTextChanger(Display.txt3, "No opponent", new Color(255, 106, 106));
			clientAndServer.P2P.displayTextChanger(Display.txt1, "Waiting for opponent", new Color(255, 106, 106));
		}
	}
	
	/**
	 * Calls functions when the action is called.
	 */
	
	public void actionPerformed(ActionEvent evt) {
		String arg = evt.getActionCommand();
		if(arg.equals("Exit game")) 
			System.exit(0);
		else if(arg.equals("Normal")) {
			P2P.christmas = false;
		 	clientAndServer.P2P.pieceMoveChanger();
		}
		else if(arg.equals("Christmas")) {
			P2P.christmas = true;
			clientAndServer.P2P.pieceMoveChanger();
		}
		else if(arg.equals("Check rules")){
			new  Rules();
		}
	}

@Override
public void mouseClicked(MouseEvent e) {}

@Override
public void mousePressed(MouseEvent e) {}

@Override
public void mouseReleased(MouseEvent e) {}

@Override
public void mouseEntered(MouseEvent e) {}

@Override
public void mouseExited(MouseEvent e) {}

}