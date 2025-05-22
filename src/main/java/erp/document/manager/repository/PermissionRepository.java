package erp.document.manager.repository;

import erp.document.manager.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByFileIdAndUserId(Long fileId, Long userId);
    void deleteByFileIdAndUserId(Long fileId, Long userId);
}