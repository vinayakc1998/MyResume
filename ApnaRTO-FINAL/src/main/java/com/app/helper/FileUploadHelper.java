package com.app.helper;

import java.io.File;
/*import java.io.FileOutputStream;
import java.io.InputStream;*/
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadHelper{

	public final String UPLOAD_DIR="D:\\ACTS\\CDAC Project\\project document folder";

	public boolean uploadFile(MultipartFile file) {
		boolean f = false;

		try {
			Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR + File.separator +file.getOriginalFilename()),
					StandardCopyOption.REPLACE_EXISTING);
			f = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return f;
	}
}
