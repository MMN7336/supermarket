package months1;

import java.util.ArrayList;
import java.util.Scanner;

public class removeMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		boolean found=false;
		ArrayList<student> students=new ArrayList<>();
		students.add(new student("Htay Htay",13,56));
		students.add(new student("Aung Aung",13,67));
		students.add(new student("Mya Hla",13,89));
		System.out.println("Enter student name to delete");
		String name=sc.nextLine();
		for(student s:students) {
			if(name.equals(s.getName())) {
				students.remove(s);
				System.out.println("Delete successfull");
				found=true;
			}
		}
		if(!found) {
			System.out.println("Student not found");
		}
		for(student student:students) {
			System.out.println(student.getName());
			System.out.println(student.getAge());
			System.out.println(student.getMark());
		}
	}

}
