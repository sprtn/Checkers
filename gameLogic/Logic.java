package gameLogic;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import clientAndServer.ClientServer;
import clientAndServer.P2P;
import graphics.Display;
import graphics.EndScreen;
import graphics.Playboard;

/**
 * @author bizgraf16
 * The Logic-class deals with applying the rules of the game,
 * as well as validation, player-changes and game state.
 */

public class Logic {
	
	public static Component 
		curComp, prevComp, jumpedComp;
	public static String 
		prevName;
	public static int 
		prevPosX, prevPosY, 
		player1 = 1, player2 = 2, curPlayer, 
		winner = -1, 
		ydiff, xdiff, 
		pieceJumped, prevPiece;
	private static boolean 
		bool, 
		jumping, 
		multipleJump;
	public static boolean 
		win = false;
	PiecePlacer pp = new PiecePlacer();
	MouseEvents me = new MouseEvents();
	public static int[][] arr;
	public static final int
		EMPTY = 0, RED_PAWN = 1, BLACK_PAWN = 2, RED_KING = 3, BLACK_KING = 4;
	
	/**
	 * The constructor calls for a new int array to be constructed.
	 * Then it calls the newGame function which initializes a new game
	 * by setting the array to the starting position.
	 */
	
	public Logic() {
		arr = new int [8][8];
		createNewGame();
	}
	
	/**
	 * Changes the player to the other player.
	 */
	
	public static void changePlayer() {
		variableReset();
		if (curPlayer == 1)
			curPlayer = player2;
		else
			curPlayer = player1;
	}
	
	/**
	 * This function validates whether the king can jump.
	 * The reason it is split up is for testing purposes
	 * and for readability. The three if-statements could
	 * easily be on one line, with &&'s between them.
	 * 
	 * @param posx		The x position of the place the player tries to jump to
	 * @param posy		The y position of the place the player tries to jump to
	 * @param change	Change is a value that shows whether a piece can move up or 
	 * 					down on the x axis. This validation gives pieces 3 and 4 
	 * 					the permission to jump "backwards"..
	 * @return			Returns whether the attempted move is legal.
	 */

	public boolean kingJumping(int posx, int posy, int change) {
		if (((prevPiece == 3 || prevPiece == 4) && jump(posx, posy, prevPiece)) && (prevPosX == posx + (change * -2)) && (prevPosY == posy - 2 || prevPosY == posy + 2)) {
			return true;
		} else
			return false;
	}
	
	/**
	 * pieceAt returns the id of the row/col position in arr.
	 * 
	 * @param row		x axis
	 * @param col		y axis
	 * @return			returns the ID.
	 */

	public int returnPieceAt(int row, int col) {
		return arr[row][col];
	}
	
	/**
	 * Sets the array to the starting position
	 */

	public void createNewGame() {
		for(int n = 0; n < 8; n++){
			for(int j = 0; j < 8; j++){
				if ( n % 2 == j % BLACK_PAWN ) {
					if (n < 3)
						arr[n][j] = RED_PAWN;
					else if (n > 4)
						arr[n][j] = BLACK_PAWN;
					else
						arr[n][j] = EMPTY;
		            } 
				else
					arr[n][j] = EMPTY;
			}
		}
	}
	
	/** 
	 * applyMove is a dynamic function which can work with both players' inputs.
	 * We start with checking whether there is a jumped piece, and clear that
	 * from the temporary memory we have stored before we make the move
	 * on the board by changing the new component into our new piece
	 * and clearing our old component and any "taken" components.
	 * 
	 * @param name		Name of the label
	 * @param posx		X position of the label
	 * @param posy		Y position of the label
	 * @param piece		Piece ID
	 */

	public void applyMove(String name, int posx, int posy, int piece) {
		if (curPlayer == 1)
			System.out.println("Moving a black piece");
		else
			System.out.println("Moving a red piece");
		if (jumpedComp != null && jumping == true) {
			((JLabel) jumpedComp).setIcon(Playboard.empty); 
			arr[prevPosX + xdiff][prevPosY + ydiff] = 0;
			
			if ((curPlayer == 1 && !((prevPosX + (xdiff * 4)) >= 8) && !((prevPosX + (xdiff * 4)) <= -1) && !((prevPosY + (ydiff * 4)) <= -1) && !((prevPosY + (ydiff * 4)) >= 8) && ((contJumpInt(3) == 2 || contJumpInt(3) == 4)) && contJumpInt(4) == 0)) {
				changeBackground(curComp, null, false);
				doubleJump();
			} else if (curPlayer == 2 && !((prevPosX + (xdiff * 4)) <= -1) && !((prevPosX + (xdiff * 4)) >= 8) && !((prevPosY + (ydiff * 4)) <= -1) && !((prevPosY + (ydiff * 4)) >= 8) && (contJumpInt(3) == 1 || contJumpInt(3) == 3) && contJumpInt(4) == 0) {
				changeBackground(curComp, null, false);
				doubleJump();
			}
				
		}
		if (!multipleJump) {
			changeBackground(curComp, null, false);
			pp.setPiece(name, posx, posy, piece);
			System.out.println("Red is done, Black's up next.");
		}
	}
	
