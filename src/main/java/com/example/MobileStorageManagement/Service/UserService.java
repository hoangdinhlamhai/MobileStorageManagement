package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.DTO.UpdateUserDTO;
import com.example.MobileStorageManagement.Entity.User;
import com.example.MobileStorageManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> findBySdt(String sdt) {
        return userRepository.findBySdt(sdt);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean existsBySdt(String sdt) {
        return userRepository.existsBySdt(sdt);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Integer id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (dto.getSdt() != null) user.setSdt(dto.getSdt());
        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getAvatar() != null) user.setAvatar(dto.getAvatar());
        if (dto.getGoogleId() != null) user.setGoogleId(dto.getGoogleId());

        return userRepository.save(user);
    }

    public UpdateUserDTO toResponse(User user) {
        UpdateUserDTO dto = new UpdateUserDTO();

        dto.setSdt(user.getSdt());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setAddress(user.getAddress());
        dto.setAvatar(user.getAvatar());
        dto.setGoogleId(user.getGoogleId());

        return dto;
    }



}
