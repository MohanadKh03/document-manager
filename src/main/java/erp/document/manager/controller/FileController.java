package erp.document.manager.controller;

import erp.document.manager.dto.request.UploadFileDTO;
import erp.document.manager.dto.response.FileResponseDTO;
import erp.document.manager.service.FileService;
import erp.document.manager.util.APIResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse<FileResponseDTO>> uploadFile(
            @Valid @ModelAttribute UploadFileDTO uploadFileDTO,
            @RequestHeader("User-Id") Long userId) {
        FileResponseDTO response = fileService.uploadFile(uploadFileDTO, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new APIResponse<>(HttpStatus.CREATED.value(), "File uploaded successfully", response));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<APIResponse<FileResponseDTO>> getFile(
            Long fileId,
            @RequestHeader("User-Id") Long userId) {
        FileResponseDTO response = fileService.getFile(fileId, userId);
        return ResponseEntity
                .ok(new APIResponse<>(HttpStatus.OK.value(), "File retrieved successfully", response));
    }

    @GetMapping("/root")
    public ResponseEntity<APIResponse<List<FileResponseDTO>>> getAllFiles(
            @RequestHeader("User-Id") Long userId) {
        List<FileResponseDTO> response = fileService.getFilesByFolder(null, userId);
        return ResponseEntity
                .ok(new APIResponse<>(HttpStatus.OK.value(), "Files retrieved successfully", response));
    }

    @GetMapping("/folder/{folderId}")
    public ResponseEntity<APIResponse<List<FileResponseDTO>>> getFilesByFolder(
        @PathVariable Long folderId,
        @RequestHeader("User-Id") Long userId) {
        List<FileResponseDTO> response = fileService.getFilesByFolder(folderId, userId);
        return ResponseEntity
                .ok(new APIResponse<>(HttpStatus.OK.value(), "Files retrieved successfully", response));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<APIResponse<Void>> deleteFile(
            @PathVariable Long fileId,
            @RequestHeader("User-Id") Long userId) {
        fileService.deleteFile(fileId, userId);
        return ResponseEntity
                .ok(new APIResponse<>(HttpStatus.OK.value(), "File deleted successfully", null));
    }
}