package com.pointofsale.dataSupplier.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pointofsale.dataSupplier.dto.request.NewCategoryRequest;
import com.pointofsale.dataSupplier.dto.response.CategoryResponse;
import com.pointofsale.dataSupplier.dto.response.CommonResponse;
import com.pointofsale.dataSupplier.dto.response.PaginationResponse;
import com.pointofsale.dataSupplier.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "CATEGORY", description = "methods of Category APIs")
public class CategoryController {
    
    private final CategoryService categoryService;

    @Operation(summary = "Create category", description = "Create category")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Successfully created category", 
                content = @Content(schema = @Schema(implementation = CommonResponse.class))),
    })
    @PostMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createCategory(@RequestBody NewCategoryRequest request) {
        CategoryResponse category = categoryService.saveCategory(request);

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully created category")
                .data(category)
                .build();

        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
    }
    

    @Operation(summary = "Get all categories", description = "Get all categories")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Get all categories successfully", 
                content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CommonResponse.class)) }),
    })
    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
        )
    public ResponseEntity<?> getAllCategories(@RequestParam(value = "category", required = false) String category,
                                              @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        
        Page<CategoryResponse> responsePage = categoryService.findAllCategories(category, page, size);
        
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Successfully get all categories")
                .data(responsePage.getContent())
                .pagination(PaginationResponse.builder()
                        .count(responsePage.getTotalElements())
                        .totalPage(responsePage.getTotalPages())
                        .page(responsePage.getNumberOfElements())
                        .size(responsePage.getSize())
                        .build())
                .build();
        
        return ResponseEntity.ok(response);
    }
    
}
