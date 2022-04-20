package database;

/**
 * Modella la restituzione di un resultset vuoto. Estende la classe Exception.
 */
public class EmptySetException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore di classe. Richiama il costruttore della superclasse.
	 */
	public EmptySetException(){
		super("Errore, set vuoto");
	}
	
	/**
	 * Costruttore di classe. Usa un argomento di tipo String per creare frasi personalizzate.
	 * 
	 * @param error		stringa contenente un messaggio d'errore personalizzato.
	 */
	public EmptySetException(String error){
		super(error);
	}
}
