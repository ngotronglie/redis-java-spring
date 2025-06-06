package redis.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String error;
    private String message;
    private MetaData meta;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, "Operation successful", null);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, null, message, null);
    }

    public static <T> ApiResponse<T> success(T data, String message, MetaData meta) {
        return new ApiResponse<>(true, data, null, message, meta);
    }

    public static <T> ApiResponse<T> error(String error, String message) {
        return new ApiResponse<>(false, null, error, message, null);
    }
} 