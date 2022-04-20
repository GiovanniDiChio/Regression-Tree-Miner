package data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe che estende la classe Attribute e rappresenta un attributo discreto e implementa l'interfaccia Iterable.
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>{
	private static final long serialVersionUID = 1L;
	private Set<String> values=new TreeSet<>();
	
	/**
	 * Costruttore di classe che richiama il costruttore della super-classe Attribute e avvalora il set values con i valori discreti in input.
	 * 
	 * @param name 	nome dell'attributo discreto.
	 * @param index 	indice dell'attributo discreto.
	 * @param values 	set di oggetti String, uno per ciascun valore discreto che l'attributo può assumere.
	 */
	DiscreteAttribute(String name, int index, Set<String> values) {
		super(name,index);
		this.values=values;
	}
	
	/**
	 * Restituisce il numero di valori discreti dell'attributo, ovvero la cardinalità dell'array values.
	 * 
	 * @return numero di valori discreti dell'attributo.
	 */
	int getNumberOfDistinctValues(){
		return values.size();
	}

	/**
	 * Restituisce un iteratore che lavora sugli elementi di values.
	 * 
	 * @return iteratore che lavora sul set di stringhe values.
	 */
	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}
	
	
}


