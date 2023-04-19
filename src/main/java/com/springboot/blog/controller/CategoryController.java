package com.springboot.blog.controller;

import com.springboot.blog.payload.CategoryDTO;
import com.springboot.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //Build Add Category REST API
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategory = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    //Build Get Category REST API
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable(name = "id") Long categoryId){
        CategoryDTO categoryDTO = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(categoryDTO);
    }

    //Build All Categories REST API
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    //Build update category REST API
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    public ResponseEntity<CategoryDTO> updateCategory(
            @RequestBody CategoryDTO categoryDTO,
            @PathVariable(name = "id") Long categoryId){
        CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return ResponseEntity.ok(updatedCategoryDTO);
    }

    //Build delete category REST API
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id") Long categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category deleted successfully!");
    }
}
