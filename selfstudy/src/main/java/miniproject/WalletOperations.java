package miniproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletOperations {
		// TODO Auto-generated method stub
		private static final String URL="jdbc:mysql://localhost:3306/wallet_db";
		private static final String USER="root";
		private static final String PASSWORD="root";
		public static void checkBalance(String wallet_Id) {
			String sql="SELECT balance FROM Wallets WHERE wallet_id=?";
			try(Connection con=DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement prepare=con.prepareStatement(sql)){
						prepare.setString(1, wallet_Id);
						ResultSet rs=prepare.executeQuery();
						if(rs.next()) {
							Double balance=rs.getDouble("balance");
							System.out.println("Wallet ID: " + wallet_Id + " ရဲ့ လက်ကျန်ငွေ: " + balance + " ကျပ်");
						}
						else {
							System.out.println("အဆိုပါ Wallet ID ကို ရှမတွေ့ပါ။");
						}
					}
			
			catch(SQLException e) {
				System.out.println("Error" + e.getMessage());
			}

	}
		public static void transferMoney(String senderId,String receiverId,double balance) {
			String checkBalanceSQL="SELECT balance FROM Wallets WHERE Wallet_id=?";
			String updateSenderSQL="UPDATE Wallets SET balance=balance - ? WHERE Wallet_id=?";
			String updateReceiveSQL="UPDATE Wallets SET balance= balance + ?  WHERE Wallet_id=?";
			double amount=500;
			try(Connection con=DriverManager.getConnection(URL, USER, PASSWORD)){
				con.setAutoCommit(false);
				try {
					PreparedStatement checkStmt=con.prepareStatement(checkBalanceSQL);
					checkStmt.setString(1,senderId);
					ResultSet rs=checkStmt.executeQuery();
					if(!rs.next()) {
						System.out.println("Sender wallet Id ကိုရှာမတွေ့ပါ");
						return ;
					}
					double senderBalance=rs.getDouble("balance");
					if(senderBalance <amount) {
						System.out.println("Insufficient balance! လက်ကျန်ငွေ မလုံလောက်ပါ။");
	                    return;
					}
	                PreparedStatement senderStmt = con.prepareStatement(updateSenderSQL);
	                senderStmt.setDouble(1, amount);
	                senderStmt.setString(2, senderId);
	                senderStmt.executeUpdate();
	                PreparedStatement receiverStmt = con.prepareStatement(updateReceiveSQL);
	                receiverStmt.setDouble(1, amount);
	                receiverStmt.setString(2, receiverId);
	                receiverStmt.executeUpdate();
	                con.commit();
	                System.out.println("ငွေလွှဲခြင်း အောင်မြင်ပါသည်။ 🚀");
				}
				catch (SQLException e) {
	                con.rollback();
	                System.out.println("Transaction Error: " + e.getMessage());
	            }
				
			}
			catch (SQLException e) {
	            System.out.println("Database Connection Error: " + e.getMessage());
	        }
			
		}
		public static void main(String[] args) {
			transferMoney("W030", "W010", 500.0);
	        checkBalance("W030");
	        checkBalance("W010");
	    }

}
