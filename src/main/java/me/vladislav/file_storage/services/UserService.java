package me.vladislav.file_storage.services;

import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.exceptions.users.UserAlreadyExistException;
import me.vladislav.file_storage.dto.UserDTO;
import me.vladislav.file_storage.exceptions.users.UserNotFoundException;
import me.vladislav.file_storage.models.User;
import me.vladislav.file_storage.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerNewUserAccount(UserDTO userDTO){

        if(userRepository.findUserByLogin(userDTO.getLogin()).isPresent()){
            throw new UserAlreadyExistException("A user with login \"" + userDTO.getLogin() + "\" already exists.");
        }

        User user = new User(
                userDTO.getLogin(),
                passwordEncoder.encode(userDTO.getPassword())
        );

        userRepository.save(user);
    }

    public User getUserByLogin(String login){
        return userRepository.findUserByLogin(login).orElseThrow(() -> new UserNotFoundException("Error when getting user by login. User not found."));
    }

}
