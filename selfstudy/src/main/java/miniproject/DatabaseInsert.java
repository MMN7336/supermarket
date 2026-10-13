package miniproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInsert {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url="jdbc:mysql://localhost:3306/wallet_db";
		String user="root";
		String password="root";
		String userId="S050";
		String name="Mg Mg";
		String email="mg@gmail.com";
		
		String sql="INSERT INTO Users (user_id,name,email) VALUES (?,?, ?)";
		try (Connection con=DriverManager.getConnection(url, user, password);
				PreparedStatement pare=con.prepareStatement(sql)){
			pare.setString(1,userId);
			pare.setString(2,name);
			pare.setString(3,email);
			int rowsAffected=pare.executeUpdate();
			if(rowsAffected>0) {
				System.out.println("User add successfull");
			}
			
		}
		catch(SQLException e) {
			System.out.println("Error :"+e.getMessage())
;		}
		String wallet_id="W030";
		String user_id="S050";
		double balance=2000;
		String walletsql="INSERT INTO Wallets(wallet_id,user_id,balance) VALUES(?,?,?)";
		try(Connection con=DriverManager.getConnection(url,user, password);
				PreparedStatement pare1=con.prepareStatement(walletsql)){
			pare1.setString(1, wallet_id);
			pare1.setString(2, user_id);
			pare1.setDouble(3, balance);
			int rowsAffected = pare1.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Wallet added successfully!");
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
				

	}

}
