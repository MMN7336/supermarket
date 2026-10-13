package practiceExercise;

public class MultiArray {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][] student= {{3,2,1},{4,5,1},{2,5,6}};
		double highest=-1;
		int highestStudent=0;
		for(int i=0;i<student.length;i++) {
			int sum=0;
			for(int j=0;j<student[i].length;j++) {
				sum+=student[i][j];
			}
			double avg=(double) sum/student[i].length;
			System.out.println("student "+(i+1)+"   "+ sum);
			System.out.println("Student"+(i+1)+"average mark "+avg );
			if(avg>highest) {
				highest=(double)avg;
				highestStudent=i+1;
				
			}
		}
		System.out.println("Highest average mark is :"+ highest);
		System.out.println("Highest student is :"+ highestStudent);
		
		

	}

}
