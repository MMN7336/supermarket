package exception;

import java.util.Scanner;

public class throwbank {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		double balance=10000;
		try {
			System.out.println("Your balance is"+ balance);
			System.out.println("Enter withdraw amount");
			double amount=sc.nextDouble();
			if(amount>balance) {
				throw new IllegalArgumentException("Insufficient balance");
			}
			else if(amount<=0) {
				throw new IllegalArgumentException("Invalid amount");
				
			}
			else {
			double receive=balance-amount;
			System.out.println("Your balance is"+ receive);
			}
		}
		catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}

}
