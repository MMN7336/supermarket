package months1;

public class student {
	
	private String name;
	private int age;
	private int mark;
	public student(String name,int age, int mark) {
		this.name=name;
		this.age=age;
		this.mark=mark;
	}
	public void setName(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setAge(int age) {
		this.age=age;
	}
	public int getAge() {
		return age;
	}
	public void setMark(int mark) {
		this.mark=mark;
	}
	public int getMark() {
		return mark;
	}

}
