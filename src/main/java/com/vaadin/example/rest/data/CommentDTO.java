package com.vaadin.example.rest.data;

/**
 * DTO class for example 1, fetching using a DTO.
 */
public class CommentDTO {

	private int postId;
	private int id;
	private String name;
	private String email;
	private String body;

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof CommentDTO)) {
			return false;
		}

		return id == ((CommentDTO) obj).id;
	}
}
