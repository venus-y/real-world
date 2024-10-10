package com.example.realworld.domain.order.repository;

import com.example.realworld.domain.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.realworld.domain.order.entity.QOrder.order;

@Repository
@RequiredArgsConstructor
public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> getOrderList(Pageable pageable) {

        long totalCount = queryFactory.selectFrom(order)
                .fetchCount();


        List<Order> orders = queryFactory.select(order)
                .from(order)
                .join(order.user)
                .fetchJoin()
                .join(order.shop)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        return new PageImpl<>(orders, pageable, totalCount);
    }
}
