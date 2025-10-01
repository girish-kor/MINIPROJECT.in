package miniproject.in.controller;

import lombok.RequiredArgsConstructor;
import miniproject.in.dto.ApiResponse;
import miniproject.in.service.DeliveryService;
import miniproject.in.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliver")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final JwtService jwtService;

    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResponse> deliverProduct(@PathVariable String orderId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");

            if (!jwtService.isTokenValid(token)) {
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder().success(false).message("Invalid token").build());
            }

            deliveryService.deliverProduct(orderId);

            return ResponseEntity.ok(ApiResponse.builder().success(true)
                    .message("Product delivered successfully").build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
        }
    }
}
