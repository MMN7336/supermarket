package dateAndtime;

import java.time.LocalDateTime;

public class LocalDateAndTime {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalDateTime datetime=LocalDateTime.of(2001,2,4, 12,33,23);
		System.out.println(datetime);
		System.out.println(datetime.plusDays(5));
		System.out.println(datetime.plusHours(3));

	}

}
