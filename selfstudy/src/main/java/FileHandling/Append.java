package FileHandling;

import java.io.FileWriter;
import java.io.IOException;

public class Append {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileWriter writer=new FileWriter("myinfo.txt",true);
			writer.write("\n Beginner java development");
			System.out.println("Successfull");
			writer.close();
		}
		catch(IOException e) {
			System.out.println("Error may be occurs");
		}

	}

}
