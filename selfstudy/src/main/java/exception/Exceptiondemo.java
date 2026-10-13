package exception;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Exceptiondemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter number");
		try {
			int num=sc.nextInt();
			System.out.println("Your number is "+ num);
		}
		catch (InputMismatchException e) {
			System.out.println("Please Enter integer number");
		}
		finally {
			System.out.println("Program finish");
		}

	}

}
