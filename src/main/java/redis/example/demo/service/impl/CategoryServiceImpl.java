package redis.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.example.demo.entity.Category;
import redis.example.demo.exception.CategoryException;
import redis.example.demo.repository.CategoryRepository;
import redis.example.demo.service.extend.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private static final String CACHE_NAME = "categories";

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'all'")
    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            throw new CategoryException("Error occurred while fetching all categories", e);
        }
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#id")
    public Optional<Category> getCategoryById(Long id) {
        if (id == null || id <= 0) {
            throw new CategoryException("Invalid category ID");
        }
        try {
            return categoryRepository.findById(id);
        } catch (Exception e) {
            throw new CategoryException("Error occurred while fetching category with id: " + id, e);
        }
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Category createCategory(Category category) {
        validateCategory(category);
        
        try {
            if (categoryRepository.existsByName(category.getName())) {
                throw new CategoryException("Category with name '" + category.getName() + "' already exists");
            }
            return categoryRepository.save(category);
        } catch (CategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new CategoryException("Error occurred while creating category", e);
        }
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public Category updateCategory(Long id, Category categoryDetails) {
        if (id == null || id <= 0) {
            throw new CategoryException("Invalid category ID");
        }
        
        validateCategory(categoryDetails);
        
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new CategoryException("Category not found with id: " + id));

            if (!category.getName().equals(categoryDetails.getName()) && 
                categoryRepository.existsByName(categoryDetails.getName())) {
                throw new CategoryException("Category with name '" + categoryDetails.getName() + "' already exists");
            }

            category.setName(categoryDetails.getName());
            category.setDescription(categoryDetails.getDescription());

            return categoryRepository.save(category);
        } catch (CategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new CategoryException("Error occurred while updating category with id: " + id, e);
        }
    }

    @Override
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void deleteCategory(Long id) {
        if (id == null || id <= 0) {
            throw new CategoryException("Invalid category ID");
        }
        
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new CategoryException("Category not found with id: " + id));
            categoryRepository.delete(category);
        } catch (CategoryException e) {
            throw e;
        } catch (Exception e) {
            throw new CategoryException("Error occurred while deleting category with id: " + id, e);
        }
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new CategoryException("Category cannot be null");
        }
        
        if (!StringUtils.hasText(category.getName())) {
            throw new CategoryException("Category name cannot be empty");
        }
        
        if (category.getName().length() > 100) {
            throw new CategoryException("Category name cannot exceed 100 characters");
        }
        
        if (category.getDescription() != null && category.getDescription().length() > 500) {
            throw new CategoryException("Category description cannot exceed 500 characters");
        }
    }
} 