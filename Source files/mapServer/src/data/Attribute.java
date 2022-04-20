package data;

import java.io.Serializable;

/**
 * Classe astratta che modella un generico attributo, discreto o continuo.
 * 
 */
public abstract class Attribute implements Serializable{
	private String name;
	private int index;
	private static final long serialVersionUID = 1L;

	
	/**
	 * Costruttore di classe che inizializza nome e indice dell'attributo.
	 * 
	 * @param name	nome dell'attributo.
	 * @param index	indice dell'attributo.
	 */
	Attribute(String name, int index){
		this.name=name;
		this.index=index;
	}
	
	/**
	 * Restituisce il nome dell'attributo.
	 * 
	 * @return Stringa che contiene il nome dell'attributo.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Restituisce l'indice dell'attributo.
	 * 
	 * @return indice dell'attributo.
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Restituisce le informazioni riguardanti l'attributo richiamando getName().
	 *
	 *@return	Stringa contenente il nome dell'attributo.
	 */
	public String toString() {
		return this.getName();
	}
}