	/**
	 * The double jump function invokes a mandatory double jump in the direction
	 * the player already is jumping if possible. The method has validation checking
	 * which piece is landing on what row. If a pawn lands on the "final row" (0 or 7),
	 * it becomes a king. If a king lands on the "final row" it remains a king.
	 */

	public void doubleJump() {
		multipleJump = true;
		int pieceJumping = arr[prevPosX][prevPosY];
		Logic.arr[prevPosX][prevPosY] = 0;
		Logic.arr[prevPosX + (xdiff * 2)][prevPosY + (ydiff * 2)] = 0;
		Logic.arr[prevPosX + (xdiff * 3)][prevPosY + (ydiff * 3)] = 0;
		if (prevPosX + (xdiff * 4) == 7 || prevPosX + (xdiff * 4) == 0) {
			if (pieceJumping < 3) // If the piece is a pawn
				setPieceJumpingToArr(pieceJumping + 2); // Make a king
			else
				setPieceJumpingToArr(pieceJumping); // Set the king on row 0 or 7
		} else
			setPieceJumpingToArr(pieceJumping); // Set the piece
		P2P.pieceMoveChanger();
		for (int i = 0; i < 8; i++){
			System.out.println();
			for (int j = 0; j < 8; j++)
				System.out.print(arr[i][j]);
		}
	}

	public void setPieceJumpingToArr(int pieceJumping) {
		Logic.arr[prevPosX + (xdiff * 4)][prevPosY + (ydiff * 4)] = pieceJumping;
	}

	public int contJumpInt(int i) {
		return arr[prevPosX + xdiff * i][prevPosY + ydiff * i];
	}
	
	
	
	/**
	 * Ends the game
	 */

	public static void gameEnder() {
		System.out.println("Player " + curPlayer + " won!");
		clientAndServer.P2P.yourTurn = false;
		graphics.EndScreen.showEndGameWindow();
	}
	
	/**
	 * Starts the next turn if the current game hasn't ended.
	 */
	
void next() {
	checkWinCondition();
	new ClientServer(arr);
	if (!win) {
		changePlayer();
		P2P.yourTurn = false;
		P2P.displayTextChanger(Display.txt1, "Opponents turn!", Color.lightGray);
	} else {
		gameEnder();
		EndScreen.showEndGameWindow();
	if (curPlayer == 1)
		EndScreen.infoWindow.setText("<html><center><b><font size=25 color=black> Red player won! </font></b></center></html>");
	else
		EndScreen.infoWindow.setText("<html><center><b><font size=25 color=black> Black player won! </font></b></center></html>");
		P2P.displayTextChanger(Display.txt1, "Game has ended!", Color.blue);
		P2P.yourTurn = false;
	}
}
	
	/**
	 * Checks whether a player has met the win condition
	 * on the play board and sets a global variable, win,
	 * to true or false.
	 */

	public static void checkWinCondition() {
		win = false;
		if (curPlayer == 1 && arrContains(2, 4))
			win = true;
		else if (curPlayer == 2 && arrContains(1, 3))
			win = true;
		if (curPlayer == 2 && !gameLogic.Logic.arrContains(2, 4))
			P2P.gameLost = true;
		else if (gameLogic.Logic.curPlayer == 1 && !gameLogic.Logic.arrContains(1, 3))
			P2P.gameLost = true;
		if (P2P.gameLost == true) {
			if (gameLogic.Logic.curPlayer == 1)
				EndScreen.infoWindow.setText("<html><center><b><font size=25 color=black> Black player won! </font></b></center></html>");
			else
				EndScreen.infoWindow.setText("<html><center><b><font size=25 color=black> Red player won! </font></b></center></html>");
		}
		else
			win = false;
	}
	
	/**
	 * Checks whether the array contains the specified pieces.
	 * Used for checking the win condition.
	 * 
	 * @param i		pawn
	 * @param j		king
	 * @return		returns false if the array contains one of the pieces, else it returns true.
	 */

	public static boolean arrContains(int i, int j) {
		Boolean bool = false;
		for (int q = 0; q < graphics.Playboard.rows; q++)
			for (int y = 0; y < graphics.Playboard.cols; y++) {
				if (Logic.arr[q][y] == i || Logic.arr[q][y] == j) {
					System.out.println("Array contains " + i + " or " + j);
					bool = false;
					return bool;
				} else
					bool = true;;
			}
		return bool;
	}
	
