package erp.document.manager.service;

import erp.document.manager.dto.request.UserRegisterRequestDTO;
import erp.document.manager.dto.request.UserLoginRequestDTO;
import erp.document.manager.dto.response.UserLoginResponseDTO;
import erp.document.manager.dto.response.UserRegisterResponseDTO;
import erp.document.manager.entity.User;
import erp.document.manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRegisterResponseDTO registerUser(UserRegisterRequestDTO userRegisterRequestDTO) {

        if (userRepository.findByEmail(userRegisterRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setEmail(userRegisterRequestDTO.getEmail());
        user.setPassword(userRegisterRequestDTO.getPassword());

        user = userRepository.save(user);

        UserRegisterResponseDTO responseDTO = new UserRegisterResponseDTO();
        responseDTO.setId(user.getId());

        return responseDTO;
    }

    public UserLoginResponseDTO loginUser(UserLoginRequestDTO userLoginRequestDTO) {
        Optional<User> user = userRepository.findByEmailAndPassword(
                userLoginRequestDTO.getEmail(),
                userLoginRequestDTO.getPassword()
        );

        if (user.isEmpty()) {
            throw new NoSuchElementException("Invalid email or password");
        }

        UserLoginResponseDTO responseDTO = new UserLoginResponseDTO();
        responseDTO.setId(user.get().getId());

        return responseDTO;
    }
}
