package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Article;
import com.example.domain.Comment;

@Repository
public class ArticleRepository {

	private static final RowMapper<Article> ARTICLE_ROWMAPPER = (rs, i) -> {
		Integer id = rs.getInt("id");
		String name = rs.getString("name");
		String content = rs.getString("content");
		ArrayList<Comment> commentList = new ArrayList<>();
		return new Article(id, name, content, commentList);
	};

	/* SQL一括発行のROW MAPPER */
	private static final ResultSetExtractor<List<Article>> RESULT_SET_EXTRATOR = (rs) -> {
		ArrayList<Article> articleList = new ArrayList<Article>();
		ArrayList<Comment> commentList = null;
		long beforeArticleId = 0;
		while(rs.next()) {
			int nowArticleId = rs.getInt("id");
			if(nowArticleId != beforeArticleId) { //articles.idが0でない時
				Article article = new Article();
				article.setId(rs.getInt("id"));
				article.setName(rs.getString("name"));
				article.setContent(rs.getString("content"));
				commentList = new ArrayList<Comment>(); //作成したnullのCommentListにCommentオブジェクトをつめる
				article.setCommentList(commentList);
				articleList.add(article); //List<Article>に上の値をaddする
			}
			if(rs.getInt("c_id") != 0) { //comment.idが0でない時
				Comment comment = new Comment();
				comment.setId(rs.getInt("c_id"));
				comment.setName(rs.getString("c_name"));
				comment.setContent(rs.getString("c_content"));
				commentList.add(comment);
			}
			beforeArticleId = nowArticleId;
		}
		return articleList;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 記事全件取得
	 * @return 掲示板画面
	 */
	public List<Article> findAll() {
		String sql = "SELECT id, name, content FROM articles ORDER BY id DESC";
		List<Article> articleList = template.query(sql, ARTICLE_ROWMAPPER);
		return articleList;
	}

	/**
	 * 記事全件取得（一括）
	 * @return 掲示板画面
	 */
	public List<Article> joinFindAll() {
		String sql = "SELECT a.id, a.name, a.content, c.id c_id, c.name c_name, c.content c_content, c.article_id "
						+ "FROM articles a LEFT JOIN comments c ON a.id = c.article_id ORDER BY a.id DESC;";
		List<Article> articleList = jdbcTemplate.query(sql, RESULT_SET_EXTRATOR);
		return articleList;
	}

	/**
	 * 記事投稿
	 * @return article 追加する記事オブジェクト
	 */
	public void insert(Article article) {
		String sql = "INSERT INTO articles (name, content) VALUES (:name, :content)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(article);
		template.update(sql, param);
	}

	/**
	 * 記事削除
	 * @return
	 */
	public void delete(Integer id) {
		String sql = "DELETE FROM articles WHERE id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}

	/**
	 * 記事&コメント削除
	 * @param id 記事ID
	 */
	public void joindelete(Integer articleId) {
		String sql =  "WITH DELETED AS (DELETE FROM articles WHERE id=:id RETURNING id) "
				+ "DELETE FROM comments WHERE article_id IN (SELECT id FROM DELETED)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", articleId);
		template.update(sql, param);
	}
}
