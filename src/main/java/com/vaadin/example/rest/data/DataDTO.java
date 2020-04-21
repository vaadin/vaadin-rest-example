package com.vaadin.example.rest.data;

/**
 * DTO class for example 3, lazily fetching partial data with a DTO.
 */
public class DataDTO {

	private int id;
	private String title;
	private String message;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof DataDTO)) {
			return false;
		}

		return id == ((DataDTO) obj).id;
	}
}
