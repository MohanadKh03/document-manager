package erp.document.manager.controller;

import erp.document.manager.dto.request.UserLoginRequestDTO;
import erp.document.manager.dto.request.UserRegisterRequestDTO;
import erp.document.manager.dto.response.UserLoginResponseDTO;
import erp.document.manager.dto.response.UserRegisterResponseDTO;
import erp.document.manager.service.UserService;
import erp.document.manager.util.APIResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserRegisterResponseDTO>> registerUser(
            @Valid @RequestBody UserRegisterRequestDTO requestDTO) {
        UserRegisterResponseDTO response = userService.registerUser(requestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new APIResponse<>(HttpStatus.CREATED.value(), "User registered successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<UserLoginResponseDTO>> loginUser(
            @Valid @RequestBody UserLoginRequestDTO requestDTO) {
        UserLoginResponseDTO response = userService.loginUser(requestDTO);
        return ResponseEntity
                .ok(new APIResponse<>(HttpStatus.OK.value(), "Login successful", response));
    }
}
