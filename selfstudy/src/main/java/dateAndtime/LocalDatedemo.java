package dateAndtime;

import java.time.LocalDate;

public class LocalDatedemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalDate date=LocalDate.of(2024, 2,29);
		LocalDate nextDate=date.plusDays(1);
		System.out.println(date);
		System.out.println("Leap year   "+ date.isLeapYear());
		System.out.println(nextDate);
		System.out.println(date.isBefore(nextDate));
		}
	
}
