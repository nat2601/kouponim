package com.example.demo;

public class ExceptionResponse {

	private int code;
	private String description;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return description;
	}

	public void setMessage(String message) {
		this.description = message;
	}

	@Override
	public String toString() {
		return "ExceptionResponse [code=" + code + ", description=" + description + "]";
	}

}


