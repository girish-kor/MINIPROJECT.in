package miniproject.in.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import miniproject.in.dto.ApiResponse;

@RestController
public class IndexController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse> index() {
        return ResponseEntity.ok(ApiResponse.builder().success(true)
                .message("MINIPROJECT.in API is running").build());
    }

    @GetMapping("/api")
    public ResponseEntity<ApiResponse> api() {
        return ResponseEntity
                .ok(ApiResponse.builder().success(true).message("API is operational").build());
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        return ResponseEntity
                .ok(ApiResponse.builder().success(true).message("Service is healthy").build());
    }
}
