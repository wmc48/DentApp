package com.booking_dent.dentApp.security;

import com.booking_dent.dentApp.database.repository.RoleRepository;
import com.booking_dent.dentApp.model.dto.UserDTO;
import com.booking_dent.dentApp.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    public final UserRepository userRepository;
    public final PatientService patientService;
    public final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;

    public UserEntity createAccount(UserDTO userDTO){

//        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        UserEntity newUser = UserEntity.builder()
                .username(userDTO.getUsername())
//                .passwordHash(hashedPassword)
                .passwordHash(userDTO.getPassword())
                .email(userDTO.getEmail())
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(newUser);

    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        List<SimpleGrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private List<SimpleGrantedAuthority> getUserAuthority(Set<RoleEntity> roles) {

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .distinct()
                .toList();
    }

    private UserDetails buildUserForAuthentication(
            UserEntity user,
            List<SimpleGrantedAuthority> authorities
    ) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.getIsActive(),
                true,
                true,
                true,
                authorities
        );
    }
}
