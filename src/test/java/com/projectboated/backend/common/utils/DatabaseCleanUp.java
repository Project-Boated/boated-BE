package com.projectboated.backend.common.utils;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseCleanUp {
    @Autowired
    EntityManager em;

    private List<String> tables;

    @PostConstruct
    public void init() {
        tables = em.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
        tables.remove("url_profile_image");
        tables.remove("upload_file_profile_image");
        tables.remove("kakao_account");
        tables.remove("custom_kanban_lane");
        tables.remove("default_kanban_lane");
    }

    @Transactional
    public void execute() {
        em.flush();
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String table : tables) {
//            if(table.equals("kakao_account")) continue;
            em.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
        }

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
