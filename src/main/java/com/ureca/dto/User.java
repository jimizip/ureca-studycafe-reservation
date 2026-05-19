package com.ureca.dto;

// 사용자 정보 Dto
public class User {
	private int id; // user id
    private String name; // user name
    private String tel; // user phone number
    private String email; // user email

    public User() {}

    public User(int id, String name, String tel, String email) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", tel=" + tel + ", email=" + email;
    }

}
