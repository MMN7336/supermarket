package practiceExercise;

import java.util.ArrayList;

public class ArrayList1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Integer> Number=new ArrayList<>();
		Number.add(12);
		Number.add(23);
		Number.add(42);
		Number.add(32);
		Number.add(43);
		Number.add(13);
		Number.add(11);
		int max=Number.get(0);
		int min=Number.get(0);
		for(Integer num:Number) {
			if(max<num) {
				max=num;
			}
		}
		System.out.println("The Largest number is :"+ max);
		for(Integer i:Number) {
			if(min>i) {
				min=i;
			}
		}
		System.out.println("The smallest number is:" + min);
	}

}
