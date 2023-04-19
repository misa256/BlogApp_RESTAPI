package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDTO;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
       Category category = mapper.map(categoryDTO, Category.class);
       Category savedCategory = categoryRepository.save(category);
       return  mapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO getCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category category = optionalCategory.orElseThrow(
                ()->{throw new ResourceNotFoundException("Category","id",categoryId);});
        return  mapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList =categories.stream()
                .map(category ->
                 mapper.map(category,CategoryDTO.class))
                .collect(Collectors.toList());
        return  categoryDTOList;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                    new ResourceNotFoundException("Category","id",categoryId)
                );
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory, CategoryDTO.class);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException("Category","id",categoryId)
                );
        categoryRepository.delete(category);
    }
}
