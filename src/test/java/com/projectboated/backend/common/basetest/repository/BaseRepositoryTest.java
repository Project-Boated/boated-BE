package com.projectboated.backend.common.basetest.repository;

import com.projectboated.backend.common.basetest.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

@DataJpaTest
public class BaseRepositoryTest extends BaseTest {

    @Autowired
    protected EntityManager em;

    protected void flushAndClear() {
        em.flush();
        em.clear();
    }

}
