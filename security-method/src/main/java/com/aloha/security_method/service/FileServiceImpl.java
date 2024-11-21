package com.aloha.security_method.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.security_method.domain.Files;
import com.aloha.security_method.mapper.FileMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Value("${upload.path}")        // application.properties 에서 지정한 업로드 경로 가져옴
    private String uploadPath;

    @Override
    public List<Files> list() throws Exception {
        List<Files> fileList = fileMapper.list();
        return fileList;
    }

    @Override
    public Files select(String id) throws Exception {
        Files file = fileMapper.select(id);
        return file;
    }

    @Override
    public int insert(Files file) throws Exception {
        int result = fileMapper.insert(file);
        return result;
    }

    @Override
    public int update(Files file) throws Exception {
        int result = fileMapper.update(file);
        return result;
    }

    /**
     * 파일 삭제
     * 1️⃣ 파일 시스템의 파일 삭제
     * 2️⃣ DB 파일 정보 삭제
     */
    @Override
    public int delete(String id) throws Exception {
        // 1️⃣ 파일 시스템의 파일 삭제
        Files file = select(id);
        String filePath = file.getFilePath();

        File deleteFile = new File(filePath);
        // 파일 존재 여부 확인
        if( !deleteFile.exists() ) {
            log.error("파일이 존재하지 않습니다.");
            log.error("filePath : " + filePath);
            return 0;
        }
        int result = 0;
        if( deleteFile.delete() ) {
            log.info("[FS] 파일 삭제 성공");
            // 2️⃣ DB 파일 정보 삭제
            result = fileMapper.delete(id);
            log.info("[DB] 파일 정보 삭제 성공");
        }
        return result;
    }

    @Override
    public boolean upload(Files file) throws Exception {
        log.info("file : " + file);

        // 파일 정보
        MultipartFile mf = file.getFile();
        String origninName = mf.getOriginalFilename();
        long fileSize = mf.getSize();
        byte[] fileData = mf.getBytes();

        log.info("원본파일명 : " + origninName);
        log.info("파일용량 : " + fileSize);
        log.info("파일데이터 : " + fileData);

        // ⭐ 파일 업로드
        // 1️⃣ 파일 데이터를 업로드 경로에 복사
        // 2️⃣ 업로드된 파일 정보를 DB 에 등록

        // 1️⃣ 파일 복사
        //  * 파일명 중복 방지 : 파일명 앞에 날짜데이터 또는 UID 를 붙여준다.
        String fileName = UUID.randomUUID().toString() + "_" + origninName;
        File uploadFile = new File(uploadPath, fileName);
        // 파일 경로 : C:/upload/UID_강아지.png
        // FileCopyUtils.copy( 파일데이터, 파일객체 )
        FileCopyUtils.copy(fileData, uploadFile);       // 파일 업로드

        // 2️⃣ DB 등록
        file.setFileName(fileName);
        file.setFilePath(uploadFile.getPath());
        file.setFileSize(fileSize);

        fileMapper.insert(file);

        return true;
    }

    @Override
    public List<Files> listByParent(Files file) throws Exception {
        List<Files> fileList = fileMapper.listByParent(file);
        return fileList;
    }

    @Override
    public int deleteByParent(Files file) throws Exception {
        List<Files> deleteFileList = fileMapper.listByParent(file);

        // 1️⃣ 파일 시스템 파일 삭제
        for (Files f : deleteFileList) {
            File deleteFile = new File(f.getFilePath());
            if ( deleteFile.exists() ) {
                deleteFile.delete();
            }
        }
        // 2️⃣ 첨부된 파일 전체 한 번에 삭제
        int result = fileMapper.deleteByParent(file);
        log.info(result + "건의 파일 정보가 삭제되었습니다.");
        return result;
    }
    
}
