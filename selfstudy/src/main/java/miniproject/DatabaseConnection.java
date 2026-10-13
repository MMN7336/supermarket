package miniproject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="jdbc:mysql://localhost:3306/wallet_db";
		String user="root";
		String password="root";
		try {
			Connection con=DriverManager.getConnection(url,user,password);
			if(con!=null) {
				System.out.println("Database connection successfull");
			}
		}
		catch(SQLException e) {
			System.out.println("unsuccessfull"+e.getMessage());
			
		}

	}

}
