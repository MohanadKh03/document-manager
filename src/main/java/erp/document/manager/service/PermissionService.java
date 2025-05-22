package erp.document.manager.service;

import erp.document.manager.dto.request.PermissionRequestDTO;
import erp.document.manager.dto.response.PermissionResponseDTO;
import erp.document.manager.entity.File;
import erp.document.manager.entity.Permission;
import erp.document.manager.entity.PermissionLevel;
import erp.document.manager.repository.FileRepository;
import erp.document.manager.repository.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final FileRepository fileRepository;

    public PermissionService(PermissionRepository permissionRepository, FileRepository fileRepository) {
        this.permissionRepository = permissionRepository;
        this.fileRepository = fileRepository;
    }

    @Transactional
    public PermissionResponseDTO grantPermission(PermissionRequestDTO requestDTO, Long grantingUserId) {
        File file = fileRepository.findById(requestDTO.getFileId())
                .orElseThrow(() -> new EntityNotFoundException("File not found"));

        if (!file.getOwner().getId().equals(grantingUserId)) {
            throw new IllegalArgumentException("Only file owner can grant permissions");
        }

        Permission existingPermission = permissionRepository
                .findByFileIdAndUserId(requestDTO.getFileId(), requestDTO.getUserId())
                .orElse(null);

        if (existingPermission != null) {
            existingPermission.setPermissionLevel(requestDTO.getPermissionLevel());
            return convertToDTO(permissionRepository.save(existingPermission));
        }

        Permission permission = new Permission();
        permission.setFile(file);
        permission.setUserId(requestDTO.getUserId());
        permission.setPermissionLevel(requestDTO.getPermissionLevel());

        return convertToDTO(permissionRepository.save(permission));
    }

    @Transactional
    public void revokePermission(Long fileId, Long userId, Long requestingUserId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));

        if (!file.getOwner().getId().equals(requestingUserId)) {
            throw new IllegalArgumentException("Only file owner can revoke permissions");
        }

        if (file.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("You cannot revoke your own permissions");
        }

        permissionRepository.deleteByFileIdAndUserId(fileId, userId);
    }

    public List<PermissionResponseDTO> getFilePermissions(Long fileId, Long requestingUserId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));

        if (!file.getOwner().getId().equals(requestingUserId) && 
            !hasPermission(fileId, requestingUserId)) {
            throw new IllegalArgumentException("You don't have permission to view this file's permissions");
        }

        return file.getPermissions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public boolean hasPermission(Long fileId, Long userId) {
        return permissionRepository.findByFileIdAndUserId(fileId, userId).isPresent();
    }

    public boolean hasPermissionLevel(Long fileId, Long userId, PermissionLevel requiredLevel) {
        return permissionRepository.findByFileIdAndUserId(fileId, userId)
                .map(permission -> hasRequiredPermissionLevel(permission.getPermissionLevel(), requiredLevel))
                .orElse(false);
    }

    private boolean hasRequiredPermissionLevel(PermissionLevel actualLevel, PermissionLevel requiredLevel) {
        if (actualLevel == PermissionLevel.FULL_ACCESS) return true;
        return actualLevel == requiredLevel;
    }

    private PermissionResponseDTO convertToDTO(Permission permission) {
        return new PermissionResponseDTO(
                permission.getId(),
                permission.getFile().getId(),
                permission.getUserId(),
                permission.getPermissionLevel()
        );
    }
}