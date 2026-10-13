package Generics;

import java.util.ArrayList;

public class students {
	private String name;
	private int age;
	public students(String name,int age) {
		this.name=name;
		this.age=age;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList <students> info=new ArrayList<>();
		info.add(new students("Mg Mg" ,12));
		info.add(new students("Mg Aung" ,34));
		info.add(new students("Ma Hla" ,24));
		info.add(new students("Ma Su" ,25));
		int min=info.get(0).age;
		String  names=info.get(0).name;
		for (students s:info) {
			if(min>s.age) {
				min=s.age;
				names=s.name;
			}
			
			
		}
		System.out.println("Student name  :"+names);
		System.out.println("Oldest age"+min);
		

	}

}
