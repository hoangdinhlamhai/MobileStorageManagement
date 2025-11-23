package com.example.MobileStorageManagement.Service;

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
}
