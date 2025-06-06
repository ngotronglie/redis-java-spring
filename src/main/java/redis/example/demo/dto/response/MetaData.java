package redis.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {
    private int page;
    private int limit;
    private long total;
    private int totalPages;
    
    public static MetaData of(int page, int limit, long total) {
        int totalPages = (int) Math.ceil((double) total / limit);
        return new MetaData(page, limit, total, totalPages);
    }
} 