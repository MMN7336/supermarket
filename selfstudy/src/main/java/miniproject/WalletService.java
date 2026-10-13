package miniproject;

import java.util.HashMap;

public class WalletService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap <String ,User> user=new HashMap<>();
		User u1=new User("S010","Mg Mg","mg@gmail.com");
		user.put("S010", u1);
		User u2=new User("S020","Ma Hla","ma@gmail.com");
		user.put("S020", u2);
		HashMap<String,Wallet> wallet=new HashMap<>();
		Wallet w1=new Wallet("W010","S010",2000.00);
		wallet.put("W010", w1);
		Wallet w2=new Wallet("W020","S020",3000.00);
		wallet.put("W020", w2);
		Wallet w=wallet.get("W010");
		System.out.println(w.getBalance());
		Wallet senderAccount=wallet.get("W010");
		Wallet receiveAccount=wallet.get("W020");
		double transferAmount=1500;
		if(transferAmount<=senderAccount.getBalance()) {
			 senderAccount.setBalance(senderAccount.getBalance()-transferAmount);
			 receiveAccount.setBalance(receiveAccount.getBalance()+transferAmount);
			 System.out.println("Your Current amount is :"+receiveAccount.getBalance());
			System.out.println("Transfer successfull");
			System.out.println("Your current amount is :"+senderAccount.getBalance());
		}
		else
			
			System.out.println("Insufficient balance");
	}

}
