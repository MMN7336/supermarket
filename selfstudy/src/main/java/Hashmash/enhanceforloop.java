package Hashmash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class enhanceforloop {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<student >students=new ArrayList<>();
		students.add(new student("Mg Mg"));
		students.add(new student("Mg Htay"));
		students.add(new student("Mg Aung"));
		students.add(new student("Mg Hla"));
		for(student names:students) {
			System.out.println(names.getName());
			
		}
		HashSet<String >student=new HashSet<>();
		student.add("Ma Hla");
		student.add("Ma Mya");
		student.add("Ma Dail");
		for(String Name:student) {
			System.out.println(Name);
			
		}
		
	}

}
