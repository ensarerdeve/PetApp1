package com.project.PetApp1.Services;

import com.project.PetApp1.Entities.User;
import com.project.PetApp1.Repositories.UserRepository;
import com.project.PetApp1.Security.JwtUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        return JwtUserDetails.create(user);
    }

    public UserDetails loadUserById(Long id){
        User user = userRepository.findById(id).get();
        return JwtUserDetails.create(user);
    }
}
