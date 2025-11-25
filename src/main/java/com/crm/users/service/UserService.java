package com.crm.users.service;

import com.crm.users.model.Role;
import com.crm.users.model.User;
import com.crm.users.repository.RoleRepository;
import com.crm.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

   private final UserRepository userRepository;
   private final RoleRepository roleRepository;

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }
//
//    public Mono<User> createUser(@RequestBody User user) {
//        User newUser = new User();
//        newUser.setName(user.getName());
//        newUser.setMobile(user.getMobile());
//        return roleRepository.findById(user.getRoleId()).flatMap(role -> {
//            newUser.setRoleId(role.getId());
//            newUser.setPermissions(role.getPermissions());
//            return userRepository.save(newUser);
//        })
//        .switchIfEmpty(Mono.error(new IllegalArgumentException("Role not found")));
//    }
}
