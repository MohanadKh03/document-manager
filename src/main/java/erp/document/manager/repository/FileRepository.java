package erp.document.manager.repository;

import erp.document.manager.entity.File;
import erp.document.manager.entity.Folder;
import erp.document.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByFolder(Folder folder);
    List<File> findByOwner(User owner);
}
