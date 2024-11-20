package com.LiveConnect.service;

import com.LiveConnect.dto.UserLoginDTO;
import com.LiveConnect.model.User;
import com.LiveConnect.repository.UserRepository;
import com.LiveConnect.service.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Método para encontrar usuário pelo 'id'
     * @param id 'id' do usuário
     * @return Optional<User>>
     */
    public Optional<User> findUserById(int id) {
        return userRepository.findById(id);
    }

    /**
     * Método para encontrar todos os usuários
     * @return List<User>>
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Método para adicionar um usuário
     * @param user usuário
     * @return User>
     */
    public User add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    /**
     * Método para atualizar um usuário
     * @param id 'id' do usuário
     * @param user usuário
     * @return User>
     */
    public User update(int id, User user) {
        if (!userRepository.existsById(id)) {
            return null;
        }
        user.setId(id);

        final User updateUser = userRepository.save(user);
        return updateUser;
    }

    /**
     * Método para deletar um usuário pelo 'id'
     * @param id 'id' do usuário
     * @return User>
     */
    public boolean deleteById(int id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
