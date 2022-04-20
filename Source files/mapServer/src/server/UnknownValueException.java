package server;

/**
 * Classe che modella le eccezioni che si verificano per un input errato dell'utente. Estende la classe Exception
 */
public class UnknownValueException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore di classe. Richiama il costruttore della superclasse.
	 */
	public UnknownValueException(){
		super("Error acquiring values.");
	}
	
	/**
	 * Costruttore di classe. Richiama il costruttore della superclasse con una stringa errore in input.
	 * 
	 * @param errore		stringa contenente un messaggio d'errore personalizzato.
	 */
	public UnknownValueException(String errore){
		super(errore);
	}
}