package com.example.realworld.domain.menu.repository;

import com.example.realworld.domain.menu.dto.MenuResponseDto;
import com.example.realworld.domain.menu.dto.MenuSearchCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMenuRepository {

    Page<MenuResponseDto> findMenuList(MenuSearchCond cond, Pageable pageable);
}
