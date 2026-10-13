package FileHandling;

import java.io.FileWriter;
import java.io.IOException;

public class file2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileWriter writer=new FileWriter("test.txt");
			writer.write("My name is Ei Ei Phyo Hlaing");
			writer.write("I am a student");
			writer.close();
		}
		catch(IOException e) {
			System.out.println("Error may be occurs");
		}

	}

}
