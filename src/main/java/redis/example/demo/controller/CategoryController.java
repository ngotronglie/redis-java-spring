package redis.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.example.demo.dto.response.ApiResponse;
import redis.example.demo.dto.response.MetaData;
import redis.example.demo.entity.Category;
import redis.example.demo.service.extend.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<Category> categories = categoryService.getAllCategories();
            MetaData meta = MetaData.of(page, limit, categories.size());
            return ResponseEntity.ok(ApiResponse.success(categories, "Categories retrieved successfully", meta));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("FETCH_ERROR", "Failed to retrieve categories: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        try {
            return categoryService.getCategoryById(id)
                    .map(category -> ResponseEntity.ok(ApiResponse.success(category, "Category retrieved successfully")))
                    .orElse(ResponseEntity.ok(ApiResponse.error("NOT_FOUND", "Category not found with id: " + id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("FETCH_ERROR", "Failed to retrieve category: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.ok(ApiResponse.success(createdCategory, "Category created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("CREATE_ERROR", "Failed to create category: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(ApiResponse.success(updatedCategory, "Category updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("UPDATE_ERROR", "Failed to update category: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("DELETE_ERROR", "Failed to delete category: " + e.getMessage()));
        }
    }
}
