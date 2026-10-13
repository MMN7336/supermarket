package FileHandling;

import java.io.File;


public class DeletefileDemo {

	public static void main(String[] args) {
		File file=new File("test.txt");
		if(file.exists()) {
			if(file.delete()) {
				System.out.println("File delete successfull");
			}
			else {
				System.out.println("Fail delete file");
			}
		}
		else {
			System.out.println("File does not exist");
		}

	}

}
