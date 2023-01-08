package com.reproducer;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAbleToQueryWithoutDistinct()
    {

        Specification<User> specification = (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            Join<User, LoginAttempt> attemptJoin = root.join("loginAttempts");
            predicates.add(criteriaBuilder.equal(attemptJoin.get("risk"), "high"));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        userRepository.findAll(specification, Sort.by("loginAttempts.attemptAt"));
    }

    @Test
    public void testAbleToQueryWithDistinct()
    {

        Specification<User> specification = (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            Join<User, LoginAttempt> attemptJoin = root.join("loginAttempts");
            predicates.add(criteriaBuilder.equal(attemptJoin.get("risk"), "high"));
            query.distinct(true);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        userRepository.findAll(specification, Sort.by("loginAttempts.attemptAt"));
    }

}
