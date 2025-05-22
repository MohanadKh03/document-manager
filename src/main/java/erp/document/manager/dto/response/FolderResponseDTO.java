package erp.document.manager.dto.response;

import java.util.List;

public class FolderResponseDTO {
    private Long id;
    private String name;
    private Long parentFolderId;
    private List<FileResponseDTO> files;
    private List<FolderResponseDTO> subFolders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<FileResponseDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileResponseDTO> files) {
        this.files = files;
    }

    public List<FolderResponseDTO> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<FolderResponseDTO> subFolders) {
        this.subFolders = subFolders;
    }
} 