package database;


/**
 * Classe che modella le colonne di una tabella di un database relazionale.
 */
public class Column{
	private String name;
	private String type;

	/**
	 * Costruttore di classe. Avvalora il nome e tipo della colonna.
	 * 
	 * @param name	nome della colonna.
	 * @param type	tipo della colonna ("string" o "number").
	 */
	Column(String name,String type){
		this.name=name;
		this.type=type;
	}
	
	/**
	 * Restituisce il nome della colonna.
	 * 
	 * @return	nome della colonna.
	 */
	public String getColumnName(){
		return name;
	}
	
	/**
	 * Restituisce true se il tipo della colonna è "number" altrimenti false.
	 * 
	 * @return	true se il tipo è "number", false altrimenti.
	 */
	public boolean isNumber(){
		return type.equals("number");
	}
	
	/**
	 * Concatena in una stringa le informazioni della colonna e le restituisce.
	 * 
	 */
	public String toString(){
		return name+":"+type;
	}
}