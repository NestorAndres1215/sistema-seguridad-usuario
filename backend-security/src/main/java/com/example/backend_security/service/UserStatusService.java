package com.example.backend_security.service;

import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.exception.ResourceAlreadyExistsException;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserStatusRepository statusRepository;

    public UserStatus createStatus(UserStatus status) {
        if (statusRepository.findByCode(status.getCode()).isPresent()) {
            throw new ResourceAlreadyExistsException("Status already exists");
        }
        return statusRepository.save(status);
    }

    public List<UserStatus> getAllStatuses() {
        return statusRepository.findAll();
    }

    public Optional<UserStatus> getStatusById(Long id) {
        return statusRepository.findById(id);
    }


    public UserStatus updateStatus(Long id, UserStatus updatedStatus) {
        UserStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));

        status.setCode(updatedStatus.getCode());
        status.setDescription(updatedStatus.getDescription());

        return statusRepository.save(status);
    }

    public void deleteStatus(Long id)  {
        UserStatus status = statusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
        statusRepository.delete(status);
    }
}