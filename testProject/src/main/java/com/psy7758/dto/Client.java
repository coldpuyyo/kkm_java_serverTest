package com.psy7758.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Client {
	private int num;
	private String id;
	private String pwd;
	private String name;
	private String phoneNum;
	private LocalDate birthDate;
	private int totPoint;
	private LocalDateTime regDate;
	private boolean pub;

	public Client() {
	}

	public Client(int num, String id, String pwd, String name, String phoneNum, LocalDate birthDate, int totPoint,
			LocalDateTime regDate, boolean pub) {
		this.num = num;
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.phoneNum = phoneNum;
		this.birthDate = birthDate;
		this.totPoint = totPoint;
		this.regDate = regDate;
		this.pub = pub;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public int getTotPoint() {
		return totPoint;
	}

	public void setTotPoint(int totPoint) {
		this.totPoint = totPoint;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public boolean getPub() {
		return pub;
	}

	public void setPub(boolean pub) {
		this.pub = pub;
	}

	@Override
	public String toString() {
		return "Client [num=" + num + ", id=" + id + ", pwd=" + pwd + ", name=" + name + ", phoneNum=" + phoneNum
				+ ", birthDate=" + birthDate + ", totPoint=" + totPoint + ", regDate=" + regDate + ", pub=" + pub
				+ "]\n";
	}
}
