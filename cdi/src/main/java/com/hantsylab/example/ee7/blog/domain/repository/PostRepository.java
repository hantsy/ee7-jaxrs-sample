package com.hantsylab.example.ee7.blog.domain.repository;

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

import com.hantsylab.example.ee7.blog.domain.model.Post;
import com.hantsylab.example.ee7.blog.domain.model.Post_;
import com.hantsylab.example.ee7.blog.domain.support.AbstractRepository;

@Stateless
public class PostRepository extends AbstractRepository<Post, Long> {

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

    @Override
    protected EntityManager entityManager() {
        return this.em;
    }

}
