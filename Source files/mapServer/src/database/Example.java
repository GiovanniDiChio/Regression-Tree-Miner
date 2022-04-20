package database;

//imports

import java.util.ArrayList;
import java.util.List;


/**
 * Modella una transazione letta dalla base di dati. Implementa le interfacce Comparable ed Iterable.
 */
public class Example implements Comparable<Example>{
	private List<Object> example=new ArrayList<Object>();

	/**
	 * Aggiunge un Object o alla lista di Object example.
	 * 
	 * @param o		oggetto aggiunto alla lista.
	 */
	void add(Object o){
		example.add(o);
	}
	
	/**
	 * Restituisce l'Object della lista example indicizzato da i.
	 * 
	 * @param i		indice dell'oggetto da restituire.
	 * 
	 * @return		oggetto da restituire.
	 */
	public Object get(int i){
		return example.get(i);
	}

	/**
	 * Permette di comparare gli object presenti nella lista example.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int compareTo(Example ex) {
		int i=0;
		for(Object o:ex.example){
			if(!o.equals(this.example.get(i)))
				return ((Comparable)o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}
	
	/**
	 * Concatena le informazioni di ogni object di example in una stringa e la restituisce.
	 */
	public String toString(){
		String str="";
		for(Object o:example)
			str+=o.toString()+ " ";
		return str;
	}
	
}