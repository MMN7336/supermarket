package Hashmash;

import java.util.HashMap;
import java.util.Map;

public class exe1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String ,Integer> students=new HashMap<>();
		students.put("Mg Mg",23);
		students.put("Hla Hla", 45);
		students.put("Htat Myat",12);
		System.out.println(students.get("Hla Hla"));
		if(students.containsKey("Htat Myat")) {
			System.out.println("Found");
		}
		else 
			System.out.println("Not found");
		for(Map.Entry<String,Integer> entry:students.entrySet()) {
			System.out.println(entry.getKey()+":" + entry.getValue());
		}

	}

}
