package database;

/**
 * Classe che modella il fallimento nella connessione al database. Estende la classe Exception.
 */
public class DatabaseConnectionException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore di classe. Richiama il costruttore della superclasse.
	 */
	public DatabaseConnectionException() {
		super("Error connecting to the database.");
	}
	
	/**
	 * Costruttore di classe. Richiama il costruttore della superclasse con la stringa in input error.
	 * 
	 * @param error		stringa contenente un messaggio d'errore personalizzato.
	 */
	public DatabaseConnectionException(String error) {
		super(error);
	}
}
