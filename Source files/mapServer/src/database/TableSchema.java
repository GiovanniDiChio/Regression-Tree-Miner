package database;

//imports

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * Classe che modella lo schema di una tabella nel database relazionale. Implementa l'interfaccia Iterable per ottenere un iteratore che lavori sulle colonne.
 */
public class TableSchema implements Iterable<Column>{
	
	private List<Column> tableSchema=new ArrayList<Column>();
	
	/**
	 * Costruttore di classe. Riceve in input il gestore dell'accesso al database db e il nome della tabella tableName. Usa una HashMap per salvare le informazioni sui tipi delle colonne. Avvalora una lista di colonne caricando quelle in tableName.
	 * @param db
	 * @param tableName
	 * @throws SQLException
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException{
		
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
	
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		
		
	
		 Connection con=db.getConnection();
		 DatabaseMetaData meta = con.getMetaData();
	     ResultSet res = meta.getColumns(null, null, tableName, null);
		   
	     while (res.next()) {
	         
	         if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	        		 tableSchema.add(new Column(
	        				 res.getString("COLUMN_NAME"),
	        				 mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
	        				 ); 
	      }
	      res.close();
	    }
	  
	
		/**
		 * Restituisce la grandezza della lista tableSchema.
		 * 
		 * @return	numero di attributi presenti in tableSchema.
		 */
		public int getNumberOfAttributes(){
			return tableSchema.size();
		}
		
		/**
		 * Restituisce la colonna di tableSchema indicizzata da index.
		 * 
		 * @param index		indice della colonna da restituire.
		 * 
		 * @return			colonna di tableSchema indicizzata da index.
		 */
		public Column getColumn(int index){
			return tableSchema.get(index);
		}


		/**
		 * Restituisce un iteratore che lavora sulle colonne presenti nella lista tableSchema.
		 */
		@Override
		public Iterator<Column> iterator() {
			return tableSchema.iterator();
		}

		
	}

		     


