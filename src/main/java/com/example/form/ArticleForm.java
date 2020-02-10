package com.example.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ArticleForm {
	private Integer id;
	@NotNull(message="投稿者名を入力してください")
	@Size(min=1, max=127, message="投稿者名は1文字以上127文字以下で入力してください")
	private String name;
	@NotNull(message="投稿内容を入力してください")
	@Size(min=3, message="投稿内容は3文字以上入力してください")
	private String content;

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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
