package erp.document.manager.repository;

import erp.document.manager.entity.Folder;
import erp.document.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByOwnerAndParentFolderIsNull(User owner);
    List<Folder> findByParentFolder(Folder parentFolder);
}
