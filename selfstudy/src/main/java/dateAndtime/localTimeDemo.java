package dateAndtime;

import java.time.LocalTime;

public class localTimeDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalTime time=LocalTime.of(12,10,0);
		System.out.println(time.getHour());
		System.out.println(time.getMinute());
		System.out.println(time.plusHours(2));

	}

}
