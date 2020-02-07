package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Comment;

@Repository
public class CommentRepository {

	private static final RowMapper<Comment> COMMENT_ROWMAPPER = (rs, i) -> {
		Integer id = rs.getInt("id");
		String name = rs.getString("name");
		String content = rs.getString("content");
		Integer articleId = rs.getInt("article_id");
		return new Comment(id, name, content, articleId);
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * ArticleIdでコメント取得
	 */
	public List<Comment> findByArticleId(int articleId) {
		String sql = "SELECT id, name, content, article_id FROM comments WHERE article_id=:articleId ORDER BY id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", articleId);
		List<Comment> commentList = template.query(sql, param, COMMENT_ROWMAPPER);
		return commentList;
	}

	/**
	 * コメントを投稿
	 * @param comment
	 */
	public void insert(Comment comment) {
		String sql = "INSERT INTO comments(name, content, article_id) VALUES(:name, :content, :articleId)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(comment);
		template.update(sql, param);
	}

	public void delete(Integer id) {
		String sql = "DELETE FROM comments WHERE article_id=:articleId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("articleId", id);
		template.update(sql, param);
	}

}
