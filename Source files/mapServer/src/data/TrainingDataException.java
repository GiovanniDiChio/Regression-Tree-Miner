package data;

/**
 * Classe che modella le eccezioni sull'acquisizione degli esempi. Estende la classe Exception.
 */
public class TrainingDataException extends Exception{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore di classe. Invoca il costruttore della superclasse con la stringa in input.
	 * 
 	 * @param error		stringa contenente un messaggio d'errore personalizzato.
	 */
	public TrainingDataException(String error) {
		super(error);
	}

}