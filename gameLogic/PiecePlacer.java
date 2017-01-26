package gameLogic;

import java.awt.Component;

import javax.swing.JLabel;

import clientAndServer.P2P;
import graphics.Playboard;

/**
 * @author bizgraf16
 * 
 * This class places the pieces depending on their position
 *
 */

public class PiecePlacer {

	/**
	 * setPiece sets the icons of the chosen location
	 * and updates the array.
	 * 
	 * @param name
	 * @param posx
	 * @param posy
	 * @param piece
	 */
	
	public void setPiece(String name, int posx, int posy, int piece) {
		Component q = Playboard.cArr[posx][posy];
		
		if (posx != 7 && posx != 0) {
			((JLabel) q).setIcon(((JLabel) Logic.prevComp).getIcon());
			Logic.arr[posx][posy] = Logic.prevPiece;
			// ((JLabel) curComp).setIcon(((JLabel) prevComp).getIcon());
		} else {
			if (Logic.curPlayer == 1 && !P2P.christmas){
				((JLabel) q).setIcon(Playboard.rKing);
				Logic.arr[posx][posy] = Logic.RED_KING;
			}
			else if (Logic.curPlayer == 1 && P2P.christmas) {
				((JLabel) q).setIcon(Playboard.redSKing);
				Logic.arr[posx][posy] = Logic.RED_KING;
			}
			else if (Logic.curPlayer == 2 && !P2P.christmas) {
				((JLabel) q).setIcon(Playboard.bKing);
				Logic.arr[posx][posy] = Logic.BLACK_KING;
			} else if (Logic.curPlayer == 2 && P2P.christmas) {
				((JLabel) q).setIcon(Playboard.blackSKing);
				Logic.arr[posx][posy] = Logic.BLACK_KING;
			}
		}
		((JLabel) Logic.prevComp).setIcon(Playboard.empty);
		((JLabel) Logic.curComp).setName(name);
		((JLabel) Logic.prevComp).setName(Logic.prevName);
		Logic.arr[Logic.prevPosX][Logic.prevPosY] = piece;
	}

}
