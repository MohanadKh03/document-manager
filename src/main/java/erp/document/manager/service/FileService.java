package erp.document.manager.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import erp.document.manager.dto.request.PermissionRequestDTO;
import erp.document.manager.dto.request.UploadFileDTO;
import erp.document.manager.dto.response.FileResponseDTO;
import erp.document.manager.entity.File;
import erp.document.manager.entity.Folder;
import erp.document.manager.entity.PermissionLevel;
import erp.document.manager.entity.User;
import erp.document.manager.repository.FileRepository;
import erp.document.manager.repository.FolderRepository;
import erp.document.manager.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final PermissionService permissionService;
    private final Cloudinary cloudinary;
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList(
        "application/pdf",                                                     // PDF
        "application/msword",                                                 // DOC
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // DOCX
        "application/vnd.ms-excel",                                           // XLS
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"  // XLSX
    );
    private static final long MAX_FILE_SIZE_IN_MB = 10 * 1024 * 1024; // 10MB

    public FileService(FileRepository fileRepository, UserRepository userRepository,
                      FolderRepository folderRepository, PermissionService permissionService, Cloudinary cloudinary) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.permissionService = permissionService;
        this.cloudinary = cloudinary;
    }

    public FileResponseDTO uploadFile(UploadFileDTO uploadFileDTO, Long userId) {
        validateFile(uploadFileDTO.getFile());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Folder folder = null;
        if (uploadFileDTO.getFolderId() != null) {
            folder = folderRepository.findById(uploadFileDTO.getFolderId())
                    .orElseThrow(() -> new EntityNotFoundException("Folder not found"));
        }

        try {
            Map<String, String> uploadResult = cloudinary.uploader().upload(
                    uploadFileDTO.getFile().getBytes(),
                    Collections.emptyMap()
            );

            File file = new File();
            file.setTitle(uploadFileDTO.getTitle());
            file.setDescription(uploadFileDTO.getDescription());
            file.setFileType(uploadFileDTO.getFile().getContentType());
            file.setFileSize(uploadFileDTO.getFile().getSize());
            file.setTags(uploadFileDTO.getTags());
            file.setFolder(folder);
            file.setFileUrl(uploadResult.get("url"));
            file.setOwner(user);


            file = fileRepository.save(file);

            PermissionRequestDTO permissionRequestDTO = new PermissionRequestDTO(file.getId(),userId,PermissionLevel.FULL_ACCESS);

            permissionService.grantPermission(permissionRequestDTO, userId);

            return convertToFileResponseDTO(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        if (!ALLOWED_FILE_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("File type not supported");
        }

        if (file.getSize() > MAX_FILE_SIZE_IN_MB) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 10MB");
        }
    }

    public FileResponseDTO getFile(Long fileId, Long userId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));

        // Check if user is owner or has permission
        if (!file.getOwner().getId().equals(userId) && 
        !permissionService.hasPermissionLevel(fileId, userId, PermissionLevel.VIEW)) {
            throw new IllegalArgumentException("You don't have permission to view this file");
        }

        return convertToFileResponseDTO(file);
    }

    public List<FileResponseDTO> getFilesByFolder(Long folderId, Long userId) {
        List<File> files;
        if (folderId == null) {
            User owner = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            files = fileRepository.findByOwner(owner);
        } else {
            Folder folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new EntityNotFoundException("Folder not found"));

            if (!folder.getOwner().getId().equals(userId)) {
                throw new IllegalArgumentException("You don't have permission to access this folder");
            }
            files = folder.getFiles();
        }

        return files.stream()
                .map(this::convertToFileResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteFile(Long fileId, Long userId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("File not found"));

        if (!file.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to delete this file");
        }

        try {
            // Delete from Cloudinary
            cloudinary.uploader().destroy(file.getId().toString(), ObjectUtils.emptyMap());
            // Delete from database
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    private FileResponseDTO convertToFileResponseDTO(File file) {
        FileResponseDTO dto = new FileResponseDTO();
        dto.setId(file.getId());
        dto.setTitle(file.getTitle());
        dto.setDescription(file.getDescription());
        dto.setFileType(file.getFileType());
        dto.setFileSize(file.getFileSize());
        dto.setTags(file.getTags());
        dto.setFolderId(file.getFolder() != null ? file.getFolder().getId() : null);
        dto.setFileUrl(file.getFileUrl());
        return dto;
    }
}