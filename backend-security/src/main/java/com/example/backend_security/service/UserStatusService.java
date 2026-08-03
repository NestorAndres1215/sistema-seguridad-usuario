package com.example.backend_security.service;

import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository statusRepository;


    public List<UserStatus> getAllStatuses() {
        return statusRepository.findAll();
    }

    public UserStatus getStatusById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado con id: " + id));
    }

}