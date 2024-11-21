package com.aloha.security_method.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.aloha.security_method.domain.Board;
import com.aloha.security_method.domain.CustomUser;
import com.aloha.security_method.domain.Files;
import com.aloha.security_method.domain.Option;
import com.aloha.security_method.domain.Page;
import com.aloha.security_method.service.BoardService;
import com.aloha.security_method.service.FileService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



/**
 * 목록         /board/list     [GET]
 * 조회         /board/select   [GET]
 * 등록         /board/insert   [GET]
 * 등록 처리    /board/insert   [POST]
 * 수정         /board/update   [GET]
 * 수정 처리    /board/update   [POST]
 * 삭제 처리    /board/delete   [POST]
*/
@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileService fileService;
    
    /**
     * 목록
     * @return
     * @throws Exception 
    */
    @GetMapping("/list")
    public String list(Model model
                    //  , @RequestParam(name = "keyword", defaultValue = "") String keyword
                        , Option option
                    // , @RequestParam(name = "rows", defaultValue = "10") int rows
                        , Page page
                     ) throws Exception {
        List<Board> boardList = boardService.list(option, page);
        log.info("###################################################");
        log.info(boardList.toString());
        log.info("###################################################");
        
        model.addAttribute("boardList", boardList);
        model.addAttribute("option", option);
        model.addAttribute("rows", page.getRows());
        model.addAttribute("page", page);

        String pageUrl = UriComponentsBuilder.fromPath("/board/list")
                            // .queryParam("page", page.getPage())
                            .queryParam("keyword", option.getKeyword())
                            .queryParam("code", option.getCode())
                            .queryParam("rows", page.getRows())
                            .queryParam("orderCode", option.getOrderCode())
                            .build()
                            .toUriString();

        model.addAttribute("pageUrl", pageUrl);

        return "/board/list";
    }
    
    /**
     * 조회
     * @param id
     * @return
     * @throws Exception 
    */
    @GetMapping("/select")
    public String select(Model model
                      , @RequestParam("id") String id
                      , Files file) throws Exception {
        
        // 게시글 조회
        Board board = boardService.select(id);
        model.addAttribute("board", board);


        // 파일 목록 조회
        file.setParentNo(board.getNo());
        file.setParentTable("board");

        log.info("file : " + file);
        List<Files> fileList = fileService.listByParent(file);
        model.addAttribute("fileList", fileList);

        return "/board/select";
    }


    // 인증된 사용자 정보를 Model에 추가
    @ModelAttribute("customUser")
    public CustomUser addAuthenticatedUser(@AuthenticationPrincipal CustomUser customUser) {
        return customUser; // 모델에 'customUser'로 등록
    }

    /**
     * 등록
     * @return
     */
    @GetMapping("/insert")
    public String insert() {

        return "/board/insert";
    }
    
    /**
     * 등록 처리
     * @param board
     * @return
     * @throws Exception 
    */
    @PostMapping("/insert")
    public String insertPost(@AuthenticationPrincipal CustomUser customUser
                            , Board board ) throws Exception {
        log.info("board : " + board);
        // 인증된 사용자 no 를 Board 의 userNo 에 넣어줌
        board.setUserNo(customUser.getUser().getNo());
        log.info("userNo : " + customUser.getUser().getNo());
        int result = boardService.insert(board);
        if( result > 0 ) {
            return "redirect:/board/list";
        }
        return "redirect:/board/insert?error";
    }
    
    /**
     * 수정
     * @param id
     * @return
     * @throws Exception 
     *  💛💛💛 #p0, #p1 로 파라미터 인덱스를 지정하여, 가져올 수 있다.
     * 여기서는 요청 파라미터로 넘어온 id ➡ #p0 
     * "@빈이름" 형태로 특정 빈의 메소드를 호출할 수 있다.
     * @Service("BoardService")
    */
    @PreAuthorize("( hasRole('ADMIN')) or (#p0  != null and @BoardService.isOwner(#p0, authentication.principal.user.no))")
    @GetMapping("/update")
    public String update(
                        @RequestParam("id") String id
                       , Model model
                       , Files file
                       ) throws Exception {
        // 게시글 조회
        Board board = boardService.select(id);
        model.addAttribute("board", board);

        // 파일 목록 조회
        // file.setParentNo(board.getNo());
        file.setParentTable("board");

        // log.info("file : " + file);
        // List<Files> fileList = fileService.listByParent(file);
        // model.addAttribute("fileList", fileList);

        return "/board/update";
    }
    
    /**
     * 수정 처리
     * @param board
     * @return
     * @throws Exception 
    */
    @PostMapping("/update")
    public String updatePost(Board board) throws Exception {
        int result = boardService.update(board);
        if( result > 0 ) {
            return "redirect:/board/list";
        }
        return "redirect:/board/update?error&id="+board.getId();
    }
    
    // 삭제 처리
    @PostMapping("/delete")
    public String delete(@RequestParam("id") String id) throws Exception {
        int result = boardService.delete(id);
        if( result > 0 ) 
            return "redirect:/board/list";
        return "redirect:/board/update?error&id="+id;
    }
    
}
