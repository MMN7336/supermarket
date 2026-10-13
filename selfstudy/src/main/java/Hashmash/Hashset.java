package Hashmash;

import java.util.HashSet;
import java.util.Map;

public class Hashset {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashSet<String> students=new HashSet<>();
		students.add("Maung Maung");
		students.add("Hla Hla");
		students.add("Maung Maung");
		if(students.contains("Maung Maung")) {
			System.out.println("Found");
		}
		else
			System.out.println("Not found");
		if(students.remove("Hla Hla")) {
			System.out.println("Delete successfull");
		}
		else {
			System.out.println("not found");
		}
		System.out.println(students);
		
	}

}
