package com.springjpacrud;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springjpacrud.domain.QQTypeTestEntity;
import com.springjpacrud.domain.QTypeTestEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class QueryDslTest {

    @Autowired
    EntityManager em;

    @Test
    public void testQueryDsl() {
        QTypeTestEntity qType = new QTypeTestEntity();
        qType.setAge(10);
        qType.setName("TEST1");
        em.persist(qType);

        JPAQueryFactory query = new JPAQueryFactory(em);
        QQTypeTestEntity qTyeTest = QQTypeTestEntity.qTypeTestEntity;

        QTypeTestEntity result = query.selectFrom(qTyeTest)
                .fetchOne();

        assertThat(result).isEqualTo(qType);
        assertThat(result.getId()).isEqualTo(qType.getId());
    }

    @Test
    void testJPQL() {
        QTypeTestEntity qType = new QTypeTestEntity();
        qType.setAge(10);
        qType.setName("TEST1");
        em.persist(qType);

        QTypeTestEntity resultJPQL = em.createQuery(
                        "select m from QTypeTestEntity m " +
                                "where m.name = :name", QTypeTestEntity.class)
                .setParameter("name", "TEST1")
                .getSingleResult();

        assertThat(resultJPQL.getName()).isEqualTo("TEST1");
    }
}
