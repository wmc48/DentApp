//package com.booking_dent.dentApp.database.entity;
//
//
//import com.booking_dent.dentApp.security.UserEntity;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalTime;
//import java.util.Set;
//
//@Data
//@Entity
//@Table(name = "user_role")
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserRoleEntity {
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private UserEntity user;
//
//    @ManyToOne
//    @JoinColumn(name = "role_id", nullable = false)
//    private RoleEntity role;
//
//}
//
