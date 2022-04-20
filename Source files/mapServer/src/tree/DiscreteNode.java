package tree;

import java.util.HashMap;

import data.Attribute;
import data.Data;
import data.DiscreteAttribute;
import java.util.ArrayList;

/**
 * Classe che modella l'entità nodo di split relativo ad un attributo indipendente discreto. Estende la classe SplitNode.
 */
class DiscreteNode extends SplitNode {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore di classe che invoca quello della super-classe.
	 * 
	 * @param trainingSet			collezione di esempi di apprendimento.
	 * @param beginExampleIndex		indice d'inizio del sottoinsieme di training.
	 * @param endExampleIndex		indice di fine del sottoinsieme di training.
	 * @param attribute				attributo indipendente sul quale si definisce lo split.
	 */
	DiscreteNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, DiscreteAttribute attribute) {
		super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
	}
	
	/**
	 * Implementa il metodo astratto corrispondente. Istanzia oggetti SplitInfo con ciascuno dei valori discreti dell'attributo relativamente al sottoinsieme di training corrente. Popola la lista mapSplit con questi oggetti.
	 * 
	 * @param trainingSet			collezione di esempi di apprendimento.
	 * @param beginExampleIndex		indice d'inizio del sottoinsieme di training.
	 * @param endExampleIndex		indice di fine del sottoinsieme di training.
	 * @param attribute				attributo indipendente sul quale si definisce lo split.
	 */
	void setSplitInfo(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
		
		HashMap<Object, Integer> map=new HashMap<Object, Integer>();
	
		int occurrences=0;
		for(int i=0; beginExampleIndex+i<=endExampleIndex; i++) {

			if(map.containsKey(trainingSet.getExplanatoryValue(beginExampleIndex+i, attribute.getIndex()))) {
				occurrences++;
				map.replace(trainingSet.getExplanatoryValue(beginExampleIndex+i, attribute.getIndex()), occurrences);				
			}
			else {
				map.put(trainingSet.getExplanatoryValue(beginExampleIndex+i, attribute.getIndex()), occurrences=1);
			}
		}

		mapSplit=new ArrayList<SplitInfo>(map.size());
		int start=beginExampleIndex;
		for(int i=0; i<map.size(); i++) {
			Object key=map.keySet().toArray()[i];
			mapSplit.add(new SplitInfo(key, start, start+map.get(key)-1, i));
			start=start+map.get(key);
		}
	}
	
	/**
	 * Implementa il metodo astratto corrispondente. Effettua il controllo del valore in input rispetto al valore contenuto nell'attributo splitValue di ciascuno degli oggetti SplitInfo collezionati in mapSplit. Restituisce l'identificativo dello split con cui il test è positivo.
	 * 
	 * @return	Indice della posizione nella lista mapSplit dello split il cui test è positivo.
	 */
	int testCondition(Object value) {
		for(int i=0; i<mapSplit.size(); i++) {
			if(value==mapSplit.get(i).getSplitValue())
				return i;
		}
		return 0;
	}
	
	/**
	 * Invoca il metodo della superclasse specializzandolo per discreti.
	 * 
	 * @return	Stringa contenente le informazioni del nodo di split discreto.
	 */
	public String toString() {
		return "DISCRETE "+super.toString();
	}
}
