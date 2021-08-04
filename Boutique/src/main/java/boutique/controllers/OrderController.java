package boutique.controllers;

import boutique.config.UserDetailsImpl;
import boutique.entities.Order;
import boutique.entities.Product;
import boutique.models.*;
import boutique.repositories.OrderRepository;
import boutique.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository,
                           ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listOrders(@RequestParam(required = false, defaultValue = "12") Integer size,
                                        @RequestParam(required = false, defaultValue = "1") Integer page) {
        try {
            PaginationRequest request = new PaginationRequest(size, page);

            Pageable paging = PageRequest.of(request.getPageIndex() - 1,
                    request.getElements(),
                    Sort.by(request.getSortBy()).descending());

            Page<Order> pagedResult = this.orderRepository.findAll(paging);

            if (!pagedResult.hasContent()) {
                return ResponseEntity.ok(new MessageResponse("This page is empty."));
            }

            List<OrderResponse> orders = parseOrders(pagedResult);

            return ResponseEntity.ok(new ListElementsResponse(orders, pagedResult.getTotalPages()));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(ex.getMessage(), Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProcess(@RequestBody OrderRequest orderRequest,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            List<Product> products = new ArrayList<>();

            for (Integer productId : orderRequest.getProducts()) {
                if (!this.productRepository.existsById(productId)) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Error: Could not find product with id ${productId}."));
                }

                products.add(this.productRepository.findById(productId).get());
            }

            Order order = new Order(userDetails.getUser(), products);

            this.orderRepository.saveAndFlush(order);

            return ResponseEntity.ok(new MessageResponse("Order created successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not create order.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Integer id) {
        try {
            if (!this.orderRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find order with id ${id}."));
            }

            Order order = this.orderRepository.findById(id).get();
            OrderResponse orderResponse = this.parseOrder(order);

            return ResponseEntity.ok(orderResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get order details.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            if (!this.orderRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find order with id ${id}."));
            }

            Order order = this.orderRepository.findById(id).get();
            OrderResponse orderResponse = this.parseOrder(order);

            return ResponseEntity.ok(orderResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get order details.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProcess(@PathVariable Integer id) {
        try {
            if (!this.orderRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find order with id ${id}."));
            }

            Order order = this.orderRepository.findById(id).get();

            this.orderRepository.delete(order);

            return ResponseEntity.ok(new MessageResponse("Order deleted successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not delete order.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    private List<OrderResponse> parseOrders(Page<Order> pagedResult) {
        List<OrderResponse> orders = pagedResult
                .stream().map(x -> new OrderResponse(
                        x.getId(),
                        new UserResponse(x.getUser().getId(),
                                x.getUser().getName(),
                                x.getUser().getSurname(),
                                x.getUser().getAddress(),
                                x.getUser().getEmail()),
                        x.getProducts().stream().map(pr -> new ProductResponse(
                                pr.getId(),
                                pr.getName(),
                                pr.getDescription(),
                                pr.getPicture(),
                                pr.getDiscount(),
                                pr.getPrice(),
                                new CategoryResponse(pr.getCategory().getId(), pr.getCategory().getName())
                        )).collect(Collectors.toList()))).collect(Collectors.toList());

        return orders;
    }

    private OrderResponse parseOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                new UserResponse(order.getUser().getId(),
                        order.getUser().getName(),
                        order.getUser().getSurname(),
                        order.getUser().getAddress(),
                        order.getUser().getEmail()),
                order.getProducts().stream().map(pr -> new ProductResponse(
                        pr.getId(),
                        pr.getName(),
                        pr.getDescription(),
                        pr.getPicture(),
                        pr.getDiscount(),
                        pr.getPrice(),
                        new CategoryResponse(pr.getCategory().getId(), pr.getCategory().getName())
                )).collect(Collectors.toList()));
    }
}
