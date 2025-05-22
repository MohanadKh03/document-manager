package erp.document.manager.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateFolderDTO {
    @NotBlank(message = "Folder name is required")
    private String name;
    
    private Long parentFolderId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
    }
} 