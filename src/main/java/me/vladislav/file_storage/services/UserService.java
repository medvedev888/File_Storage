package me.vladislav.file_storage.services;

import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.exceptions.UserAlreadyExistException;
import me.vladislav.file_storage.dto.UserDTO;
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
            //TODO: Need to display this exception in /registration in the advanced controller
            throw new UserAlreadyExistException("A user with that login exists");
        }

        User user = new User(
                userDTO.getLogin(),
                passwordEncoder.encode(userDTO.getPassword())
        );

        userRepository.save(user);
    }
}
