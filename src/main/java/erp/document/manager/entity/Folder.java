package erp.document.manager.entity;

import java.util.List;

public class Folder {

    Long id;

    String name;

    Long parentFolderId;

    List<Folder> folders;

    List<File> files;

}
