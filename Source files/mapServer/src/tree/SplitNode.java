package tree;

//imports

import data.Attribute;
import data.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Classe astratta per modellare l'astrazione dell'entità nodo di split (continuo o discreto). Estende la superclasse Node ed implementa l'interfaccia Comparable.
 */
abstract class SplitNode extends Node implements Comparable<SplitNode> {
	
	/**
	 * Classe che colleziona informazioni descrittive dello split. Implementa l'interfaccia Serializable.
	 */
	class SplitInfo implements Serializable{
		private Object splitValue;
		private int beginIndex;
		private int endIndex;
		private int numberChild;
		private String comparator="=";
		private static final long serialVersionUID = 1L;

		
		/**
		 * Costruttore di classe. Avvalora gli attributi di classe per split a valori discreti.
		 * 
		 * @param splitValue 	valore di tipo Object (di un attributo indipendente) che definisce uno split.
		 * @param beginIndex	indice del primo esempio del sottoinsieme considerato.
		 * @param endIndex		indice dell'ultimo esempio del sottoinsieme considerato.
		 * @param numberChild	numero di split (nodi figli) originanti dal nodo corrente.
		 */
		SplitInfo(Object splitValue,int beginIndex,int endIndex,int numberChild){
			this.splitValue=splitValue;
			this.beginIndex=beginIndex;
			this.endIndex=endIndex;
			this.numberChild=numberChild;
		}
		
		/**
		 * Costruttore di classe. Avvalora gli attributi di classe per generici split.
		 * 
		 * @param splitValue	valore di tipo Object (di un attributo indipendente) che definisce uno split.
		 * @param beginIndex	indice del primo sesempio del sottoinsieme considerato.
		 * @param endIndex		indice dell'ultimo esempio del sottoinsieme considerato.
		 * @param numberChild	numero di split (nodi figli) originanti dal nodo corrente.
		 * @param comparator	operatore matematico che definisce il test nel nodo corrente.
		 */
		SplitInfo(Object splitValue,int beginIndex,int endIndex,int numberChild, String comparator){
			this.splitValue=splitValue;
			this.beginIndex=beginIndex;
			this.endIndex=endIndex;
			this.numberChild=numberChild;
			this.comparator=comparator;
		}
		
		/**
		 * Restituisce l'indice del primo esempio del sottoinsieme considerato.
		 * 
		 * @return indice del primo esempio del sottoinsieme considerato.
		 */
		int getBeginindex(){
			return beginIndex;			
		}
		
		/**
		 * Restituisce l'indice dell'ultimo esempio del sottoinsieme considerato.
		 * 
		 * @return l'indice dell'ultimo esempio del sottoinsieme considerato.
		 */
		int getEndIndex(){
			return endIndex;
		}
		
		/**
		 * Restituisce il valore dello split.
		 * 
		 * @return valore di tipo Object che definisce uno split.
		 */
		Object getSplitValue(){
			return splitValue;
		}
		 
		/**
		 * Restituisce le informazioni del nodo di split.
		 *
		 *@return Stringa contenente le informazioni del nodo di split.
		 */
		public String toString(){
			return "child " + numberChild +" split value"+comparator+splitValue + "[Examples:"+beginIndex+"-"+endIndex+"]";
		}
		
		 /**
		  * Restituisce il comparator da utilizzare.
		  * 
		 * @return Stringa contenente il comparator da utilizzare ("=" per valori discreti).
		 */
		String getComparator(){
			return comparator;
		}		
	}

	private Attribute attribute;	
	List<SplitInfo> mapSplit;
	private double splitVariance;		
	private static final long serialVersionUID = 1L;

	
	/**
	 * Metodo astratto per generare le informazioni necessarie per ciascuno degli split candidati.
	 * 
	 * @param trainingSet			collezione di esempi di apprendimento.
	 * @param beginExampleIndex		indice d'inizio del sottoinsieme di training.
	 * @param endExampleIndex		indice di fine del sottoinsieme di training.
	 * @param attribute				attributo indipendente sul quale si definisce lo split.
	 */
	abstract void setSplitInfo(Data trainingSet,int beginExampleIndex, int endExampleIndex, Attribute attribute);
	
