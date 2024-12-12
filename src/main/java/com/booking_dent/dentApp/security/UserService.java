package com.booking_dent.dentApp.security;

import com.booking_dent.dentApp.model.dto.UserDTO;
import com.booking_dent.dentApp.model.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserEntity createAccount(UserDTO userDTO, Integer roleId) {
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        UserEntity newUser = UserEntity.builder()
                .username(userDTO.getUsername())
                .passwordHash(hashedPassword)
                .createdAt(LocalDateTime.now())
                .build();

        UserEntity savedUser = userRepository.save(newUser);
        userRepository.assignUserRole(savedUser.getUserId(), roleId);
        return savedUser;
    }

    public UserDTO getUserDTObyUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Niepoprawne dane uwierzytelniające"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                        .collect(Collectors.toList())
        );
    }

    public boolean checkRole(Principal principal, String roleToCheck){
        //sprawdzenie ról użytkownika
        //authorities przechowuje wszystkie role przypisane do aktualnie zalogowanego użytkownika.
        Collection<? extends GrantedAuthority> authorities =
                ((Authentication) principal).getAuthorities();

        //sprawdzamy czy zalogowany user ma role 'roleToCheck'
        boolean isThisRoleToCheck = authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals(roleToCheck));
        return isThisRoleToCheck;
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
