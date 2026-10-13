package practiceExercise;

import java.util.Scanner;

public class Array1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[ ] number=new int[10];
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter 10 number");
		for(int i=0;i<number.length;i++) {
			number[i]=sc.nextInt();
		}
		int max=number[0];
		int min=number[0];
		for(int i=0;i<10;i++) {
			if(max<number[i]) {
				max=number[i];
			}
		}
		System.out.println(max);
		for(int i=0;i<10;i++) {
			if(min>number[i]) {
				min=number[i];
			}
		}
		System.out.println(min);
		int sum=0;
		for(int i=0;i<10;i++) {
			sum+=number[i];
		}
		System.out.println(sum);
		double avg=(double)sum/number.length;
		System.out.println(avg);
		

	}

}
