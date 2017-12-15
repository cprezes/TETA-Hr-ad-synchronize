import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class GetData {

	static Baza baza;
	
	

	public static void main(String[] args) throws IOException, SQLException {

		baza = new Baza();
		baza.initialize();
			getResult();
	}

	
	public static void getResult() throws SQLException, IOException {
		Util util= new Util();
		FileWrite file = new FileWrite("out.txt");
		String guery=util.getQuery(); 
		ResultSet resultSet = baza.executeQuery(guery);

		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		String line="";
		
		while (resultSet.next()) {
		    line ="";
			for (int i = 1; i <= columnsNumber; i++) {
		        if (i > 1) line= line+" ";
		      String columnValue = resultSet.getString(i);
		      //System.out.println(rsmd.getColumnName(i) +"   "+ columnValue);
		            line=line + util.addPsTag(rsmd.getColumnName(i) , columnValue);
		    	}
			   file.write(line);
		    }
		file.closeFile();
	}

	
}
