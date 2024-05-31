package com.ecommerce.controller;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.entity.ContactUs;
import com.ecommerce.service.auth.AuthService;
import com.ecommerce.service.customer.customerorder.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final CustomerOrderService customerOrderService;

    private final AuthService authService;

    @GetMapping("/order/{trackingId}")
    public ResponseEntity<OrderDto> searchOrderByTrackingId(@PathVariable UUID trackingId) {
        OrderDto orderDto = customerOrderService.searchOrderByTrackingId(trackingId);
        if (orderDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/contact-us")
    public ResponseEntity<?> saveContactUsForm(@RequestBody ContactUs contactUs) {
        authService.saveContactUsForm(contactUs);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/contact-us")
    public ResponseEntity<?> getAllContactUs() {
        return ResponseEntity.ok(authService.getAllContactUs());
    }

}
