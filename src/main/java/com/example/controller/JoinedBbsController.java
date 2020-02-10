package com.example.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Article;
import com.example.domain.Comment;
import com.example.form.ArticleForm;
import com.example.form.CommentForm;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentRepository;

@Controller
@RequestMapping("/joined")
public class JoinedBbsController {
	@Autowired
	private ArticleRepository articleRepository;
	@Autowired
	private CommentRepository commentRepository;

	@ModelAttribute
	public ArticleForm setUpArticleForm() {
		return new ArticleForm();
	}
	@ModelAttribute
	public CommentForm setUpCommentForm() {
		return new CommentForm();
	}

	/**
	 * 掲示板画面表示
	 * @param model
	 * @return 掲示板画面
	 */
	@RequestMapping
	public String index(Model model) {
		List<Article> articleList = articleRepository.joinFindAll();
		model.addAttribute("articleList", articleList);
		return "joinebbsview";
	}

	/**
	 * 記事投稿
	 * @param form
	 * @return 掲示板画面
	 */
	@RequestMapping("/article")
	public String postarticle(@Validated ArticleForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return index(model);
		}
		Article article = new Article();
		BeanUtils.copyProperties(form, article);
		articleRepository.insert(article);
		return "redirect:/joined";
	}

	/**
	 * コメント投稿
	 * @param form
	 * @return 掲示板画面
	 */
	@RequestMapping("/comment")
	public String postcomment(@Validated CommentForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return index(model);
		}
		Comment comment = new Comment();
		BeanUtils.copyProperties(form, comment);
		commentRepository.insert(comment);
		return "redirect:/joined";
	}

	/**
	 * 記事&コメント削除
	 * @return 掲示板画面
	 */
	@RequestMapping("/delete")
	public String delete(Integer articleId) {
		articleRepository.joindelete(articleId);
		return "redirect:/joined";
	}


}
