package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    public User user;


    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        if(input.contains("@")){
            user = userService.findByEmail(input)
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user với email: " + input));
        }else{
            user = userService.findBySdt(input)
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user với SDT: " + input));
        }

        String roleName = user.getRole().getRoleName().toUpperCase();
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + roleName)
        );


        return new org.springframework.security.core.userdetails.User(
                input,
                user.getPassword(),
                authorities
        );
    }
}
