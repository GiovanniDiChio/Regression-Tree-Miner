package database;

//imports

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import data.TrainingDataException;



/**
 * Classe che modella l'insieme di transazioni collezionate in una tabella.
 */
public class TableData {

	private DbAccess db;

	/**
	 * Costruttore di classe. Avvalora l'attributo db che contiene i parametri di connessione al database.
	 * 
	 * @param db
	 */
	public TableData(DbAccess db) {
		this.db=db;
	}

	/**
	 * Ricava lo schema della tabella con nome table. Esegue un'interrogazione per estrarre le tuple distinte da tale tabella. Per ogni tupla del resultset, si crea un oggetto, istanza della classe Example, il cui riferimento va incluso nella lista da restituire.
	 * In particolare per la tupla corrente nel resultset, si estraggono i valori dei singoli campi e li si aggiungono all'oggetto istanza della classe Example che si sta costruendo. Solleva eccezioni di tipo SQLException ed EmptySetException.
	 * 
	 * @param table						nome della tabella da cui ricavare gli esempi.
	 * 
	 * @return							lista di transazioni memorizzate nella tabella.
	 * 
	 * @throws SQLException				sollevata se ci sono errori nell'esecuzione della query.
	 * @throws EmptySetException		sollevata se il resultset è vuoto.
	 * @throws TrainingDataException 	sollevata se l'ultima colonna non possiede numeri.
	 */
	public List<Example> getTransazioni(String table) throws SQLException, EmptySetException, TrainingDataException{
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		
		
		String query="select ";
		
		for(int i=0;i<tSchema.getNumberOfAttributes();i++){
			Column c=tSchema.getColumn(i);
			if(i>0)
				query+=",";
			query += c.getColumnName();
		}
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();
		query += (" FROM "+table);
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty=true;
		while (rs.next()) {
			empty=false;
			Example currentTuple=new Example();
			for(int i=0;i<tSchema.getNumberOfAttributes();i++)
				if(tSchema.getColumn(i).isNumber()) 
					currentTuple.add(rs.getDouble(i+1));
				else
				{
					if(i==tSchema.getNumberOfAttributes()-1)
						throw new TrainingDataException("Attribute not a number");
				
					currentTuple.add(rs.getString(i+1));
				}
					
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		if(empty) throw new EmptySetException();
		
		
		return transSet;

	}

	/**
	 * Formula ed esegue una interrogazione SQL per estrarre i valori distinti ordinati di column e popolare un insieme da restituire. Solleva eccezioni di tipo SQLException ed EmptySetException.
	 * 
	 * @param table					nome della tabella.
	 * @param column				nome della colonna nella tabella.
	 * 
	 * @return						insieme di valori distinti ordinati in modalità ascendente che l'attributo identificato da nome column assume nella tabella identificata dal nome table.
	 * 
	 * @throws SQLException			sollevata se ci sono errori nell'esecuzione della query.
	 * @throws EmptySetException	sollevata se il resultset è vuoto.
	 */
	public Set<Object> getDistinctColumnValues (String table, Column column) throws SQLException, EmptySetException{
		Set<Object> result=new TreeSet<Object>();
		Statement statement;
		String query="SELECT "+column.getColumnName()+" FROM "+table;
		statement=db.getConnection().createStatement();
		ResultSet rs=statement.executeQuery(query);
		boolean empty=true;
		while(rs.next()) {
			empty=false;
			result.add(rs.getString(column.getColumnName()));
		}
		rs.close();
		statement.close();
		if(empty) throw new EmptySetException();
		
		return result;
	}
		
	public enum QUERY_TYPE {
		MIN, MAX
		}
	

	

}
