package com.technews.aggregate.releases.springframework.contoller;

import com.technews.common.constant.Categories;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/spring-project")
@RequiredArgsConstructor
public class SpringFrameworkReleaseController {

//    @ResponseBody
//    @GetMapping("/{category}")
//    public ResponseEntity<PageResponse> scrollList(
//            @PathVariable String category,
//            @PageableDefault(page = 0, size = 10) Pageable pageable) {
//
//        Page<Product> productListPage = productService.findAllSortByIdDescPaging(category.toUpperCase(), pageable.getPageNumber(), pageable.getPageSize());
//        List<Object> resultList = productListPage.getContent().stream()
//                .map(product -> new PhotosDto.Response(product))
//                .collect(Collectors.toList());
//
//        PageResponse pageResponse = PageResponse.builder()
//                .code(HttpStatus.OK.value())
//                .httpStatus(HttpStatus.OK)
//                .message("성공적으로 조회되었습니다.")
//                .count(resultList.size())
//                .totalElements(productListPage.getTotalElements())
//                .totalPages(productListPage.getTotalPages())
//                .result(resultList)
//                .build();
//
//        return new ResponseEntity<>(pageResponse, pageResponse.getHttpStatus());
//    }
}
