package mapClient;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import gui.GUI;

/**
 * Classe main del client.
 */
public class MainTest {

	/**
	 * Setta il look and feel dell'interfaccia grafica e la avvia con i parametri del server a cui connettersi in input.
	 * 
	 * @param args		ip e porta del server a cui connettersi.
	 */
	public static void main(String[] args){
		
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
					} catch (Exception e) {	}
				break;
			}
		}
        
		new GUI(args);

	}

}
