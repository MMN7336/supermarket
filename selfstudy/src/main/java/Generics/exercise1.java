package Generics;

import java.util.ArrayList;

public class exercise1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Integer> number =new ArrayList<>();
		number.add(10);
		number.add(20);
		number.add(30);
		number.add(40);
		number.add(50);
		int total=0;
		for(Integer i:number) {
			total+=i;
		}
		System.out.println(total);
		
		
	}

}
