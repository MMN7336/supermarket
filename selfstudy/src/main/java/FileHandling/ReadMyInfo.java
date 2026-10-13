package FileHandling;

import java.io.FileReader;
import java.io.IOException;

public class ReadMyInfo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
		FileReader reader=new FileReader("myinfo.txt");
		int count=0;
		int character;
		while((character=reader.read()) !=-1) {
			count++;
			System.out.print((char)character);
			
		}
		System.out.println("Number"+count);
		reader.close();
		}
		catch(IOException e) {
			System.out.println("Error may be occurs");			
		}
		

	}

}
