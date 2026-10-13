package dateAndtime;

import java.time.LocalDate;

public class plusDays {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalDate date=LocalDate.of(2005,9,3);
		System.out.println(date.plusDays(7));
		System.out.println(date.minusDays(7));

	}

}
