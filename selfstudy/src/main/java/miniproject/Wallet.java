package miniproject;

public class Wallet {
	private String walletId;
	private String userId;
	private double balance;
	public Wallet(String walletId,String userId,double balance) {
		this.walletId=walletId;
		this.userId=userId;
		this.balance=balance;
	}
	public void setUserId(String userid) {
		this.userId=userid;
	}
	public String getUserId() {
		return userId;
	}
	public void setWalletId(String walletid) {
		this.walletId=walletid;
	}
	public String getWalletId() {
		return walletId;
	}
	public void setBalance(double balance) {
		this.balance=balance;
	}
	public double getBalance() {
		return balance;
	}
}
