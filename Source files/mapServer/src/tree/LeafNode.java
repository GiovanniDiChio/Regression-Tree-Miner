package tree;

import data.Data;

/**
 * Classe che modella l'entità nodo fogliare. Estende la classe Node.
 */
class LeafNode extends Node {
	private double predictedClassValue;
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore di classe. Istanzia un oggetto invocando il costruttore della superclasse e avvalore l'attributo predictedClassValue (media dei valori dell'attributo di classe nella partizione compresa tra beginExampleIndex ed endExampleIndex).
	 * 
	 * @param trainingSet			collezione di esempi di apprendimento.
	 * @param beginExampleIndex		indice nell'array del training set del primo esempio coperto dal nodo corrente.
	 * @param endExampleIndex		indice nell'array del training set dell'ultimo esempio coperto dal nodo corrente.
	 */
	LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		super(trainingSet, beginExampleIndex, endExampleIndex);
		double val=0;
		for(int i=beginExampleIndex; i<endExampleIndex+1; i++) {
			val+=trainingSet.getClassValue(i);			
		}
		predictedClassValue=val/(endExampleIndex-beginExampleIndex+1);
	}
	
	/**
	 * Restituisce il valore del membro predictedClassValue.
	 * 
	 * @return		media dei valori dell'attributo di classe.
	 */
	double getPredictedClassValue() {
		return predictedClassValue;
	}
	
	/**
	 * Restituisce il numero di split originanti del nodo foglia.
	 * 
	 * @return	0 perchè i nodi foglia non hanno figli.
	 */
	int getNumberOfChildren() {
		return 0;
	}
	
	/**
	 * Invoca il metodo della superclasse assegnando anche il valore di classe della foglia.
	 * 
	 * @return	stringa che concatena le informazioni del nodo foglia.
	 */
	public String toString() {
		return "LEAF : class="+getPredictedClassValue()+" "+super.toString();
	}
}
