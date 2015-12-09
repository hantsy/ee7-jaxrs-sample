package com.hantsylabs.angularjs.ee7.sample.rest.dto;

import java.io.Serializable;
import com.hantsylabs.angularjs.ee7.sample.model.Member;
import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class MemberDTO implements Serializable {

	private Long id;
	private String name;
	private String phoneNumber;

	public MemberDTO() {
	}

	public MemberDTO(final Member entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.name = entity.getName();
			this.phoneNumber = entity.getPhoneNumber();
		}
	}

	public Member fromDTO(Member entity, EntityManager em) {
		if (entity == null) {
			entity = new Member();
		}
		entity.setName(this.name);
		entity.setPhoneNumber(this.phoneNumber);
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}