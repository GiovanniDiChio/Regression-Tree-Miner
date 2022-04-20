package data;

//imports

import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.TableData;
import database.TableSchema;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;


/**
 * Classe che modella gli esempi dell'insieme di training caricandoli da una tabella di un database relazionale.
 */
public class Data {
	
	private List<Example> data=new ArrayList<Example>();
	private int numberOfExamples;
	private List<Attribute> explanatorySet=new LinkedList<Attribute>();
	private ContinuousAttribute classAttribute;
	
	/**
	 * Costruttore di classe. Si occupa di caricare i dati (schema ed esempi) di addestramento da una tabella table della base di dati. Solleva eccezioni di tipo TrainingDataException se la connessione al database fallisce, la tabella non esiste, la tabella ha meno di due colonne, la tabella ha zero tuple, l'attributo corrispondente all'ultima colonna della tabella non è numerico.
	 * In base al tipo di dato presente nel database, li aggiunge come DiscreteAttribute o ContinuousAttribute.
	 * 
	 * @param table						nome della tabella del database.
	 * 
	 * @throws TrainingDataException	sollevata se la connessione al database fallisce, la tabella non esiste, la tabella ha meno di due colonne, la tabella ha zero tuple, l'attributo corrispondente all'ultima colonna della tabella non è numerico.
	 */
	public Data(String table) throws TrainingDataException{
		
		try {
		DbAccess access=new DbAccess();
		TableData dbData=new TableData(access);
		TableSchema dbSchema=new TableSchema(access, table);
		data=dbData.getTransazioni(table);
		numberOfExamples=data.size();
		if(numberOfExamples==0)
			throw new TrainingDataException("Specified table is empty.");
		if(dbSchema.getNumberOfAttributes()<2)
			throw new TrainingDataException("Table has less than 2 columns.");
		
		for(int i=0; i<dbSchema.getNumberOfAttributes()-1; i++) {

			if(!dbSchema.getColumn(i).isNumber()) {
				Set<String> valueSet=dbData.getDistinctColumnValues(table, dbSchema.getColumn(i)).stream().map(e->String.valueOf(e)).collect(Collectors.toSet());		//da set di object a set di stringhe
				explanatorySet.add(new DiscreteAttribute(dbSchema.getColumn(i).getColumnName(), i, valueSet));
			}
			else {
				explanatorySet.add(new ContinuousAttribute(dbSchema.getColumn(i).getColumnName(), i));
			}
		}
	}catch(DatabaseConnectionException a) {
		throw new TrainingDataException("Error connecting to the database.");
	}catch(EmptySetException b) {
		throw new TrainingDataException("Specified table is empty.");
	}catch(SQLException c) {
		throw new TrainingDataException("Specified table does not exist.");
	}
	}
	
