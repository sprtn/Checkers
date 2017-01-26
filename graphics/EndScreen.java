package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

/**
 * @author bizgraf16
 * 
 * This class is called when the game
 * is finished and a winner is declared.
 * 
 * A JFrame pops up which states the winner of the game
 * 
 */

public class EndScreen extends JFrame{
		
	private static final long serialVersionUID = 1L;
	static JFrame window = new JFrame();
	public static JTextPane infoWindow = new JTextPane();

	/**
	 * Activates the end-game screen
	 */
	
	public static void showEndGameWindow() {
		editInfoWindowSettings();
		JButton accept = new JButton("Close game");
		accept.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				Object arg = e.getSource();
				if (arg==accept) {
					System.exit(0);
				}
			}
		});
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		int screenHeight = d.height;
		int screenWidth = d.height;
		editWindowSettings(accept, panel, screenHeight, screenWidth);
		
	}
	
	/**
	 * Specifies the settings for the infoWindow
	 */

	public static void editInfoWindowSettings() {
		infoWindow.setEditable(false);
		infoWindow.setContentType("text/html");
		infoWindow.setText("");
		infoWindow.setBackground(new Color(238,238,238));
	}
	
	/**
	 * Specifies the settings for the window.
	 * 
	 * @param accept			JButton
	 * @param panel				JPanel
	 * @param screenHeight		Preferred height
	 * @param screenWidth		Preferred width
	 */

	public static void editWindowSettings(JButton accept, JPanel panel, int screenHeight, int screenWidth) {
		window.setLocation(screenWidth / 4, screenHeight / 4);
		window.setVisible(true);
		window.setSize(250, 150);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(panel);
		window.add(infoWindow, BorderLayout.NORTH);
		window.add(accept, BorderLayout.SOUTH);
	}	
	
}