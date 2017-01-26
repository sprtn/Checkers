package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import clientAndServer.P2P;

/**
 * @author bizgraf16
 * 
 * The class WindowMaker creates a JFrame for the player's port and IP choice
 * It comes with a default port and IP which can be overridden by choice.
 */

public class WindowMaker extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	static JTextField textfield1 = new JTextField("localhost");
	static JTextField textfield2 = new JTextField("14010");
	static JFrame window = new JFrame("Connect to host");
	
	/**
	 * This constructor creates the connect JFrame/window
	 */
	
	public WindowMaker() {
			JLabel gameName = new JLabel();
			JLabel ip = new JLabel();
			JLabel port = new JLabel();
			JButton button = new JButton("Connect");
			JButton cancel = new JButton("Cancel");
			JPanel panel = new JPanel();
			
			panel.setLayout(null);
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension d = tk.getScreenSize();
			int screenHeight = d.height;
			int screenWidth = d.width;
			
			fixWindow(panel, screenHeight, screenWidth);
			setBounds(gameName, ip, port);
			fixButton(button);
			fixCancel(cancel);
			
			ip.setText("IP:");
			port.setText("Port:");
			gameName.setText("Checkers");
			
			addToPanel(gameName, ip, port, button, cancel, panel);
	}
	
	/**
	 * Adds the specified elements to the JFrame
	 * 
	 * @param gameName
	 * @param ip
	 * @param port
	 * @param button
	 * @param cancel
	 * @param panel
	 */
	
	public static void addToPanel(JLabel gameName, JLabel ip, JLabel port, JButton button, JButton cancel, JPanel panel) {
		panel.add(button);
		panel.add(cancel);
		panel.add(textfield2);
		panel.add(gameName);
		panel.add(textfield1);
		panel.add(ip);
		panel.add(port);
	}
	
	/**
	 * Fixes settings for the cancel-button.
	 * 
	 * @param cancel
	 */

	public static void fixCancel(JButton cancel) {
		cancel.setBounds(190,130,100,35);
		cancel.setBackground(new Color(255,100,90));
		cancel.setBorder(new RoundBorders(10));
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				String arg = evt.getActionCommand();
				if(arg.equals("Cancel")) System.exit(0);
			}
		});
	}
	
	/**
	 * sets the bounds for the elements.
	 * 
	 * @param gameName
	 * @param ip
	 * @param port
	 */

	public static void setBounds(JLabel gameName, JLabel ip, JLabel port) {
		gameName.setBounds(150,10,120,35);
		textfield1.setBounds(150,50,180,30);
		textfield2.setBounds(150,90,180,30);
		ip.setBounds(80,50,120,30);
		port.setBounds(80,90,120,30);
	}
	
	/**
	 * Sets the settings for the button.
	 * 
	 * @param button
	 */

	public static void fixButton(JButton button) {
		button.setBounds(50,130,100,35);
		button.setBackground(new Color(109,218,255));
		button.setBorder(new RoundBorders(10));
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ip2 = textfield1.getText();
				port1 = textfield2.getText();
				IP = Integer.parseInt(port1);
				P2P.ip = ip2;
				P2P.port = IP;
				new P2P();
				window.dispose();
			}
		});
	}
	
	/**
	 * Changes the window parameters
	 * 
	 * @param panel
	 * @param screenHeight
	 * @param screenWidth
	 */

	public static void fixWindow(JPanel panel, int screenHeight, int screenWidth) {
		window.setLocation(screenWidth / 4, screenHeight / 4);
		window.setVisible(true);
		window.setSize(350,200);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(panel);
	}
	
	static String port1;
	static String ip2;
	static int IP;

	@Override
	public void actionPerformed(ActionEvent e) {}

}
