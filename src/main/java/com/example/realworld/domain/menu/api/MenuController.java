package com.example.realworld.domain.menu.api;

import com.example.realworld.domain.menu.dto.MenuResponseDto;
import com.example.realworld.domain.menu.dto.MenuSearchCond;
import com.example.realworld.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/")
    public ResponseEntity<Object> getMenus(MenuSearchCond cond, Pageable pageable) {
        Page<MenuResponseDto> menus = menuService.getMenus(cond, pageable);

        return ResponseEntity.ok(menus);
    }

}
