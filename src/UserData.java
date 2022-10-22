import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class UserData {
	
	public static void getUserData()
	{
		UserModel userModel = new UserModel();
		System.out.println(" ********Please Register Here************");
		
		Scanner sc = new Scanner(System.in);
		System.out.println(" Enter username ");
		userModel.setUsername(sc.nextLine());
		
		System.out.println(" Enter password ");
		userModel.setPassword(sc.nextLine());
		
		System.out.println(" Enter role 1.Role Admin 2.Role User ");
		userModel.setRoleId(sc.nextInt());
		
		sc = new Scanner(System.in);
		System.out.println(" Enter city ");
		userModel.setCity(sc.nextLine());
		
		System.out.println(" Enter mobileNo ");
		userModel.setMobileNo(sc.nextLong());
		
		saveUserData(userModel);
	}
	
	
	public static int checkRegisterUser(){
		
		Scanner sc = new Scanner(System.in);
		System.out.println(" Enter username ");
		String username=sc.nextLine();
		
		System.out.println(" Enter password ");
		String password=sc.nextLine();
		
		int i=0;
		try
		{
		Connection con=ConnectionDetail.getConnection();
		PreparedStatement pstmt = con.prepareStatement("SELECT * FROM user WHERE username = ? and password =? ");
	    pstmt.setString(1, username);
	    pstmt.setString(2, password);
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			i = rs.getInt(1);

			
			//System.out.println("user id"+i);
		}
		
		con.close();
		pstmt.close(); 
		}
		catch (Exception e) {
			// TODO: handle exceptione.
			e.printStackTrace();
		}
		return i;
 
	}



public static void saveUserData(UserModel userModel)
{
	Connection con=ConnectionDetail.getConnection();
	
	try {
		PreparedStatement stmt =con.prepareStatement("insert into user(roleid,username,password,city,mobileNo)values(?,?,?,?,?)");
		
		stmt.setInt(1, userModel.getRoleId());
    	stmt.setString(2, userModel.getUsername());
 		stmt.setString(3, userModel.getPassword());
 		stmt.setString(4, userModel.getCity());
 		stmt.setLong(5, userModel.getMobileNo());
		
 		int j = stmt.executeUpdate();
		
		System.out.println("Record is inserted."+ j);
 		con.close();
		stmt.close();
		
		
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
}
}