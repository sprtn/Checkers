package clientAndServer;

import java.io.IOException;

import gameLogic.Logic;

/**
 * @author bizgraf16
 * This class sends objects to the server.
 */

public class ClientServer {

	/**
	 * @param arr
	 * 
	 * ClientToServer writes the array as an object
	 * which in turn is sent to the server.
	 */

	public ClientServer(int[][] arr) {
	try {
		P2P.oos.writeObject(Logic.arr);
		P2P.oos.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}