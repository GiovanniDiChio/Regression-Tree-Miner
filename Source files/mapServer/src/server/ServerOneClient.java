package server;

//imports

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.Data;
import data.TrainingDataException;
import tree.RegressionTree;

/**
 * Classe che modella la singola istanza di un server sfruttando il multithreading. Estende la classe Thread.
 */
class ServerOneClient extends Thread {
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	/**
	 * Costruttore di classe. Inizializza gli attributi socket, in e out. Avvia il Thread. Solleva IOException.
	 * 
	 * @param s				socket su cui effettuare la connessione.
	 * 
	 * @throws IOException	sollevata quando si verifica un errore di lettura/scrittura sul socket.
	 */
	ServerOneClient(Socket s) throws IOException{
		socket=s;
		in=new ObjectInputStream(s.getInputStream());
		out=new ObjectOutputStream(s.getOutputStream());
		start();
	}
	
	/**
	 * Riscrive il metodo run della superclasse Thread al fine di gestire le richieste del client in modo da rispondere a quest'ultime.
	 * Effettua delle scritture/letture sugli stream in base alle scelte effettuate dall'utente. Permette la lettura di un RegressionTree da database o da disco. Permette di effettuare la predizione dei valori in base all'albero caricato.
	 */
	public void run() {
		try {
			while (true) {
				int decision=(int)in.readObject();
				String tableName = in.readObject().toString();
				RegressionTree tree=null;
				Data trainingSet=null;
				if(decision==0) {
					
					try{
						trainingSet= new Data(tableName);
					}
					catch(TrainingDataException e){
						System.out.println(e);
						out.writeObject(e.toString());
						return;
						}
					out.writeObject("OK");
					if((int)in.readObject()!=1)
					{
						System.out.println("Error acquiring data.");
						return;
					}
					tree=new RegressionTree(trainingSet);
					tree.salva(tableName+".dmp");										//salvo una copia sul disco

					out.writeObject(tree.getRules());
					out.writeObject(tree.toString());

				}
				else {
					tree=RegressionTree.carica(tableName+".dmp");
					out.writeObject(tree.getRules());
					out.writeObject(tree.toString());
				}
			do {
				if((int)in.readObject()!=3)
					return;
				else {
					out.writeObject("QUERY");
					out.writeObject(tree.predictClass(in, out));
				}
			}while(in.readObject().equals('Y'));
			}
		} catch(IOException e) {
			System.out.println("Client closed the connection.");
		} catch(ClassNotFoundException e){
			System.out.println("ClassNotFound Exception");		
		}catch (UnknownValueException e) {
			System.out.println("UnknownValue Exception");
			
		}finally {
		
			try {
				socket.close();
			} catch(IOException e) {
				System.err.println("Socket not closed");
			}
		}
	}


}
