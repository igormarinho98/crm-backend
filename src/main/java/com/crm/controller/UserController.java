package com.crm.controller;

import com.crm.model.User;
import com.crm.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "APIs para gerenciar usuários do sistema")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Criar usuário", description = "Cria um novo usuário do sistema")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @Operation(summary = "Listar usuários", description = "Retorna todos os usuários do sistema")
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Buscar usuário", description = "Retorna um usuário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza dados de um usuário existente")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        return userRepository.findById(id).map(existing -> {
            existing.setFirstName(updatedUser.getFirstName());
            existing.setLastName(updatedUser.getLastName());
            existing.setEmail(updatedUser.getEmail());
            userRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
