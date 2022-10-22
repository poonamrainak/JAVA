import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDetail {
	
	public static Connection getConnection()
	{
		Connection con=null;
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
            con =DriverManager.getConnection("jdbc:mysql://localhost:3306/bigbasket","root","root");
		   
		
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return con;
		

	}
	
	
	
}
