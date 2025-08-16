package anh.pham.altech.service;


import anh.pham.altech.dto.AddBasketItemDTO;
import anh.pham.altech.entity.Basket;
import anh.pham.altech.entity.Product;
import anh.pham.altech.repository.BasketRepository;
import anh.pham.altech.repository.ProductRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
@SpringBootTest
public class BasketServiceIntegrationImplTest {

    @Autowired
    private BasketService basketService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BasketRepository basketRepository;

    private UUID productId;
    private UUID basketId;

    @BeforeEach
    void setup() {
        Product p = new Product();
        p.setName("Test Product");
        p.setStock(10);
        p = productRepository.saveAndFlush(p);
        productId = p.getId();

        // Tạo basket
        Basket basket = new Basket();
        basket.setItems(new HashMap<>());
        basket = basketRepository.saveAndFlush(basket);
        basketId = basket.getId();
    }

    @Test
    void testDecreaseStockConcurrently() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch = new CountDownLatch(1);
        List<Future<String>> results = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            results.add(executor.submit(() -> {
                latch.await();
                try {
                    AddBasketItemDTO dto = new AddBasketItemDTO();
                    dto.setBasketId(basketId);
                    dto.setProductId(productId);
                    dto.setQuantity(3);
                    basketService.update(dto);
                    return "Thread " + finalI + " OK";
                } catch (BadRequestException e) {
                    return "Thread " + finalI + " FAIL: " + e.getMessage();
                }
            }));
        }

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        for (Future<String> r : results) {
            try {
                System.out.println(r.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int remainingStock = productRepository.findById(productId)
                .orElseThrow()
                .getStock();

        System.out.println("Remaining stock: " + remainingStock);
    }
}
