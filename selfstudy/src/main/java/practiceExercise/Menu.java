package practiceExercise;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		boolean running =true;
		while(running) {
		System.out.println("1  : Hello");
		System.out.println("2  : Thank you ");
		System.out.println("0 :Exit");
		
		try {
		System.out.println("Enter number");
		int num=sc.nextInt();
		switch(num) {
		case 1:{
			System.out.println("Hello");
			
			break;
		}
		case 2:{
			System.out.println("Thank you ");
			
			break;
		}
		case 0:{
			System.out.println("Exit");
			running =false;
			break;
		}
		default:
		System.out.println("Program ended");
		break;
		
		}

		}
		catch(InputMismatchException e) {
			System.out.println("error "+e.getMessage());
		}
		
		
	}

	}
}

