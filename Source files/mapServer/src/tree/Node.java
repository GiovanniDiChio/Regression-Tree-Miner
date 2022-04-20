package tree;

//imports

import java.io.Serializable;
import java.lang.Math;

import data.Data;

/**
 * Classe astratta che modella l'astrazione dell'entità nodo (fogliare o intermedio) dell'albero di decisione. Implementa l'interfaccia Serializable.
 */
public abstract class Node implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static int idNodeCount=0;
	private int idNode;
	private int beginExampleIndex;
	private int endExampleIndex;
	private double variance;
	
	/**
	 * Costruttore di classe. Ne inizializza i parametri richiamando anche il metodo calcVariance.
	 * 
	 * @param trainingSet		collezione di esempi di apprendimento.
	 * @param beginExampleIndex	indice nell'array del training set del primo esempio coperto dal nodo corrente.
	 * @param endExampleIndex	indice nell'array del training set dell'ultimo esempio coperto dal nodo corrente.
	 */
	Node(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
		this.beginExampleIndex=beginExampleIndex;
		this.endExampleIndex=endExampleIndex;
		this.idNode=idNodeCount;
		Node.idNodeCount++;
		this.variance=calcVariance(trainingSet, beginExampleIndex, endExampleIndex);
		
	}
	
	/**
	 * Restituisce il valore dell'identificativo numerico del nodo.
	 * 
	 * @return identificativo numerico del nodo.
	 */
	int getIdNode() {
		return this.idNode;
	}
	
	/**
	 * Restituisce il valore dell'indice del primo esempio del sotto-insieme rispetto al training set complessivo.
	 * 
	 * @return	indice del primo esempio.
	 */
	int getBeginExampleIndex() {
		return this.beginExampleIndex;
	}
	
	/**
	 * Restituisce il valore dell'indice dell'ultimo esempio del sotto-insieme rispetto al training set complessivo.
	 * 
	 * @return	indice dell'ultimo esempio.
	 */
	int getEndExampleIndex() {
		return this.endExampleIndex;
	}
	
	/**
	 * Restituisce il valore della somma dei quadrati degli errori (SSE) dell'attributo da predire rispetto al nodo corrente.
	 * 
	 * @return	valore dello SSE dell'attributo da predire rispetto al nodo corrente.
	 */
	double getVariance() {
		return this.variance;
	}
	
	/**
	 * Calcola il valore dello SSE, rispetto all'attributo di classe, nel sottoinsieme di training del nodo definito dagli indici start ed end.
	 * 
	 * @param trainingSet	collezione di esempi di apprendimento.
	 * @param start			indice d'inizio del sottoinsieme.
	 * @param end			indice di fine del sottoinsieme.
	 * @return				valore dello SSE calcolato.
	 */
	private double calcVariance(Data trainingSet, int start, int end) {
		double var1=0, var2=0;
		for(int i=0; start+i<end+1; i++) {
			var1=var1+Math.pow(trainingSet.getClassValue(start+i), 2);
			var2=var2+trainingSet.getClassValue(start+i);
		}
		return var1-(Math.pow(var2, 2)/(end-start+1));
	}
	
	/**
	 * Restituisce il numero di nodi figli. Astratto perchè applicabile soltanto a nodi di tipo SplitNode.
	 * 
	 * @return numero di nodi figli.
	 */
	abstract int getNumberOfChildren();
	
	/**
	 * Restituisce le informazioni del nodo concatenandole in una stringa.
	 * 
	 * @return	 stringa contenente le informazioni del nodo.
	 */
	public String toString() {
		return "Nodo: [Examples:"+beginExampleIndex+"-"+endExampleIndex+"] variance:"+variance;
	}
	
	
}