	/**
	 * Validates whether the move is legal or not.
	 * 
	 * @param posx		x position of the position the player wants to move to.
	 * @param posy		y position of the position the player wants to move to.
	 * @param pawn		the imageIcon pawn
	 * @param king		the imageIcon king
	 * @param santa		the imageIcon santa
	 * @param sKing		the imageIcon sKing
	 * @return			returns whether the last piece clicked was one of the imageIcons you have put in as arguments.
	 */

	public boolean validate(int posx, int posy, ImageIcon pawn, ImageIcon king, ImageIcon santa, ImageIcon sKing){
		if (prevComp != null ){
			return (((JLabel) prevComp).getIcon().equals(pawn) || ((JLabel) prevComp).getIcon().equals(king)) && checkLegalMove(posx, posy) || (((JLabel) prevComp).getIcon().equals(santa)) || ((JLabel) prevComp).getIcon().equals(sKing) && checkLegalMove(posx, posy) ;
		}
		else
			return false;
	}
	
	/**
	 * Resets global variables, ready for use next turn.
	 */

	public static void variableReset() {
		Logic.pieceJumped = 0;
		Logic.bool = false;
		Logic.curComp = null;
		Logic.prevComp = null;
		Logic.jumpedComp = null;
		Logic.ydiff = 0;
		Logic.xdiff = 0;
		Logic.jumping = false;
		multipleJump = false;
		
	}
	
	/**
	 * Prepares the turn for the players, applying highlights.
	 * 
	 * @param posx		x position of the label clicked
	 * @param posy		y position of the label clicked
	 * @param piece		ID of the piece clicked
	 * @param color		Color of highlight
	 */

	public void prepare(int posx, int posy, int piece, String color) {
		Color clr;
		if (color == "black")
			clr = Color.black;
		else
			clr = Color.red;
		if (prevComp != null)
			changeBackground(prevComp, null, false);
		changeBackground(curComp, clr, true);
		storeValues(posx, posy, piece);
	}

	/**
	 * Checks whether the move is valid
	 * 
	 * @param posx		x position of the label the player wants to move to
	 * @param posy		y position of the label the player wants to move to
	 * @return
	 */

	boolean checkLegalMove(int posx, int posy) {
		Boolean bool;
		int change;
		change = curPlayer > 1 ? (change = 1) : (change = -1);
		if (kingJumping(posx, posy, change)){
			bool = true;
		} else if (prevPosX == posx + change && (prevPosY == posy - 1 || prevPosY == posy + 1)){
			System.out.println("Normal move");
			bool = true;
		} else if (jump(posx, posy, prevPiece) && (prevPosX == posx + (change * 2) && (prevPosY == posy - 2 || prevPosY == posy + 2))) {
			bool = true;
		} else if (((JLabel)prevComp).getIcon().equals(Playboard.rKing) || ((JLabel)prevComp).getIcon().equals(Playboard.bKing) || ((JLabel)prevComp).getIcon().equals(Playboard.blackSKing) || ((JLabel)prevComp).getIcon().equals(Playboard.redSKing)){
			change *= (-1);
			if (prevPosX == posx + change && (prevPosY == posy - 1 || prevPosY == posy + 1)){
				bool = true;
			} else
				bool = false;
		} else if (prevPiece == 4 && (prevPosX == posx + 1 || prevPosX == posx -1) && (prevPosY == posy - 1 || prevPosY == posy +1)) { // King moves
			change *= (-1);
			if (prevPosX == posx + change && (prevPosY == posy - 1 || prevPosY == posy + 1))
				bool = true;
			else
				bool = false;
		} else {
			bool = false;
		}
		return bool;
	}

	/**
	 * Validates whether the player is jumping, and whether the player can jump over the piece the player tries to jump over.
	 * 
	 * @param posx			x position of the label clicked
	 * @param posy			y position of the label clicked
	 * @param prevPiece		the piece that is doing the jumping
	 * @return				Returns true or false. Valid or invalid jump.
	 */
	
