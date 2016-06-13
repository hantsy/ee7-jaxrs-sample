package com.hantsylab.example.ee7.blog.repository;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.hantsylab.example.ee7.blog.model.Post;
import com.hantsylab.example.ee7.blog.model.Post_;

@Stateless
public class PostRepository {

	@PersistenceContext
	private EntityManager em;

	public List<Post> findByKeyword(String keyword) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();

		CriteriaQuery<Post> q = cb.createQuery(Post.class);
		Root<Post> c = q.from(Post.class);

		List<Predicate> predicates = new ArrayList<>();

		if (StringUtils.isNotBlank(keyword)) {
			predicates.add(
					cb.or(
							cb.like(c.get(Post_.title), '%' + keyword + '%'),
							cb.like(c.get(Post_.content), '%' + keyword + '%')
						)
					);
		}

		q.where(predicates.toArray(new Predicate[predicates.size()]));

		TypedQuery<Post> query = em.createQuery(q);

		return query.getResultList();
	}

	public Post save(Post entity) {
		if (entity.getId() == null) {
			em.persist(entity);

			return entity;
		} else {
			return em.merge(entity);
		}
	}

	public Post findById(Long id) {
		return em.find(Post.class, id);
	}

	public void delete(Post entity) {
		Post _post = em.merge(entity);
		em.remove(_post);
	}

}
