package tree;

//imports

import data.Data;
import Utility.Keyboard;
import data.Attribute;
import data.DiscreteAttribute;
import server.UnknownValueException;
import data.ContinuousAttribute;

import java.io.*;
import java.util.TreeSet; 

/**
 * Classe che modella l'entità albero di decisione come insieme di sottoalberi. Implementa l'interfaccia Serializable per poter serializzarne gli oggetti.
 */
public class RegressionTree implements Serializable{
	
	private Node root;
	private RegressionTree childTree[];
	private static final long serialVersionUID = 1L;
	
	/**
	 * Costruttore di default della classe. Istanzia un sotto-albero dell'intero albero.
	 */
	private RegressionTree(){
	}
	
	/**
	 * Costruttore della classe. Istanzia un sotto-albero dell'intero albero e avvia l'induzione dell'albero dagli esempi di training in input.
	 * 
	 * @param trainingSet	collezione di esempi di apprendimento.
	 */
	public RegressionTree(Data trainingSet){			
		learnTree(trainingSet,0,trainingSet.getNumberOfExamples()-1,trainingSet.getNumberOfExamples()*10/100);
	}
		
	/**
	 * Genera un sottoalbero con il sottoinsieme di input istanziando un nodo fogliare o un nodo di split. In tal caso determina il miglior nodo rispetto al sottoinsieme di input ed a tale nodo esso associa un sottoalbero avente radice il nodo medesimo e avente un numero di rami pari al numero dei figli determinati dallo split.
	 * Ricorsivamente in ogni oggetto RegressionTree sarà reinvocato il metodo learnTree() per l'apprendimento su un insieme ridotto del sottoinsieme attuale. Nella condizione in cui il nodo di split non origina figli, diventa fogliare.
	 * 
	 * @param trainingSet				collezione di esempi di apprendimento.
	 * @param begin						primo indice del sottoinsieme di esempi considerato.
	 * @param end						ultimo indice del sottoinsieme di esempi considerato.
	 * @param numberOfExamplesPerLeaf	numero minimo che una foglia deve contenere.
	 */
	private void learnTree(Data trainingSet,int begin, int end,int numberOfExamplesPerLeaf){
		if( isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf)){
			//determina la classe che compare più frequentemente nella partizione corrente
			root=new LeafNode(trainingSet,begin,end);
		}
		else //split node
		{
			root=determineBestSplitNode(trainingSet, begin, end);	
			if(root.getNumberOfChildren()>1){
				childTree=new RegressionTree[root.getNumberOfChildren()];
				for(int i=0;i<root.getNumberOfChildren();i++){
					childTree[i]=new RegressionTree();
					childTree[i].learnTree(trainingSet, ((SplitNode)root).getSplitInfo(i).getBeginindex(), ((SplitNode)root).getSplitInfo(i).getEndIndex(), numberOfExamplesPerLeaf);
				}
			}
			else
				root=new LeafNode(trainingSet,begin,end);			
		}
	}
	
	/**
	 * Per ciascun attributo indipendente istanzia il DiscreteNode associato e seleziona il nodo di split con minore varianza tra i DiscreteNode istanziati. Ordina la porzione di trainingSet corrente (tra begin ed end) rispetto all'attributo indipendente del nodo di split selezionato. Restituisce il nodo selezionato.
	 * 
	 * @param trainingSet		collezione di esempi di apprendimento.
	 * @param begin				primo indice del sottoinsieme di esempi considerato.
	 * @param end				ultimo indice del sottoinsieme di esempi considerato.
	 * 
	 * @return					nodo di split con minore varianza tra quelli istanziati.
	 */
	private SplitNode determineBestSplitNode(Data trainingSet, int begin, int end) {
		TreeSet<SplitNode> ts=new TreeSet<SplitNode>();
		SplitNode currentNode=null;
		for(int j=0; j<trainingSet.getNumberOfExplanatoryAttributes(); j++) {
			Attribute a=trainingSet.getExplanatoryAttribute(j);
			if(a instanceof DiscreteAttribute) {
				DiscreteAttribute attribute=(DiscreteAttribute)trainingSet.getExplanatoryAttribute(j);
				currentNode=new DiscreteNode(trainingSet, begin, end, attribute);
			}
			else {
				ContinuousAttribute attribute=(ContinuousAttribute)trainingSet.getExplanatoryAttribute(j);
				currentNode=new ContinuousNode(trainingSet, begin, end, attribute);
			}
			
			ts.add(currentNode);
		}
		SplitNode bestNode=ts.first();		
		trainingSet.sort(bestNode.getAttribute(), begin, end);
		return bestNode;
	}

	/**
	 * Verifica se il sottoinsieme corrente può essere coperto da un nodo foglia controllando che il numero di esempi del training set compresi tra begin ed end sia minore o uguale di numberOfExamplesPerLeaf.
	 * 
	 * @param trainingSet				collezione di esempi di apprendimento.
	 * @param begin						primo indice del sottoinsieme di esempi considerato.
	 * @param end						ultimo indice del sottoinsieme di esempi considerato.
	 * @param numberOfExamplesPerLeaf	numero minimo che una foglia deve contenere.
	 * 
	 * @return							true se il numero esempi compresi tra begin ed end è minore o uguale di numberOfExamplePerLeaf, false altrimenti.
	 */
	private boolean isLeaf(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
		return (end-begin)<=numberOfExamplesPerLeaf;		
	}
	
	/**
	 * Stampa a schermo le informazioni sull'albero sfruttando la funzione toString().
	 */
	void printTree(){
		System.out.println("********* TREE **********\n");
		System.out.println(toString());
		System.out.println("*************************\n");
	}
		
	/**
	 * Concatena in una String tutte le informazioni di root-childTree correnti invocando i relativi metodi toString(). Nel caso il root corrente è di split, vengono concatenate anche le informazioni dei rami.
	 * 
	 * @return		String che contiene le informazioni dell'albero.
	 */
	public String toString(){
		String tree=root.toString()+"\n";
		
		if( root instanceof LeafNode){
		
		}
		else //split node
		{
			for(int i=0;i<childTree.length;i++)
				tree +=childTree[i];
		}
		return tree;
	}
		
	/**
	 * Stampa a schermo le informazioni sulle regole dell'albero sfruttando la funzione getRules().
	 */
	void printRules() {
		System.out.println("********* RULES **********\n");
		System.out.println(getRules());
		System.out.println("\n*************************\n");
	}
	
	/**
	 * Scandisce ciascun ramo dell'albero completo dalla radice alla foglia concatenando le informazioni dei nodi di split fino al nodo foglia. In particolare per ogni sottoalbero in childTree[] concatena le informazioni del nodo root: se è di split discende ricorsivamente l'albero per ottenere le informazioni del nodo sottostante (necessario per ricostruire le condizioni in AND) di ogni ramo-regola, se è di foglia (leaf) termina l'attraversamento.
	 * 
	 * @return	String contenente le informazioni sulle regole dell'albero.
	 */
	public String getRules() {
		String rule="";
		if(root instanceof LeafNode) {
			
		}
		else {
				rule+=getRules(rule);
			}

		return rule;
	}
	
	/**
	 * Metodo di supporto a printRules(). Concatena alle informazioni n current del precedente nodo quelle del nodo root del corrente sotto-albero: se il nodo corrente è di split il metodo viene invocato ricorsivamente con current e le informazioni del nodo corrente, se è fogliare restituisce tutte le informazioni concatenate.
	 * 
	 * @param current	Stringa contenente le informazioni del nodo di split del sottoalbero al livello superiore.
	 * 
	 * @return			Stringa contenente tutte le informazioni concatenate.
	 */
	private String getRules(String current) {
		String rule="";
		if(current!="")
			current+=" AND ";
		String checkpoint=current;
		for(int i=0; i<childTree.length; i++) {
			current+=((SplitNode)root).getAttribute().getName()+"="+((SplitNode)root).getSplitInfo(i).getSplitValue();
			if(childTree[i].root instanceof LeafNode) {
				String newCurrent=current+" ==> Class="+((LeafNode)childTree[i].root).getPredictedClassValue();
				rule+=newCurrent+"\n";
				current=checkpoint;
				continue;
			}
			rule+=childTree[i].getRules(current);
			current=checkpoint;
		}
		return rule;
	}
	
	/**
	 * Visualizza le informazioni di ciascuno split dell'albero e per il corrispondente attributo acquisisce il valore dell'esempio da predire da tastiera. Se il nodo root corrente è fogliare termina l'acquisizione e restituisce la predizione per l'attributo classe, altrimenti invoca ricorsivamente sul figlio di root in childTree[] individuato dal valore acquisito da tastiera.
	 * Il metodo solleva l'eccezione UnknownValueException qualora la risposta dell'utente non permetta di selezionare un ramo valido del nodo di split. L'eccezione sarà gestita nel metodo che invoca predictClass(). Non più utilizzata nel progetto.
	 * 
	 * @return							predizione dell'attributo di classe.
	 * 
	 * @throws UnknownValueException	sollevata se il ramo scelto non è valido.
	 */
	public double predictClass() throws UnknownValueException{
		if(root instanceof LeafNode)
			return ((LeafNode) root).getPredictedClassValue();
		else{
			int risp;
			System.out.println(((SplitNode)root).formulateQuery());
			risp=Keyboard.readInt();
			if(risp==-1 || risp>=root.getNumberOfChildren())
				throw new UnknownValueException("The answer should be an integer between 0 and " +(root.getNumberOfChildren()-1)+"!");
			else
			return childTree[risp].predictClass();
			}
	}
	
	/**
	 * Utilizzata per inviare e ricevere al client informazioni utili alla predizione dei valori. Se il nodo root corrente è fogliare termina l'acquisizione, scrive un messaggio di conferma nell'ObjectOutputStream out e restituisce la predizione per l'attributo classe.
	 * Altrimenti stampa a schermo le scelte disponibili all'utente (ovvero il risultato di formulateQuery()) e le scrive nell'ObjectOutputStream out. Legge poi la scelta dell'utente ottenuta nell'ObjectInputStream in e controlla che sia valido. Se lo è, scrive un messaggio di conferma in out e richiama ricorsivamente questo metodo.
	 * 
	 * @param in						ObjectInputStream contenente le informazioni utili alla predizione dei valori.
	 * @param out						ObjectOutputStream contenente le informazioni utili alla predizione dei valori.
	 * 
	 * @return							predizione dell'attributo di classe.
	 * 
	 * @throws UnknownValueException	sollevata se il ramo scelto non è valido.
	 * @throws IOException				sollevata se c'è un problema nella lettura/scrittura degli stream.
	 * @throws ClassNotFoundException	sollevata se la lettura della scelta dell'utente non andasse a buon fine.
	 */
	public double predictClass(ObjectInputStream in, ObjectOutputStream out ) throws UnknownValueException, IOException, ClassNotFoundException{

		if(root instanceof LeafNode) {
			out.writeObject("OK");
			System.out.println("Predicted class "+((LeafNode) root).getPredictedClassValue());
			return ((LeafNode) root).getPredictedClassValue();
		}
		else{

			int risp;
			System.out.println(((SplitNode)root).formulateQuery());
			out.writeObject(((SplitNode)root).formulateQuery());

			risp=(int)in.readObject();
			System.out.println("User chose path number "+(risp+1)+".\n");
			if(risp==-1 || risp>=root.getNumberOfChildren())
				throw new UnknownValueException("The answer should be an integer between 0 and " +(root.getNumberOfChildren()-1)+"!");
			else {
				out.writeObject("QUERY");	
				return childTree[risp].predictClass(in,out);
			}
		}
	}
	
	/**
	 * Effettua la serializzazione dell'albero su file.
	 * 
	 * @param nomeFile					nome del file in cui salvare l'albero
	 * 
	 * @throws FileNotFoundException	sollevata se si dovessero verificare problemi con il file specificato.
	 * @throws IOException				sollevata se c'è un problema nella scrittura su file.
	 */
	public void salva(String nomeFile) throws FileNotFoundException, IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));
		out.writeObject(this);
		out.close();
	}
	
	/**
	 * Effettua il caricamento di un albero salvato precedentemente in un file.
	 * 
	 * @param nomeFile					nome del file su cui è salvato l'albero da caricare.
	 * 
	 * @return							albero contenuto nel file.
	 * 
	 * @throws FileNotFoundException	sollevata se non si dovesse trovare il file specificato.
	 * @throws IOException				sollevata se c'è un problema nella lettura da file.
	 * @throws ClassNotFoundException	sollevata se l'oggetto contenuto nel file non dovesse essere di tipo RegressionTree.
	 */
	public static RegressionTree carica(String nomeFile) throws FileNotFoundException,IOException,ClassNotFoundException{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));
		RegressionTree tree=(RegressionTree)in.readObject();
		in.close();
		return tree;
	}
	

}

		
