package com.maktabsharif.homeservices.repository;

import com.maktabsharif.homeservices.domain.*;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom{

    @PersistenceContext
      EntityManager entityManager;



    @Override
    public List<User> findUserByUserRoleAndNameAndFamilyNameAndEmailAndExpertPoint(User user, String subserviceTitle, Integer lowExpertPoint, Integer highExpertPoint) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cqUser = cb.createQuery(User.class);
        CriteriaQuery<Expert> cqExpert = cb.createQuery(Expert.class);

        Root<User> searchUser = cqUser.from(User.class);
        Root<Expert> searchExpert = cqExpert.from(Expert.class);
        List<Predicate> predicates = new ArrayList<>();

        if (user.getRole() != null) {
            predicates.add(cb.like(searchUser.get("role"), "%" + user.getRole() + "%"));
        }
        if (user.getName() != null) {
            predicates.add(cb.like(searchUser.get("name"), "%" + user.getName() + "%"));
        }
        if (user.getFamilyName() != null) {
            predicates.add(cb.like(searchUser.get("familyName"), "%" + user.getFamilyName()+ "%"));
        }
        if (user.getEmail() != null) {
            predicates.add(cb.like(searchUser.get("email"), "%" + user.getEmail() + "%"));
        }
        if (subserviceTitle != null) {
            Join<Expert, Subservices> subservices = searchExpert.join("subservices");
            predicates.add(cb.like(subservices.<String>get("subserviceTitle"), "%"+subserviceTitle+"%"));
        }
        if (lowExpertPoint != null && highExpertPoint != null) {
            predicates.add(cb.between(searchExpert.get("expertPoint"),  lowExpertPoint, highExpertPoint));
        }
        cqUser.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cqUser).getResultList();
    }

    @Override
    public List<Client> findUserRegisterDateAndRedisteredOrdersCountAndCompletedOrdersCount(Timestamp registerDte, Integer registeredOrders, Integer completedOrders) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> cqClient = cb.createQuery(Client.class);
        CriteriaQuery<Expert> cqExpert = cb.createQuery(Expert.class);

        Root<Client> searchClient = cqClient.from(Client.class);
        Root<Expert> searchExpert = cqExpert.from(Expert.class);
        List<Predicate> predicates = new ArrayList<>();

        if (registerDte != null) {
            predicates.add(cb.equal(searchClient.get("registerDate"), registerDte));
            predicates.add(cb.equal(searchExpert.get("registerDate"), registerDte));
        }
        if (registeredOrders != null) {
            Join<Client, Orders> orders = searchClient.join("orders");
            predicates.add(cb.equal(cb.count(orders.<Integer>get("id")), registeredOrders));
        }
        if (completedOrders != null) {
            Join<Expert, Orders> orders = searchExpert.join("orders");
            predicates.add(cb.equal(cb.count(searchExpert.<Integer>get("id")), registeredOrders));
        }
        cqClient.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cqClient).getResultList();
    }
}
