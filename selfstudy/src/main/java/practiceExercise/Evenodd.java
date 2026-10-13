package practiceExercise;

import java.util.Scanner;

public class Evenodd {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter Number");
		int num=sc.nextInt();
			if(num%2==0) {
				System.out.println(num +"  is even");
			}
			else
				System.out.println(num+"   is Odd");
	}


}
