package tw.com.eeit.petforum.user.model.bean;

import java.io.Serializable;

public class Users implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String account;
	private String password;
	private String level;
	private String photo;

	public Users() {
	}

	public Users(String account, String password) {
		super();
		this.account = account;
		this.password = password;
	}

	public Users(String id, String account, String password, String level, String photo) {
		super();
		this.id = id;
		this.account = account;
		this.password = password;
		this.level = level;
		this.photo = photo;
	}

	public String getAccount() {
		return account;
	}

	public String getId() {
		return id;
	}

	public String getLevel() {
		return level;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoto() {
		return photo;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
