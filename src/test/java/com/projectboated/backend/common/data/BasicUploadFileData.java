package com.projectboated.backend.common.data;

import com.projectboated.backend.domain.uploadfile.entity.UploadFile;

public abstract class BasicUploadFileData {

    public final static String EXT = "ext";
    public final static String ORIGINAL_FILE_NAME_WITH_EXT = "originalFileName" + EXT;
    public final static String ORIGINAL_FILE_NAME = "originalFileName";
    public final static String SAVE_FILE_NAME = "saveFileName";
    public final static String MEDIATYPE = "mediaType";
    public final static UploadFile UPLOADFILE = new UploadFile(ORIGINAL_FILE_NAME_WITH_EXT, SAVE_FILE_NAME, MEDIATYPE);

}
