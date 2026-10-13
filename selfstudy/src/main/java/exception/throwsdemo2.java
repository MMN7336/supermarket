package exception;

public class throwsdemo2 {
	static void waitandprint() throws InterruptedException{
		Thread.sleep(3000);
		System.out.println("Task complete");
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			waitandprint();
		}
		catch(InterruptedException e) {
			System.out.println("Thread was interrupted");
		}

	}

}
