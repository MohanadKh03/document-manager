package erp.document.manager.dto.response;

import erp.document.manager.entity.PermissionLevel;

public class PermissionResponseDTO {
    private Long id;
    private Long fileId;
    private Long userId;
    private PermissionLevel permissionLevel;

    public PermissionResponseDTO(Long id, Long fileId, Long userId, PermissionLevel permissionLevel) {
        this.id = id;
        this.fileId = fileId;
        this.userId = userId;
        this.permissionLevel = permissionLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