	/**
	 * Metodo astratto per modellare la condizione di test.
	 * 
	 * @param value		valore dell'attributo che si vuole testare rispetto a tutti gli split.
	 */
	abstract int testCondition (Object value);
	
	/**
	 * Costruttore di classe. Invoca il costruttore della superclasse, ordina i valori dell'attributo di input per gli esempi del sottoinsieme che vanno da beginExampleIndex a endExampleIndex e sfrutta questo ordinamento per determinare i possibili split e popolare la lista mapSplit.
	 * Computa lo SSE per l'attributo usato nello split sulla base del partizionamento indotto dallo split (lo stesso è la somma degli SSE su ciascuno SplitInfo collezionato in mapSplit).
	 * 
	 * @param trainingSet			collezione di esempi di apprendimento.
	 * @param beginExampleIndex		indice d'inizio del sottoinsieme di training.
	 * @param endExampleIndex		indice di fine del sottoinsieme di training.
	 * @param attribute				attributo indipendente sul quale si definisce lo split.
	 */
	SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute){
			super(trainingSet, beginExampleIndex,endExampleIndex);
			this.attribute=attribute;
			trainingSet.sort(attribute, beginExampleIndex, endExampleIndex); // order by attribute

			setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);
						
			//compute variance
			splitVariance=0;
			for(int i=0;i<mapSplit.size();i++){
					double localVariance=new LeafNode(trainingSet, mapSplit.get(i).getBeginindex(),mapSplit.get(i).getEndIndex()).getVariance();
					splitVariance+=(localVariance);
			}
	}
	
	/**
	 * Restituisce l'oggetto per l'attributo usato per lo split.
	 * 
	 * @return oggetto per l'attributo usato per lo split.
	 */
	Attribute getAttribute(){
		return attribute;
	}
	
	/**
	 * Restituisce l'information gain per lo split corrente.
	 * 
	 * @return information gain per lo split corrente.
	 */
	double getVariance(){
		return splitVariance;
	}
	
	/**
	 * Restituisce il numero dei rami originanti nel nodo corrente, ovvero la cardinalità della lista mapSplit.
	 * 
	 * @return cardinalità della lista mapSplit.
	 */
	int getNumberOfChildren(){
		return mapSplit.size();
	}
	
	/**
	 * Restituisce le informazioni per il ramo in mapSplit indicizzato da child.
	 * 
	 * @param child		indice del ramo di cui richiedere le informazioni.
	 * @return			informazioni descrittive dello split indicizzato da child.
	 */
	SplitInfo getSplitInfo(int child){
		return mapSplit.get(child);
	}

	
	/**
	 * Concatena le informazioni di ciascun test in una String e la restituisce. Utilizzato per la predizione di nuovi esempi.
	 * 
	 * @return		Stringa contenente le informazioni di ciascun test (attributo, operatore e valore).
	 */
	String formulateQuery(){
		String query = "";
		for(int i=0;i<mapSplit.size();i++)
			query+= (i+1 + ":" + attribute + mapSplit.get(i).getComparator() +mapSplit.get(i).getSplitValue())+"\n";
		return query;
	}
	
	/**
	 * Concatena le informazioni di ciascun test in una String finale e la restituisce.
	 * 
	 * @return		informazioni di ciascun test (attributo, esempi coperti, varianza di split).
	 */
	public String toString(){
		String v= "SPLIT : attribute=" +attribute +" "+ super.toString()+  " Split Variance: " + getVariance()+ "\n" ;
		
		for(int i=0;i<mapSplit.size();i++){
			v+= "\t"+mapSplit.get(i)+"\n";
		}
		
		return v;
	}
	
	/**
	 * Effettua il confronto tra due SplitNode basato sulla loro splitVariance.
	 * 
	 * @return		Integer che vale 1, -1 o 0 a seconda del risultato del confronto.
	 */
	public int compareTo(SplitNode o) {
		if(this.splitVariance<o.splitVariance)
			return -1;
		else if(this.splitVariance>o.splitVariance)
			return 1;
		else {
			return 0;
		}
	}
}
