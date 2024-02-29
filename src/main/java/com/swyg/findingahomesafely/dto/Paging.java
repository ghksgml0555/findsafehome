package com.swyg.findingahomesafely.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
public class Paging {

    public int totalPages;
    private Long totalElements;
    private int pageSize;
    private int pageNumber;

}
