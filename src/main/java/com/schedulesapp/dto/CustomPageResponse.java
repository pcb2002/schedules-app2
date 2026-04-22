package com.schedulesapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class CustomPageResponse<T> {

    private List<T> content;
    private PageInfo pageable;
    private long totalElements;
    private int totalPages;

    // 스프링의 기본 Page 객체를 받아서 필요한 데이터만 추출해 담는다.
    public CustomPageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageable = new PageInfo(page.getNumber(), page.getSize());
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    // 내부 클래스로 pageable 객체 구조를 맞춰준다.
    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private int pageNumber;
        private int pageSize;
    }
}