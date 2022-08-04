package com.projectboated.backend.common.data;

import com.projectboated.backend.domain.uploadfile.entity.UploadFile;

public abstract class BasicDataUploadFile {

    public final static Long UPLOAD_FILE_ID = 202L;
    public final static String EXT = "ext";
    public final static String ORIGINAL_FILE_NAME = "originalFileName" + "." + EXT;
    public final static String ORIGINAL_FILE_NAME_NO_EXT = "originalFileName";
    public final static String SAVE_FILE_NAME = "saveFileName";
    public final static String MEDIATYPE = "mediaType";
    public final static UploadFile UPLOADFILE = new UploadFile(UPLOAD_FILE_ID, ORIGINAL_FILE_NAME, SAVE_FILE_NAME, MEDIATYPE);

}
