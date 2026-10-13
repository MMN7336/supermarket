package months1;

import java.util.ArrayList;
import java.util.Scanner;

public class studentManin {

	public static void main(String[] args) {
		boolean found=false;
		// TODO Auto -generated method stub
		Scanner sc=new Scanner(System.in);
		ArrayList<student>students = new ArrayList<>();
		students.add(new student("Mg Mg",12,90));
		students.add(new student("Hla Hla",12,95));
		students.add(new student("Htay Htay ",12,67));
		System.out.println("Enter you search student name");
		for(student student:students) {
			System.out.println(student.getName());
			System.out.println(student.getAge());
			System.out.println(student.getMark());
		}
		//search
		System.out.println("Enter search student name");
		String name =sc.nextLine();
		for(student s:students) {
			if(name.equals(s.getName())) {
				System.out.println(s.getName());
				System.out.println(s.getAge());
				System.out.println(s.getMark());
				found=true;
			}
			
		}
		if(found== false) {
			System.out.println("Student not found");
		}
		//update
		System.out.println("Enter student name");
		String Name =sc.nextLine();
		System.out.println("Enter student new marks");
		int marks=sc.nextInt();
		for(student s:students) {
			if(Name.equals(s.getName())) {
				s.setMark(marks);
				System.out.println("Successfull");
				System.out.println(s.getName());
				System.out.println(s.getAge());
				System.out.println(s.getMark());
				found=true;
			}
		}
		if(!found) {
			System.out.println("Student not found");
		}
		
	}

}
 