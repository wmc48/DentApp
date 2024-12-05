package com.booking_dent.dentApp.security;

import com.booking_dent.dentApp.database.entity.PatientEntity;
import com.booking_dent.dentApp.database.repository.PatientRepository;
import com.booking_dent.dentApp.model.dto.PatientDTO;
import com.booking_dent.dentApp.model.dto.UserDTO;
import com.booking_dent.dentApp.service.PatientService;
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

    public final UserRepository userRepository;
    public final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity createPatientAccount(UserDTO userDTO) {
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        UserEntity newUser = UserEntity.builder()
                .username(userDTO.getUsername())
                .passwordHash(hashedPassword)
                .createdAt(LocalDateTime.now())
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        //przypisanie roli dla nowo powstałego usera - (patient = 4)
        userRepository.assignUserRole(savedUser.getUserId(), 4); // Role ID for patient
        return savedUser;
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
