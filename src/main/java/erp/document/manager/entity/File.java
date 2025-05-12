package erp.document.manager.entity;

import java.util.List;

public class File {

    Long id;

    String fileType;

    Long fileSize;

    String description;

    String title;

    List<String> tags;

    Long folderPathId;

    List<Permission> permissions;

}
