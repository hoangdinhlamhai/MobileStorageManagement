package com.example.MobileStorageManagement.Controller;


import com.example.MobileStorageManagement.DTO.LoginRequest;
import com.example.MobileStorageManagement.DTO.LoginResponse;
import com.example.MobileStorageManagement.DTO.RegisterRequest;
import com.example.MobileStorageManagement.DTO.UpdateUserDTO;
import com.example.MobileStorageManagement.Entity.Role;
import com.example.MobileStorageManagement.Entity.User;
import com.example.MobileStorageManagement.JWT.JwtUtil;
import com.example.MobileStorageManagement.Repository.RoleRepository;
import com.example.MobileStorageManagement.Repository.UserRepository;
import com.example.MobileStorageManagement.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userService.existsBySdt(String.valueOf(req.getSdt()))) {
            return ResponseEntity.badRequest().body("SĐT đã tồn tại");
        }

        User user = new User();
        user.setSdt(String.valueOf(req.getSdt()));
        user.setPassword(passwordEncoder.encode(req.getMatKhau()));
        user.setAddress(req.getDiaChi());
        user.setFullName(req.getHoVaTen());
        user.setEmail(req.getEmail());

        Role role = roleRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền"));

        user.setRole(role);

        userService.saveUser(user);

        return ResponseEntity.ok("Đăng ký thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        String input = (req.getSdt() != null && !req.getSdt().isBlank())
                ? req.getSdt()
                : req.getEmail();

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input, req.getPassWord())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Sai thông tin đăng nhập");
        }

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(input, req.getPassWord())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Lấy role
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Lấy user info từ DB
        User user = userRepository.findBySdtOrEmail(input, input)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        // Generate token
        String jwt = jwtUtil.generateToken(input, roles);

        // Build response
        LoginResponse res = new LoginResponse();
        res.setUserId(user.getUserId());
        res.setSdt(user.getSdt());
        res.setFullName(user.getFullName());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setAvatar(user.getAvatar());
        res.setRole(user.getRole().getRoleId());
        res.setToken(jwt);

        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    @GetMapping("/sdt/{sdt}")
    public ResponseEntity<User> findBySdt(@PathVariable  String sdt){
        return userService.findBySdt(sdt)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole ('ADMIN') or hasRole('WORKER')")
    @GetMapping("/email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable  String email){
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @RequestBody UpdateUserDTO req
    ) {
        try {
            User updated = userService.updateUser(id, req);
            return ResponseEntity.ok(userService.toResponse(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
