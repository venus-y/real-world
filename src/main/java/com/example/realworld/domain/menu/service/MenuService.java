package com.example.realworld.domain.menu.service;


import com.example.realworld.domain.menu.dto.MenuResponseDto;
import com.example.realworld.domain.menu.dto.MenuSearchCond;
import com.example.realworld.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public Page<MenuResponseDto> getMenus(MenuSearchCond cond, Pageable pageable) {
        return menuRepository.findMenuList(cond, pageable);
    }
}
