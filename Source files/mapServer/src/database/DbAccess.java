package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che realizza l'accesso alla base di dati.
 */
public class DbAccess  {

	private static String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";	
	private final String DBMS = "jdbc:mysql";
	private final String SERVER = "localhost";
	private final String DATABASE = "MapDB";
	private final int PORT = 3306;
	private final String USER_ID = "MapUser";
	private final String PASSWORD = "map";
	
	private Connection conn;
	
	/**
	 * Costruttore di classe. Impartisce al class loader l'ordine di caricare il driver mysql, inizializza la connessione riferita da conn. Solleva un eccezione di tipo DataBaseConnectionException qualora si verificasse un fallimento nella connessione al database.
	 * 
	 * @throws DatabaseConnectionException		sollevata nel caso di fallimento nella connessione al database.
	 */
	public DbAccess() throws DatabaseConnectionException		
	{
		try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
		} catch(ClassNotFoundException e) {
			System.out.println("[!] Driver not found: " + e.getMessage());
			throw new DatabaseConnectionException();
		} catch(InstantiationException e){
			System.out.println("[!] Error during the instantiation : " + e.getMessage());
			throw new DatabaseConnectionException();
		} catch(IllegalAccessException e){
			System.out.println("[!] Cannot access the driver : " + e.getMessage());
			throw new DatabaseConnectionException();
		}
		String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
					+ "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";
			
		System.out.println("Connection's String: " + connectionString);
			
			
		try {			
			conn = DriverManager.getConnection(connectionString);
		} catch(SQLException e) {
			System.out.println("[!] SQLException: " + e.getMessage());
			System.out.println("[!] SQLState: " + e.getSQLState());
			System.out.println("[!] VendorError: " + e.getErrorCode());
			throw new DatabaseConnectionException();
		}
	}

	/**
	 * Restituisce il valore di conn.
	 * 
	 * @return	valore di conn.
	 */
	Connection getConnection() {
		return conn;
	}
	
	/**
	 * Chiude la connessione conn. Solleva un eccezione SQLException.
	 * 
	 * @throws SQLException		sollevata se si verifica un fallimento nella chiusura della connessione.
	 */
	void closeConnection() throws SQLException{
		conn.close();
	}

	
}