package com.microservice.serviceB.service.impl;

import com.microservice.serviceB.enums.ServiceType;
import com.microservice.serviceB.repository.ServiceRepository;
import com.microservice.serviceB.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Page<com.microservice.serviceB.entity.Service> getService(String keyword, Pageable pageable) {
        return serviceRepository.findAllByKeywordOptional(keyword, pageable);
    }

    @Override
    public List<com.microservice.serviceB.entity.Service> getServiceByType(String serviceType) {
        ServiceType serviceTypeEnum = ServiceType.valueOf(serviceType);
        return serviceRepository.findAllByType(serviceTypeEnum);
    }

    @Override
    public com.microservice.serviceB.entity.Service saveService(com.microservice.serviceB.entity.Service service) {
        return serviceRepository.save(service);
    }

    @Override
    public void deleteService(UUID id) {
        serviceRepository.deleteById(id);
    }
}
