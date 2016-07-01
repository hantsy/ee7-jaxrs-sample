package com.hantsylab.example.ee7.blog.domain.repository;

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