	/**
	 * Restituisce il numero di esempi caricati.
	 * 
	 * @return	numero di esempi caricati.
	 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}
	
	/**
	 * Restituisce la grandezza della lista di attributi indipendenti.
	 * 
	 * @return	numero di attributi indipendenti.
	 */
	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}
	
	/**
	 * Restituisce il valore dell'attributo di classe per l'esempio exampleIndex.
	 * 
	 * @param exampleIndex	indice dell'esempio da considerare.
	 * 
	 * @return				valore dell'attributo di classe.
	 */
	public Double getClassValue(int exampleIndex) {
		return (Double)data.get(exampleIndex).get(explanatorySet.size());
	}
		
	/**
	 * Restituisce il valore dell'attributo indicizzato da attributeIndex per l'esempio exampleIndex.
	 * 
	 * @param exampleIndex		indice dell'esempio da considerare.
	 * @param attributeIndex	indice dell'attributo da considerare.
	 * 
	 * @return					Object associato all'attributo indipendente per l'esempio indicizzato in input
	 */
	public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
		//return data[exampleIndex][attributeIndex];
		return data.get(exampleIndex).get(attributeIndex);
	}
	
	/**
	 * Restituisce l'attributo indicizzato da index in explanatorySet.
	 * 
	 * @param index		indice dell'attributo da considerare.
	 * 
	 * @return			Attribute indicizzato da index.
	 */
	public Attribute getExplanatoryAttribute(int index){
		return explanatorySet.get(index);
	}
	
	/**
	 * Restituisce l'oggetto corrispondente all'attributo di classe.
	 * 
	 * @return		ContinuousAttribute associato al membro classAttribute.
	 */
	public ContinuousAttribute getClassAttribute(){
		return this.classAttribute;
	}
	
	/**
	 * Concatena in una stringa tutti i valori degli attributi per ogni esempio di data e la restituisce.
	 */
	public String toString(){
		String value="";
		for(int i=0;i<numberOfExamples;i++){
			for(int j=0;j<explanatorySet.size();j++)
				//value+=data[i][j]+",";
				value+=data.get(i).get(j);
			
			//value+=data[i][explanatorySet.size()]+"\n";
			value+=data.get(i).get(explanatorySet.size())+"\n";
		}
		return value;
		
		
	}


	/**
	 * Ordina il sottoinsieme di esempi compresi nell'intervallo beginExampleIndex e endExampleIndex rispetto allo specifico attributo attribute. Usa l'algoritmo quicksort per l'ordinamento di un array di interi usando come relazione d'ordine totale minore o uguale.
	 * L'array in questo caso è dato dai valori assunti dall'attributo passato in input. Richiama i metodi quicksort, partition e swap.
	 * 
	 * @param attribute				Attribute i cui valori devono essere ordinati.
	 * @param beginExampleIndex		indice del primo esempio del sottoinsieme.
	 * @param endExampleIndex		indice dell'ultimo esempio del sottoinsieme.
	 */
	public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex){
			quicksort(attribute, beginExampleIndex, endExampleIndex);
	}
	

	/**
	 * Scambia l'esempio in data indicizzato da i con l'esempio indicizzato da j.
	 * 
	 * @param i		indice dell'esempio da scambiare.
	 * @param j		indice dell'esempio da scambiare.
	 */
	private void swap(int i,int j){
			Collections.swap(data, i, j);
	}
		
	/**
	 * Partiziona il vettore rispetto all'elemento x e restituisce il punto di separazione.
	 * 
	 * @param attribute		attributo di tipo DiscreteAttribute.
	 * @param inf			indice d'inizio del vettore.
	 * @param sup			indice di fine del vettore.
	 * 
	 * @return				punto di separazione.
	 */
	private int partition(DiscreteAttribute attribute, int inf, int sup){
		int i,j;
	
		i=inf; 
		j=sup; 
		int	med=(inf+sup)/2;
		String x=(String)getExplanatoryValue(med, attribute.getIndex());
		swap(inf,med);
	
		while (true) 
		{
			
			while(i<=sup && ((String)getExplanatoryValue(i, attribute.getIndex())).compareTo(x)<=0){ 
				i++; 
				
			}
		
			while(((String)getExplanatoryValue(j, attribute.getIndex())).compareTo(x)>0) {
				j--;
			
			}
			
			if(i<j) { 
				swap(i,j);
			}
			else break;
		}
		swap(inf,j);
		return j;

	}
	
	/**
	 * Partiziona il vettore rispetto all'elemento x e restiutisce il punto di separazione.
	 * 
	 * @param attribute		attributo di tipo ContinuousAttribute.
	 * @param inf			indice d'inizio del vettore.
	 * @param sup			indice di fine del vettore.
	 * 
	 * @return				punto di separazione.
	 */
	private int partition(ContinuousAttribute attribute, int inf, int sup){
		int i,j;
	
		i=inf; 
		j=sup; 
		int	med=(inf+sup)/2;
		Double x=(Double)getExplanatoryValue(med, attribute.getIndex());
		swap(inf,med);
	
		while (true) 
		{
			
			while(i<=sup && ((Double)getExplanatoryValue(i, attribute.getIndex())).compareTo(x)<=0){ 
				i++; 
				
			}
		
			while(((Double)getExplanatoryValue(j, attribute.getIndex())).compareTo(x)>0) {
				j--;
			
			}
			
			if(i<j) { 
				swap(i,j);
			}
			else break;
		}
		swap(inf,j);
		return j;

	}
	
	/**
	 * Algoritmo quicksort per l'ordinamento di un array di interi A usando come relazione d'ordine totale minore o uguale.
	 * 
	 * @param attribute		attributo di tipo generico.
	 * @param inf			indice d'inizio del vettore.
	 * @param sup			indice di fine del vettore.
	 */
	private void quicksort(Attribute attribute, int inf, int sup){
		
		if(sup>=inf){
			
			int pos;
			if(attribute instanceof DiscreteAttribute)
				pos=partition((DiscreteAttribute)attribute, inf, sup);
			else
				pos=partition((ContinuousAttribute)attribute, inf, sup);
					
			if ((pos-inf) < (sup-pos+1)) {
				quicksort(attribute, inf, pos-1); 
				quicksort(attribute, pos+1,sup);
			}
			else
			{
				quicksort(attribute, pos+1, sup); 
				quicksort(attribute, inf, pos-1);
			}
			
			
		}
		
	}
	
}

