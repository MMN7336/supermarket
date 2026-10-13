package FileHandling;

import java.io.FileWriter;
import java.io.IOException;

public class FileHandlingDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		FileWriter writer=new FileWriter("Myinfo.txt");
		writer.write("University: Computer University  \n");
		writer.write("Goal: Java Backend Developer");
		writer.close();
		}
		catch(IOException e) {
			System.out.println("Error may be occurs");
		}
	}

}
