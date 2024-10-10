package com.example.realworld.domain.menu.repository;

import com.example.realworld.domain.menu.dto.MenuResponseDto;
import com.example.realworld.domain.menu.dto.MenuSearchCond;
import com.example.realworld.domain.menu.dto.QMenuResponseDto;
import com.example.realworld.domain.menu.entity.MenuCategory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.realworld.domain.menu.entity.QMenu.menu;
import static org.springframework.util.StringUtils.isEmpty;

@Repository
public class CustomMenuRepositoryImpl implements CustomMenuRepository {

    private final JPAQueryFactory queryFactory;

    public CustomMenuRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MenuResponseDto> findMenuList(MenuSearchCond cond, Pageable pageable) {

        QueryResults<MenuResponseDto> results = queryFactory
                .select(new QMenuResponseDto(menu.name, menu.price, menu.category))
                .from(menu)
                .where(menuNameEq(cond.getMenuName()),
                        categoryEq(cond.getCategory())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MenuResponseDto> responseDtoList = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(responseDtoList, pageable, total);

    }

    private BooleanExpression categoryEq(MenuCategory category) {
        return category != null ? menu.category.eq(category) : null;
    }

    private Predicate menuNameEq(String menuName) {
        return isEmpty(menuName) ? null : menu.name.eq(menuName);
    }
}
