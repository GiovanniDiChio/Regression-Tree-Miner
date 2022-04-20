package data;

/**
 * Classe che estende la classe Attribute e rappresenta un attributo continuo.
 */
public class ContinuousAttribute extends Attribute {
	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore di classe che richiama il costruttore della super-classe Attribute.
	 * 
	 * @param name	nome dell'attributo continuo.
	 * @param index	indice dell'attributo continuo.
	 */
	ContinuousAttribute(String name, int index) {
		super(name, index);
	}
			
}
