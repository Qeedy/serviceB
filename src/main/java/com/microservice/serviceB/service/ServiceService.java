package com.microservice.serviceB.service;

import com.microservice.serviceB.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ServiceService {
    public Page<Service> getService(String keyword, Pageable pageable);
    public List<Service> getServiceByType(String serviceType);
    public Service saveService(Service service);
    public void deleteService(UUID id);
}
