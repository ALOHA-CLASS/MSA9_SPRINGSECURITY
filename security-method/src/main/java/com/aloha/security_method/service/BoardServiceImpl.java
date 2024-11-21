package com.aloha.security_method.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.security_method.domain.Board;
import com.aloha.security_method.domain.Files;
import com.aloha.security_method.domain.Option;
import com.aloha.security_method.domain.Page;
import com.aloha.security_method.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("BoardService")
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private FileService fileService;
    
    @Override
    public List<Board> list() throws Exception {
        List<Board> list = boardMapper.list(new Option(), new Page());
        return list;
    }

    @Override
    public Board select(String id) throws Exception {
        Board board = boardMapper.select(id);
        return board;
    }

    @Override
    public int insert(Board board) throws Exception {
        int result = boardMapper.insert(board);

        List<MultipartFile> fileList = board.getFileList();

        if( fileList != null ) 
            for (MultipartFile file : fileList) {
                // 빈 파일이 넘어온 경우 
                if (file != null && file.isEmpty()) 
                    continue;
                
                Files uploadFile = new Files();
                uploadFile.setFile(file);
                uploadFile.setParentTable("board");
                uploadFile.setParentNo(board.getNo());
                uploadFile.setType("main");
                fileService.upload(uploadFile);
            }

        return result;
    }

    @Override
    public int update(Board board) throws Exception {
        // 게시글 정보 수정
        int result = boardMapper.update(board);

        // 삭제할 파일 처리
        List<String> deleteFiles = board.getDeleteFiles();
        if( deleteFiles != null && !deleteFiles.isEmpty() )
            for (String fileId : deleteFiles) {
                log.info("fileId : " + fileId);
                fileService.delete(fileId); // 파일 삭제 요청
            }

        return result;
    }

    @Override
    public int delete(String id) throws Exception {
        Board board = boardMapper.select(id);

        // 게시글 삭제
        int result = boardMapper.delete(id);

        // 첨부파일 종속 삭제
        Files deleteFile = new Files();
        deleteFile.setParentTable("board");
        deleteFile.setParentNo(board.getNo());
        int fileResult = fileService.deleteByParent(deleteFile);
        log.info("fileResult : " + fileResult);
        return result;
    }

    // 검색
    @Override
    public List<Board> list(String keyword) throws Exception {
        // List<Board> boardList = boardMapper.list(keyword);
        Option option = new Option();
        option.setKeyword(keyword);
        List<Board> boardList = boardMapper.list(new Option(), new Page());
        return boardList;
    }
    // 검색+옵션
    @Override
    public List<Board> list(Option option) throws Exception {
        List<Board> boardList = boardMapper.list(option, new Page());
        return boardList;
    }

    @Override
    public List<Board> list(Option option, int rows) throws Exception {
        List<Board> boardList = boardMapper.list(option, new Page());
        return boardList;
    }

    @Override
    public List<Board> list(Option option, Page page) throws Exception {
        // 데이터 개수 
        int total = count(option);
        page.setTotal(total);
        
        List<Board> boardList = boardMapper.list(option, page);

        return boardList;
    }

    @Override
    public int count(Option option) throws Exception {
        return boardMapper.count(option);
    }

    @Override
    public boolean isOwner(String id, Long userNo) throws Exception {
        log.info("isOwner - id : " + id);
        log.info("isOwner - userNo : " + userNo);
        Board board = select(id);
        Long boardUserNo = board.getUserNo();
        if( userNo != null && userNo == boardUserNo  ) {
            return true;
        }
        return false;
    }
    
}
