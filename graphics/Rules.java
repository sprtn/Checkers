package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * The Rules class creates a separate JFrame for easy reading of the rules while playing the game.
 * 
 * @author bizgraf16
 */

public class Rules {
   
	/**
	 * Constructor initializes the new elements.
	 */
	
    Rules(){
    	JFrame ruleswindow = new JFrame();
        JLabel header1= new JLabel();
        JPanel panel = new JPanel();
        JTextArea area1 = new JTextArea();
        area1.setText(
        		" There are two players. " +
			    "\n They are at the opposite ends of the board. One player has red pieces, the other has black pieces." +
			    "\n The red will always start."
			    + "\n" +
			    "\n Each player starts with 12 pieces on the three rows closest to their own side." +
			    "\n The row closest to each player is called the King Row." +
			    "\n All the starting pieces can only move diagonally forward." + 
			    "\n If one piece jumps over another player's piece, that piece is removed from the board." +
			    "\n If it can jump one more time in the same direction, it is forced to do so."
			    + "\n" + 
			    "\n If a player's piece moves into the other player's King Row,"
			    + "\n" +
    			" it becomes a King." + 
			    "\n A King can move forwards AND backwards."
			    + "\n" +
    			"\n The first player to lose all of his or hers pieces loses the game.");
        designer(header1, area1);
        add(ruleswindow, header1, panel, area1);
    }
    
    /**
     * The designing method takes two parameters, the JLabel and the JTextArea.
     * The method takes the two elements and styles them.
     * 
     * @param header1
     * @param area1
     */
 
    public void designer(JLabel header1, JTextArea area1) {
        header1.setBounds(180,15, 50,10); 
        area1.setEditable(false);
        area1.setPreferredSize(new Dimension(300,600));
        area1.setForeground(Color.WHITE);
        area1.setLineWrap(true);
        area1.setOpaque(false);
        area1.setWrapStyleWord(true);
        area1.setFont(new Font("SansSerif", Font.BOLD, 16));
    }
    
    /**
     * The add method styles the parameters
     * 
     * @param ruleswindow
     * @param header1
     * @param panel
     * @param area1
     */
 
    public void add(JFrame ruleswindow, JLabel header1, JPanel panel, JTextArea area1) {
        ruleswindow.setSize(400,620);
        ruleswindow.setVisible(true);
        ruleswindow.setTitle("Rules");
        ruleswindow.setResizable(false);
        ruleswindow.add(panel);
        panel.setBackground(new Color(102,128,156));
        panel.add(header1);
        panel.add(area1);
    }
    
}