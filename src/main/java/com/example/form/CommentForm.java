package com.example.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentForm {
	private Integer id;
	@NotNull(message="コメント者名を入力してください")
	@Size(min=1, max=127, message="コメント者名は1文字以上127文字以内で入力してください")
	private String name;
	@NotNull(message="コメントを入力してください")
	@Size(min=3, message="コメントは3文字以上入力してください")
	private String content;
	private Integer articleId;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getArticleId() {
		return articleId;
	}
	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	@Override
	public String toString() {
		return "CommentForm [id=" + id + ", name=" + name + ", content=" + content + ", articleId=" + articleId + "]";
	}

}
