package erp.document.manager.dto.request;

import erp.document.manager.entity.PermissionLevel;

public class PermissionRequestDTO {
    private Long fileId;
    private Long userId;
    private PermissionLevel permissionLevel;

    public PermissionRequestDTO(Long fileId, Long userId, PermissionLevel permissionLevel) {
        this.fileId = fileId;
        this.userId = userId;
        this.permissionLevel = permissionLevel;
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
