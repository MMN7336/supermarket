package exception;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		
		try {
			System.out.println("Enter first number");
			int a=sc.nextInt();
			System.out.println("Enter second number");
			int b=sc.nextInt();
			int result=a/b;
			System.out.println(result);
		}
		catch(ArithmeticException e) {
			System.out.println("Can not divided by zero");
		}
		catch(InputMismatchException e) {
			System.out.println("Please enter integer number");
		}
		finally {
			System.out.println("Progrem Finish");
		}

	}

}
