package graphics;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * @author bizgraf16
 * 
 * This class implements a border and is called upon when
 * creating an element that requires rounded borders
 */

public class RoundBorders implements Border {
	private int r;
	
	/**
	 * Constructor function, takes radius as a parameter.
	 * 
	 * @param radius		The radius of the border.
	 */
	
	RoundBorders(int radius) {
		this.r = radius;
	}
	
	/**
	 * Takes the component as an argument and sets the insets.
	 */
	
	public Insets getBorderInsets(Component c) {
		return new Insets(this.r+1, this.r+1, this.r+2, this.r);
	}
	
	/**
	 * Sets the border to opaque
	 */
	
	public boolean isBorderOpaque() {
		return true;
	}
	
	/**
	 * Paints the border
	 */
	
	public void paintBorder(Component C, Graphics g, int x, int y, int width, int height) {
		g.drawRoundRect(x, y, width-1, height-1, r, r);
	}
}
