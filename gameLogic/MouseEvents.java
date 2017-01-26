package gameLogic;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import clientAndServer.P2P;
import graphics.Playboard;

/**
 * @author bizgraf16
 *
 * This class checks whether you are connected to the server.
 * If you are, it runs through the moves from the class Logic.
 * If it is your turn, your mouse clicks will register.
 * If not, mouse clicking is disabled for the play board.
 */

public class MouseEvents {
	
	public MouseListener mouseEvents(final Playboard playBoard) {
		return new MouseListener(){
	
			Logic l = new Logic();
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!P2P.unableToCommunicateWithOpponent) {
					if (P2P.accepted && P2P.yourTurn) {
						l.play(e);
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}
	
			@Override
			public void mouseExited(MouseEvent e) {}
	
			@Override
			public void mousePressed(MouseEvent e) {}
	
			@Override
			public void mouseReleased(MouseEvent e) {}
		};
	}

}
