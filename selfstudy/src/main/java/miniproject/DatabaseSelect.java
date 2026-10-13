package miniproject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSelect {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="jdbc:mysql://localhost:3306/wallet_db";
		String user="root";
		String password="root";
		String sql="SELECT * FROM Users";
		try(Connection con=DriverManager.getConnection(url, user, password);
				PreparedStatement prepare=con.prepareStatement(sql);
				ResultSet rs=prepare.executeQuery()){
			while (rs.next()) {
				String userId=rs.getString("user_id");
				String name=rs.getString("name");
				String email=rs.getString("email");
				System.out.println("user_Id  :"+userId+"  name :" +name+"  Email  :"+email);
			}
			
					
				}
		catch(SQLException e) {
			System.out.println("Error "+e.getMessage());
		}
				

	}

}
