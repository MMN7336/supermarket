package miniproject;

public class User {
	private String userId;
	private String name;
	private String email;
	public User(String userId,String name,String email) {
		this.userId=userId;
		this.name=name;
		this.email=email;
	}
	public void setUserId(String userid) {
		this.userId=userid;
	}
	public String getUserId() {
		return userId;
	}
	public void setName(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
	public void setEmail(String email) {
		this.email=email;
	}
	public String getEmail() {
		return email;
	}
}
