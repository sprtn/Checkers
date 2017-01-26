package graphics;
import java.awt.Color;

import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gameLogic.Logic;
import gameLogic.MouseEvents;

/**
 * 
 * @author bizgraf16
 *
 * The main job of this  class is creating the 
 * play board and adding the pieces into JLabels. using
 * the values from Logic.arr to set our pieces unto the
 * board.
 * 
 * Adding the MouseListener event to the JLabels
 * gives us access to what piece is pressed
 * 
 * For ease of access, we also add the pieces in a
 * component array cArr, so that we can access the
 * components by their x and y coordinates, and alter them
 * with more ease.
 *
 */

public class Playboard extends JPanel {
	
private static final long serialVersionUID = 1L;
	
	public static int rows = 8;
	public static int cols = 8;
	
	int lHeight = 65;
	int lWidth = 65;
	
	JPanel panel;
	JLabel label;
	
	public Logic arr;
	public static Component[][] cArr = new Component[8][8];

	public Color col1 = new Color(153, 76, 0);
	public Color col2 = new Color(255, 153, 51);
	
	BufferedImage img = null;
	
	public static ImageIcon empty = new ImageIcon("pics/nothing.gif");
	public static ImageIcon red = new ImageIcon("pics/redPawn.gif");
	public static ImageIcon black = new ImageIcon("pics/blackPawn.gif");
	public static ImageIcon rKing = new ImageIcon("pics/redKing.gif");
	public static ImageIcon bKing = new ImageIcon("pics/blackKing.gif");
	public static ImageIcon redSanta = new ImageIcon("pics/santaRedPawn.gif");
	public static ImageIcon blackSanta = new ImageIcon("pics/santaBlackPawn.gif");
	public static ImageIcon redSKing = new ImageIcon("pics/santaRedKing.gif");
	public static ImageIcon blackSKing = new ImageIcon("pics/santaBlackKing.gif");

	MouseEvents me = new gameLogic.MouseEvents();
	
	/**
	 * Constructor function for Playboard.
	 * Calls a new array as Logic, calls a new game
	 * and sets the current player to player 1.
	 */
	
	public Playboard() {
		arr = new Logic();
		arr.createNewGame();
		Logic.curPlayer = Logic.player1;
	}
	
	/**
	 * Creates the board.
	 * 
	 * @param pane		The container that is to be filled with the game objects.
	 */
	
	public void createBoard(Container pane) {
		fixImages();
		Color temp;
		for (int i = 0; i < rows; i++){
			if (i % 2 == 0){
				temp = col1;
			}
			else {
				temp = col2;
			}
			for (int j = 0; j < cols; j++){
				panel = new JPanel();
				panel.setBackground(temp);
				if (temp.equals(col1)) {
					temp = col2;
					addLabel(i, j);
				}
				else {
					temp = col1;
				}
				pane.add(panel);
			}
		}
	}
	
	/**
	 * Calls the fixImage function for all the ImageIcons
	 */

	public void fixImages() {
		empty = fixImage(empty);
		red = fixImage(red);
		black = fixImage(black);
		rKing = fixImage(rKing);
		bKing = fixImage(bKing);
		redSanta = fixImage(redSanta);
		blackSanta = fixImage(blackSanta);
		redSKing = fixImage(redSKing);
		blackSKing = fixImage(blackSKing);
	}
	
	/**
	 * sets the label of the component depending on 
	 * the id of the piece in the integer array (Logic) 
	 * 
	 * @param x		x position
	 * @param y		y position
	 */

	public void addLabel(int x, int y) {
		switch (arr.returnPieceAt(x, y)){
			case Logic.EMPTY:
				setLabel(x, y, empty);
				break;
			case Logic.BLACK_PAWN:
				setLabel(x, y, black);
				break;
			case Logic.RED_PAWN:
				setLabel(x, y, red);
				break;
			case Logic.BLACK_KING:
				setLabel(x, y, bKing);
				break;
			case Logic.RED_KING:
				setLabel(x, y, rKing);
				break;
		}
	}
	
	/**
	 * Sets up the component of the i and j with the specified image icon
	 * 
	 * @param i			x coordinate
	 * @param j			y coordinate
	 * @param img		The imageIcon to be set.
	 */

	public void setLabel(int i, int j, ImageIcon img) {
		String labelName;
		label = new JLabel(img);
		label.addMouseListener(me.mouseEvents(this));
		labelName = "" + i + "" + j;
		label.setName(labelName);
		cArr[i][j] = label;
		panel.add(label);
	}
	
	/**
	 * Changes the ImageIcon to fit the preferred global lWidth and lHeight, as well as smooth scaling.
	 * 
	 * @param pic		The ImageIcon to be fixed.
	 * @return			Returns the fixed ImageIcon
	 */

	public ImageIcon fixImage(ImageIcon pic) {
		pic = new ImageIcon(pic.getImage().getScaledInstance(lWidth, lHeight, java.awt.Image.SCALE_SMOOTH));		
		return pic;
	}
	
}