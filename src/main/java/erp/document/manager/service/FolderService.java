package erp.document.manager.service;

import erp.document.manager.dto.request.CreateFolderDTO;
import erp.document.manager.dto.response.FolderResponseDTO;
import erp.document.manager.entity.Folder;
import erp.document.manager.entity.User;
import erp.document.manager.repository.FolderRepository;
import erp.document.manager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public FolderService(FolderRepository folderRepository, UserRepository userRepository) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    public FolderResponseDTO createFolder(CreateFolderDTO createFolderDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Folder parentFolder = null;
        if (createFolderDTO.getParentFolderId() != null) {
            parentFolder = folderRepository.findById(createFolderDTO.getParentFolderId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent folder not found"));
            
            if (!parentFolder.getOwner().getId().equals(userId)) {
                throw new IllegalArgumentException("You don't have permission to create folder in this location");
            }
        }

        Folder folder = new Folder();
        folder.setName(createFolderDTO.getName());
        folder.setParentFolder(parentFolder);
        folder.setOwner(user);

        folder = folderRepository.save(folder);
        return convertToFolderResponseDTO(folder);
    }

    public FolderResponseDTO getFolder(Long folderId, Long userId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("Folder not found"));

        if (!folder.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to access this folder");
        }

        return convertToFolderResponseDTO(folder);
    }

    public List<FolderResponseDTO> getRootFolders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return folderRepository.findByOwnerAndParentFolderIsNull(user)
                .stream()
                .map(this::convertToFolderResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteFolder(Long folderId, Long userId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("Folder not found"));

        if (!folder.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to delete this folder");
        }

        folderRepository.delete(folder);
    }

    public FolderResponseDTO updateFolder(Long folderId, CreateFolderDTO updateFolderDTO, Long userId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException("Folder not found"));

        if (!folder.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to update this folder");
        }

        folder.setName(updateFolderDTO.getName());

        if (updateFolderDTO.getParentFolderId() != null) {
            Folder newParentFolder = folderRepository.findById(updateFolderDTO.getParentFolderId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent folder not found"));
            
            if (!newParentFolder.getOwner().getId().equals(userId)) {
                throw new IllegalArgumentException("You don't have permission to move folder to this location");
            }
            
            folder.setParentFolder(newParentFolder);
        } else {
            folder.setParentFolder(null);
        }

        folder = folderRepository.save(folder);
        return convertToFolderResponseDTO(folder);
    }

    private FolderResponseDTO convertToFolderResponseDTO(Folder folder) {
        FolderResponseDTO dto = new FolderResponseDTO();
        dto.setId(folder.getId());
        dto.setName(folder.getName());
        dto.setParentFolderId(folder.getParentFolder() != null ? folder.getParentFolder().getId() : null);
        return dto;
    }
}
