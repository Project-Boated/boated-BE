package com.projectboated.backend.domain.project.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.condition.GetMyProjectsCond;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.projectboated.backend.domain.project.entity.QAccountProject.accountProject;
import static com.projectboated.backend.domain.project.entity.QProject.project;

@Repository
public class ProjectQueryDslRepositoryImpl implements ProjectQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public ProjectQueryDslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Project> getMyProjects(Account account, GetMyProjectsCond cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!cond.hasCond() || cond.isCaptainTerm()) {
            builder.or(project.captain.eq(account).and(isProjectTerminated()));
        }
        if (!cond.hasCond() || cond.isCaptainNotTerm()) {
            builder.or(project.captain.eq(account).and(isProjectNotTerminated()));
        }
        if (!cond.hasCond() || cond.isCrewTerm()) {
            builder.or(project.id.in(
                            JPAExpressions
                                    .select(accountProject.project.id)
                                    .from(accountProject)
                                    .where(accountProject.account.eq(account)))
                    .and(isProjectTerminated()));
        }
        if (!cond.hasCond() || cond.isCrewNotTerm()) {
            builder.or(project.id.in(
                            JPAExpressions
                                    .select(accountProject.project.id)
                                    .from(accountProject)
                                    .where(accountProject.account.eq(account)))
                    .and(isProjectNotTerminated()));
        }

        Pageable pageable = cond.getPageable();
        return queryFactory
                .selectFrom(project)
                .where(builder)
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "name":
                        orderSpecifiers.add(new OrderSpecifier(direction, project.name));
                        break;
                    case "createdDate":
                        orderSpecifiers.add(new OrderSpecifier(direction, project.createdDate));
                        break;
                    case "deadline":
                        orderSpecifiers.add(new OrderSpecifier(direction, project.deadline));
                        break;
                }
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

    private Predicate isProjectNotTerminated() {
        return project.isTerminated.eq(false);
    }

    private Predicate isProjectTerminated() {
        return project.isTerminated.eq(true);
    }

}
