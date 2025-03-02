package com.microservice.serviceB.controller;

import com.microservice.serviceB.entity.Service;
import com.microservice.serviceB.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/service")
public class ServicesController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping("/list")
    public ResponseEntity<Page<Service>> getServices(
            @RequestParam("keyword") String keyword,
            @PageableDefault(page = 0, size = 5) Pageable pageable) {
        return ResponseEntity.ok(serviceService.getService(keyword, pageable));
    }

    @GetMapping("/get-by-type")
    public ResponseEntity<List<Service>> getServices(
            @RequestParam("serviceType") String serviceType) {
        return ResponseEntity.ok(serviceService.getServiceByType(serviceType));
    }


    @PostMapping
    public ResponseEntity<Service> saveService(@RequestBody Service service){
        return ResponseEntity.ok(serviceService.saveService(service));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable("id") UUID id){
        serviceService.deleteService(id);
        return ResponseEntity.ok().build();
    }
}
