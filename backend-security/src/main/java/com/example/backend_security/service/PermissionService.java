package com.example.backend_security.service;

import com.example.backend_security.entity.Permission;
import com.example.backend_security.exception.ResourceAlreadyExistsException;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission)  {
        if (permissionRepository.findByCode(permission.getCode()).isPresent()) {
            throw new ResourceAlreadyExistsException("Permission already exists");
        }
        return permissionRepository.save(permission);
    }


    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }


    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }


    public Permission updatePermission(Long id, Permission updatedPermission)  {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        permission.setCode(updatedPermission.getCode());
        permission.setDescription(updatedPermission.getDescription());
        return permissionRepository.save(permission);
    }


    public void deletePermission(Long id)  {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        permissionRepository.delete(permission);
    }
}