	boolean jump(int posx, int posy, int prevPiece) {
		bool = false;
		xdiff = (posx - prevPosX) / 2; 
		ydiff = (posy - prevPosY) / 2;
		if ((xdiff < 2 || xdiff > -2 && ydiff < 2) && prevPiece == 1) {
			System.out.println("Norm jump 1 " + prevPiece);
			pieceJumped = Logic.arr[prevPosX + xdiff][prevPosY + ydiff];
			jumpedComp =  Playboard.cArr[prevPosX + xdiff][prevPosY + ydiff];
			if (curPlayer == 1 && (pieceJumped == 2 || pieceJumped == 4))
				bool = true;
			else if (curPlayer == 2 && (pieceJumped == 1 || pieceJumped == 3))
				bool = true;
			else
				bool = false;
		} else if ((xdiff < 2 || xdiff > -2 && ydiff < -2) && prevPiece == 2) {
			System.out.println("Norm jump 2 " + prevPiece);
			pieceJumped = Logic.arr[prevPosX + xdiff][prevPosY + ydiff];
			jumpedComp =  Playboard.cArr[prevPosX + xdiff][prevPosY + ydiff];
			if (curPlayer == 2 && (pieceJumped == 1 || pieceJumped == 3))
				bool = true;
			else
				bool = false;
		} else if (prevPiece == 3 || prevPiece == 4 && (xdiff < 2 || xdiff > -2 && ydiff < 2 || ydiff > -2)) {
			System.out.println("King jump " + prevPiece);
			pieceJumped = Logic.arr[prevPosX + xdiff][prevPosY + ydiff];
			jumpedComp =  Playboard.cArr[prevPosX + xdiff][prevPosY + ydiff];
			if (curPlayer == 1 && (pieceJumped == 2 || pieceJumped == 4)) {
				System.out.println(curPlayer + " Player 1 jumping over " + pieceJumped);
				bool = true;
			} else if (curPlayer == 2 && (pieceJumped == 1 || pieceJumped == 3)){
				System.out.println(curPlayer + " jumping over " + pieceJumped);
				bool = true;
			} else 
				bool = false;
		}
		jumping = bool;
		return bool;
	}
	
	/**
	 * Changes background color depending on the arguments
	 * 
	 * @param component		The JLabel that is to be changed
	 * @param col			The color the JLabel will be changed to
	 * @param b				true or false on "set opaque"
	 */

	void changeBackground(Component component, Color col, boolean b) {
		((JLabel) component).setBackground(col);
		((JLabel) component).setOpaque(b);
	}
	
	/**
	 * Stores the current values as the previous values, ready for new input on click number 2.
	 * 
	 * @param posx		x position
	 * @param posy		y position
	 * @param piece		piece id
	 */

	void storeValues(int posx, int posy, int piece) {
		prevPosX = posx;
		prevPosY = posy;
		prevName = "" + prevPosX + "" + prevPosY;
		prevComp = (JLabel) curComp;
		prevPiece = piece;
		piece = Logic.arr[posx][posy];
	}
	
	/**
	 * returns true if the current player is 2 and the JLabel clicked holds no piece.
	 * 
	 * @param piece		the piece that is clicked.
	 * @return			returns true if the move is valid.
	 */

	boolean finishBlackMoves(int piece) {
		return (piece == 0 && curPlayer == 2);
	}
	
	/**
	 * returns true if the current player is 1 and the JLabel clicked holds no piece.
	 * 
	 * @param piece		the piece that is clicked.
	 * @return			returns true if the move is valid.
	 */

	boolean finishRedMoves(int piece) {
		return (piece == 0 && curPlayer == 1);
	}
	
	/**
	 * Checks whether the selection is a valid piece.
	 * 
	 * @param piece		The piece selected
	 * @return			returns true if the selection is valid.
	 */

	boolean moveBlack(int piece) {
		return ((piece == 2 || piece == 4) && curPlayer == 2);
	}
	
	/**
	 * Checks whether the selection is a valid piece.
	 * 
	 * @param piece		The piece selected
	 * @return			returns true if the selection is valid.
	 */

	boolean moveRed(int piece) {
		return ((piece == 1 || piece == 3) && curPlayer == 1);
	}
	
	/**
	 * This is where the actual game runs its validations and
	 * highlights. This is the "main method" for the game logic.
	 * 
	 * @param e		The mouse clicked event from gameLogic.MouseEvents
	 */

	public void play(MouseEvent e) {
		Logic.curComp = (JLabel) e.getComponent();
		String name = Logic.curComp.getName();
		System.out.println(name);
		int posx = (int) name.charAt(0) - 48;
		int posy = (int) name.charAt(1) - 48;
		int piece = Logic.arr[posx][posy]; 
		
		if (moveRed(piece)) { // Checks turn && target piece is pawn or king
			jumping = false;
			prepare(posx, posy, piece, null);
		} else if (moveBlack(piece)) { // Checks turn && target piece is pawn or king
			jumping = false;
			prepare(posx, posy, piece, "black");
		} else if (validate(posx, posy, Playboard.red, Playboard.rKing , Playboard.redSanta, Playboard.redSKing) && finishRedMoves(piece)){ // Checks that previous component was pawn or king and that it's a legal move
			applyMove(name, posx, posy, piece);
			next();
		} else if (validate(posx, posy, Playboard.black, Playboard.bKing, Playboard.blackSanta, Playboard.blackSKing) && finishBlackMoves(piece)){ // Checks that previous component was pawn or king and that it's a legal move
			applyMove(name, posx, posy, piece);
			next();
		} else {
			System.out.println("Invalid move");
		}
	}
	
}

