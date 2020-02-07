package com.example.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Article;
import com.example.domain.Comment;
import com.example.form.ArticleForm;
import com.example.form.CommentForm;
import com.example.repository.ArticleRepository;
import com.example.repository.CommentRepository;

@Controller
@RequestMapping("/")
public class BbsController {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private CommentRepository commentRepository;

	/**
	 * 記事とコメントを表示
	 * @param model
	 * @return bbsview.html
	 */
	@RequestMapping
	public String index(Model model) {
		List<Article> articleList = articleRepository.findAll();
		model.addAttribute("articleList", articleList);

		// commentListをarticleId毎に取得
		for(Article article : articleList) {
			List<Comment> commentList = commentRepository.findByArticleId(article.getId());
			article.setCommentList(commentList);
		}

		return "bbsview";
	}

	/**
	 * 記事投稿
	 * @param form
	 * @param model
	 * @return 掲示板画面
	 */
	@RequestMapping("/postarticle")
	public String insertArticle(ArticleForm form, Model model) {
		Article article = new Article();
		BeanUtils.copyProperties(form, article);
		articleRepository.insert(article);
		return "redirect:/";
	}

	/**
	 * コメント投稿
	 * @param form
	 * @param model
	 * @return 掲示板画面
	 */
	@RequestMapping("/postcomment")
	public String insertComment(CommentForm form, Model model) {
		Comment comment = new Comment();
		BeanUtils.copyProperties(form, comment);
		commentRepository.insert(comment);
		return "redirect:/";
	}

	/**
	 * 記事&コメント削除
	 * @param form
	 * @return 掲示板画面
	 */
	@RequestMapping("/deletearticle")
	public String deleteArticle(ArticleForm form) {
		commentRepository.delete(form.getId());
		articleRepository.delete(form.getId());
		return "redirect:/";
	}

}
