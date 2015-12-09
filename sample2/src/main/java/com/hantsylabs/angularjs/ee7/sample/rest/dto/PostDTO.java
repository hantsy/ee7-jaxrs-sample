package com.hantsylabs.angularjs.ee7.sample.rest.dto;

import java.io.Serializable;
import com.hantsylabs.angularjs.ee7.sample.model.Post;
import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class PostDTO implements Serializable {

	private Long id;
	private String title;
	private String content;
	private int version;

	public PostDTO() {
	}

	public PostDTO(final Post entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.title = entity.getTitle();
			this.content = entity.getContent();
			this.version = entity.getVersion();
		}
	}

	public Post fromDTO(Post entity, EntityManager em) {
		if (entity == null) {
			entity = new Post();
		}
		entity.setTitle(this.title);
		entity.setContent(this.content);
		entity.setVersion(this.version);
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}
}