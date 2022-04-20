package server;

//imports

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

/**
 * Classe che modella un server.
 */
public class MultiServer {
	private int PORT = 8080;
	
	/**
	 * Costruttore di classe. Inizializza la porta ed invoca run(). Solleva IOException se si dovesse verificare un problema di lettura/scrittura sul socket.
	 * 
	 * @param port			porta del server da utilizzare.
	 * 
	 * @throws IOException	sollevata quando si verifica un problema di lettura/scrittura sul socket.
	 */
	public MultiServer(int port) throws IOException {
		PORT=port;
		run();
	}
	
	/**
	 * Istanzia un oggetto della classe ServerSocket che pone in attesa di richiesta di connessioni da parte del client. Ad ogni nuova richiesta di connessione si istanzia ServerOneClient. Solleva IOException.
	 * 
	 * @throws IOException	sollevata quando si verifica un problema di lettura/scrittura sul socket.
	 */
	public void run() throws IOException{
		ServerSocket s=new ServerSocket(PORT);
		System.out.println("Started: " + s);
		try {
			while(true) {
				Socket socket=s.accept();
				try {
					System.out.println("Connessione accettata: "+socket);
					new ServerOneClient(socket);
					}
					catch(IOException e) {
						socket.close();
					}
			}
		}finally {
			s.close();
		}
	}

}
