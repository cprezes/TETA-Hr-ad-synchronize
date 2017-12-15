import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Baza {
	private static String db = "jdbc:oracle:thin:@localhost:1521:test";
	private static String user = "AD";
	private static String pass = "2017haslo";

	public String database = "Oracle";
	
	private boolean con;
	private Connection connection;
	private Statement Stmt;
	private PreparedStatement Statement;
	private ResultSet ResultSet;


	public Baza() {
		connection=null;
		con=false;
	}

	public void initialize() {
	
		try {
			if (! isDbConnected())	connection();
			} catch (SQLException ex) {
			Log.serwerek("[" + database.toString() + " Database] Failed to connect: " + ex);
		} catch (ClassNotFoundException e) {
			Log.serwerek("[" + database.toString() + " Database] Connector not found: " + e);
		}
	}

	private Connection connection() throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection(db, user, pass);
		return connection;
	}

	public ResultSet executeQuery(String query) {

		initialize();
		try {
			Statement = connection.prepareStatement(query);
			return Statement.executeQuery();
		} catch (SQLException ex) {
			Log.serwerek("[" + database.toString() + " baza nieche wykonac: " + query + " ==> " + ex);
			errorMsg();
		}

		return null;
	}

	public boolean executeQueryIfResult(String query) {
		ResultSet rs = executeQuery(query);
		boolean ret = false;

		try {
			if (rs != null) if (rs.next()) ret = true;
			
		} catch (SQLException e) {
			Log.serwerek("Problem z wynikiem zapytania");
		}
		return ret;
	}

	public int executeUpdate(String query) {
		initialize();
		try {
			Stmt = connection.createStatement();
		return Stmt.executeUpdate(query);
		} catch (SQLException ex) {	Log.serwerek("[" + database.toString() + "baza nieche wykonac: " + query + "==>" + ex); errorMsg();	}
		return 0;
	}

	public void close() {
		try {
			if (Statement != null) Statement.close();
			if (ResultSet != null) ResultSet.close();
			if (connection != null)	connection.close();
			if (con)con=false;

		} catch (SQLException ex) {
			connection = null;
			Statement = null;
			ResultSet = null;
			con=false;
		}
	}
	
	public  boolean isDbConnected() {
		boolean ret=false;
		if (con)
	    try{  if(connection.isClosed() || connection!=null) ret=true;}
	    catch (SQLException e) {ret=false;}
	    return ret;
	}

	protected void finalize() {
		close();
	}
	
	private void errorMsg(){
		System.out.println("Wyst¹pi³ b³¹d krytyczny niepeorawny schemat bazy.");
		System.out.println("Prosze uruchomic program ponownie i przy zapytaniu \"Czy wype³niæ tabele przyk³adowymi danymi?\" wpisaæ tak.");
		System.out.println("Jeœli problem bêdzie sie powtarza³ proszê skontaktowaæ sie z programist¹");
		System.out.println("Teraz program zastaje zamkniêty.");
		System.exit(0);
	}
}
