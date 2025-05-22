package erp.document.manager.controller;

import erp.document.manager.dto.request.CreateFolderDTO;
import erp.document.manager.dto.response.FolderResponseDTO;
import erp.document.manager.service.FolderService;
import erp.document.manager.util.APIResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folders")
public class FolderController {
    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<FolderResponseDTO>> createFolder(
            @Valid @RequestBody CreateFolderDTO createFolderDTO,
            @RequestHeader("User-Id") Long userId) {
        FolderResponseDTO response = folderService.createFolder(createFolderDTO, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new APIResponse<>(HttpStatus.CREATED.value(), "Folder created successfully", response));
    }

    @GetMapping("/{folderId}")
    public ResponseEntity<APIResponse<FolderResponseDTO>> getFolder(
            @PathVariable Long folderId,
            @RequestHeader("User-Id") Long userId) {
        FolderResponseDTO response = folderService.getFolder(folderId, userId);
        return ResponseEntity
                .ok(new APIResponse<>(HttpStatus.OK.value(), "Folder retrieved successfully", response));
    }

    @GetMapping("/root")
    public ResponseEntity<APIResponse<List<FolderResponseDTO>>> getRootFolders(
            @RequestHeader("User-Id") Long userId) {
        List<FolderResponseDTO> response = folderService.getRootFolders(userId);
        return ResponseEntity
                .ok(new APIResponse<>(HttpStatus.OK.value(), "Root folder retrieved successfully", response));
    }

    @PutMapping("/{folderId}")
    public ResponseEntity<APIResponse<FolderResponseDTO>> updateFolder(
            @PathVariable Long folderId,
            @Valid @RequestBody CreateFolderDTO updateFolderDTO,
            @RequestHeader("User-Id") Long userId) {
        FolderResponseDTO response = folderService.updateFolder(folderId, updateFolderDTO, userId);
        return ResponseEntity
                .ok(new APIResponse<>(HttpStatus.OK.value(), "Folder updated successfully", response));
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<APIResponse<Void>> deleteFolder(
            @PathVariable Long folderId,
            @RequestHeader("User-Id") Long userId) {
        folderService.deleteFolder(folderId, userId);
        return ResponseEntity
                .ok(new APIResponse<>(HttpStatus.OK.value(), "Folder deleted successfully", null));
    }
}
