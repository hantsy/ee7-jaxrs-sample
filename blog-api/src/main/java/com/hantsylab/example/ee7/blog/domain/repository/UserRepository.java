package com.hantsylab.example.ee7.blog.domain.repository;

import com.hantsylab.example.ee7.blog.domain.model.Post;
import com.hantsylab.example.ee7.blog.domain.model.Post_;
import com.hantsylab.example.ee7.blog.domain.model.User;
import com.hantsylab.example.ee7.blog.domain.model.User_;
import com.hantsylab.example.ee7.blog.domain.support.AbstractRepository;
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

/**
 *
 * @author hantsy
 */
@Stateless
public class UserRepository extends AbstractRepository<User, Long> {

    @PersistenceContext
    EntityManager em;

    @Override
    protected EntityManager entityManager() {
        return this.em;
    }

    public List<User> findByKeyword(String keyword) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();

        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(keyword)) {
            predicates.add(
                cb.or(
                    cb.like(c.get(User_.firstName), '%' + keyword + '%'),
                    cb.like(c.get(User_.lastName), '%' + keyword + '%'),
                    cb.like(c.get(User_.username), '%' + keyword + '%')
                )
            );
        }

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        TypedQuery<User> query = em.createQuery(q);

        return query.getResultList();
    }

    public User findByUsername(String username) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();

        CriteriaQuery<User> q = cb.createQuery(User.class);
        Root<User> c = q.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(
            cb.equal(c.get(User_.username), username)
        );

        q.where(predicates.toArray(new Predicate[predicates.size()]));

        TypedQuery<User> query = em.createQuery(q);
        List<User> users = query.getResultList();

        if (users.size() == 1) {
            return users.get(0);
        }

        return null;
    }

}
