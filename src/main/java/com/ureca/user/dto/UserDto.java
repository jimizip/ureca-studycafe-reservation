package com.ureca.user.dto;

// 사용자 정보 (세션 저장 + 엔티티 겸용)
public class UserDto {
	private int id;
	private String name;
	private String tel;
	private String email;
	private String password;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getTel() { return tel; }
	public void setTel(String tel) { this.tel = tel; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	@Override
	public String toString() {
		return "UserDto [id=" + id + ", name=" + name + ", tel=" + tel + ", email=" + email + "]";
	}
}
