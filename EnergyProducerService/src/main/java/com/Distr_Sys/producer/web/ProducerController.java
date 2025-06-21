package com.Distr_Sys.producer.web;

import com.Distr_Sys.producer.model.ProductionRecord;
import com.Distr_Sys.producer.service.ProducerService;
import com.Distr_Sys.producer.web.ProductionRequest; // Import the top-level DTO
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/producer")
public class ProducerController {
    private final ProducerService service;

    public ProducerController(ProducerService service) {
        this.service = service;
    }

    @GetMapping("/current")
    public ProductionRecord current() {
        return service.latest();
    }

    @GetMapping("/records")
    public List<ProductionRecord> getAllRecords() {
        return service.findAll();
    }

    @PostMapping("/publish")
    public ResponseEntity<Map<String, Object>> publish(@RequestBody ProductionRequest request) {
        ProductionRecord rec = service.produce(request.getProducedKw());
        return ResponseEntity.ok(Map.of(
                "message", "Message has been published",
                "id", rec.getId(),
                "timestamp", rec.getDatetime(),
                "producedKw", rec.getKwh()
        ));
    }

    // For local testing: triggers publish logic via GET
    @GetMapping("/publish/test")
    public ResponseEntity<Map<String, Object>> publishTest() {
        ProductionRequest request = new ProductionRequest();
        request.setProducedKw(5); // test value
        return publish(request);
    }
}