package com.hantsylabs.angularjs.ee7.sample.rest.dto;

import java.io.Serializable;
import com.hantsylabs.angularjs.ee7.sample.Post;
import javax.persistence.EntityManager;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class PostDTO implements Serializable {

	private Long id;
	private int version;
	private String title;
	private String content;
	private Date createdOn;

	public PostDTO() {
	}

	public PostDTO(final Post entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.version = entity.getVersion();
			this.title = entity.getTitle();
			this.content = entity.getContent();
			this.createdOn = entity.getCreatedOn();
		}
	}

	public Post fromDTO(Post entity, EntityManager em) {
		if (entity == null) {
			entity = new Post();
		}
		entity.setVersion(this.version);
		entity.setTitle(this.title);
		entity.setContent(this.content);
		entity.setCreatedOn(this.createdOn);
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
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

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}
}