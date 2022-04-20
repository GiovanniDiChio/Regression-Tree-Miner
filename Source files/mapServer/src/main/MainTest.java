package main;

//imports

import java.io.IOException;
import data.TrainingDataException;
import server.MultiServer;

/**
 * Classe main del server.
 */
public class MainTest {
	
	/**
	 * Avvia il server sulla porta 8080. Solleva TrainingDataException se si verificano problemi nel caricamento dell'albero e IOException per problemi di lettura/scrittura col client.
	 * 
	 * @throws TrainingDataException		sollevata se si verificano problemi nel caricamento dell'albero.
	 * @throws IOException					sollevata se si verificano problemi di lettura/scrittura col client.
	 */
	public static void main(String[] args) throws TrainingDataException, IOException{
				
		MultiServer server=new MultiServer(8080);
		server.run();
	}
}
