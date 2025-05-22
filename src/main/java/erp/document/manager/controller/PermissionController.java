package erp.document.manager.controller;

import erp.document.manager.dto.request.PermissionRequestDTO;
import erp.document.manager.dto.response.PermissionResponseDTO;
import erp.document.manager.service.PermissionService;
import erp.document.manager.util.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<PermissionResponseDTO>> grantPermission(
            @RequestBody PermissionRequestDTO requestDTO,
            @RequestHeader("User-Id") Long userId) {
        PermissionResponseDTO response = permissionService.grantPermission(requestDTO, userId);
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "Permission granted successfully",
                response
        ));
    }

    @DeleteMapping("/{fileId}/user/{targetUserId}")
    public ResponseEntity<APIResponse<Void>> revokePermission(
            @PathVariable Long fileId,
            @PathVariable Long targetUserId,
            @RequestHeader("User-Id") Long userId) {
        permissionService.revokePermission(fileId, targetUserId, userId);
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "Permission revoked successfully",
                null
        ));
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<APIResponse<List<PermissionResponseDTO>>> getFilePermissions(
            @PathVariable Long fileId,
            @RequestHeader("User-Id") Long userId) {
        List<PermissionResponseDTO> permissions = permissionService.getFilePermissions(fileId, userId);
        return ResponseEntity.ok(new APIResponse<>(
                HttpStatus.OK.value(),
                "Permissions retrieved successfully",
                permissions
        ));
    }
}
