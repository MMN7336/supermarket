package exception;

public class throwsdemo {
	static void chrecknum( ) throws InterruptedException{
		Thread.sleep(2000);
		System.out.println("Number check successfull");
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			chrecknum();
		}
		catch(InterruptedException e) {
			System.out.println("Thread was interrupt");
			
		}
		

	}

}
