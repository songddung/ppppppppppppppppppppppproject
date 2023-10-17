package com.example.project.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.model.Board;
import com.example.project.model.Comment;
import com.example.project.model.User;
import com.example.project.repository.BoardRepository;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    HttpSession session;

    // 게시글 리스트
    @GetMapping({ "/list" })
    public String list(Model model,
            @RequestParam(defaultValue = "1") int p) {
        // 내림차순 정렬
        Direction dic = Direction.DESC;
        Sort sort = Sort.by(dic, "id");
        // 페이지 나누기
        Pageable page = PageRequest.of(p - 1, 9, sort);
        Page<Board> boardList = boardRepository.findAll(page);
        model.addAttribute("boardList", boardList.getContent());
        return "board/list";
    }

    // 글쓰기
    @GetMapping("/write")
    public String write() {

        User user = (User) session.getAttribute("user_info");

        if (user == null) {
            log.error("null");
            return "redirect:/auth/signin";
        }
        log.error(user.toString());
        return "board/write";
    }

    @PostMapping("/write")
    public String writePost(@ModelAttribute Board board) {

        String email = (String) session.getAttribute("email");
        User user = userRepository.findByEmail(email);

        if (email != null) {
            board.setUser(user);
            board.setCreDate(new Date());
            boardRepository.save(board);
        } else {
            System.out.println("email값 오류");
        }
        return "redirect:/board/list";
    }

    // 상세보기 페이지
    @GetMapping("/detail")
    public String detail(@RequestParam int id, Model model) {
        Optional<Board> opt = boardRepository.findById(id);
        if(opt.isPresent()){
            Board board = opt.get();
            board.setView(board.getView()+1);
            boardRepository.save(board);

            model.addAttribute("board", board);
        }

        return "board/detail";
    }

    // 게시글 수정
    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model) {
        Optional<Board> opt = boardRepository.findById(id);
        model.addAttribute("board", opt.get());
        model.addAttribute("id", id);
        // 로그인 정보
        String email = (String) session.getAttribute("email");
        User user = userRepository.findByEmail(email);
        Board board = opt.get();
        // 댓글작성자
        User boardUser = board.getUser();
        if (user != null && boardUser != null) {
            if (user.getEmail().equals(boardUser.getEmail())) {

                return "board/edit";
            }
        }
        return "auth/fail4";
    }

    @PostMapping("/edit")
    public String editPost(@ModelAttribute Board reboard,
            @RequestParam Integer id, Model model) {

        User user = (User) session.getAttribute("user_info");
        String loggedUserId = user.getEmail();
        Optional<Board> dbBoard = boardRepository.findById(id);
        if(loggedUserId == null){
            return "auth/fail3";
        }

        if (dbBoard.isPresent()) {
            Board board = dbBoard.get();

            // 게시글 작성자의 UserId 가져오기
            String saveUserId = board.getUser().getEmail();

            // 로그인된 사용자의 userId와 게시글 작성자의 userId 비교
            if (loggedUserId.equals(saveUserId)) {
                // 제목과 내용 수정 및 저장
                board.setTitle(reboard.getTitle());
                board.setContent(reboard.getContent());
                board.setCreDate(new Date());
                boardRepository.save(board);
                return "redirect:/board/detail?id=" + id;

            } else {
                model.addAttribute("id", id);
                return "auth/fail4";
            }
        }
        return "/auth/fail";
    }

    // 댓글
    @PostMapping("/comment")
    public String commnet(@ModelAttribute Comment comment,
            @RequestParam int boardId) {
        Long id = (Long) session.getAttribute("id");

        if (id == null) {
            return "redirect:/auth/signin";
        } else {

            User user = new User();

            user.setId(id);
            user.setEmail(user.getEmail());

            comment.setUser(user);
            comment.setCrDate(new Date());

            Board board = new Board();
            board.setId(boardId);
            comment.setBoard(board);

            commentRepository.save(comment);

        }
        return "redirect:/board/detail?id=" + boardId;
    }

    // 게시물삭제,로그인된이름과 작성자가 같을때 삭제
    @GetMapping("/remove")
    public String remove(@RequestParam int id, Model model) {
        User loggedUserId = (User) session.getAttribute("user_info");

        Optional<Board> dbBoard = boardRepository.findById(id);
            if(loggedUserId == null){
            model.addAttribute("id", id);
            return "auth/fail4";
        }
        if (dbBoard.isPresent()) {
            Board board = dbBoard.get();

            // 게시글 작성자의 email 가져오기
            String saveUserId = board.getUser().getEmail();

            // 댓글먼저 삭제
            Optional<Comment> commentOptional = commentRepository.findById(id);
            if (commentOptional.isPresent()) {
                commentRepository.deleteById(id);
            }
            // 로그인된 사용자의 eamil과 게시글 작성자의 email 비교
            if (loggedUserId.getEmail().equals(saveUserId)) {
                // 게시글 삭제
                boardRepository.delete(board);
                return "redirect:/board/list";
            }
        }
        // 삭제 권한이 없는 경우 또는 게시글을 찾을 수 없는 경우
        model.addAttribute("id", id);
        return "auth/fail4";
    }

    // 댓글 수정 파트
    @GetMapping("/commentEdit")
    public String commentEdit(@RequestParam int id, Model model, @RequestParam int boardId) {
        Optional<Comment> opt = commentRepository.findById(id);
        if (opt.isPresent()) {

            model.addAttribute("comment", opt.get());
            model.addAttribute("boardId", boardId);

            // 로그인한 사용자 정보
            User user = (User) session.getAttribute("user_info");
            Comment comment = opt.get();
            // 댓글 작성자
            User commentUser = comment.getUser();
            if (user != null && commentUser != null) {
                if (user.getEmail().equals(commentUser.getEmail())) {

                    return "/board/commentEdit";
                }
            }
        }
        return "/auth/fail3";
    }

    @PostMapping("/commentEdit")
    public String commentEditpost(@ModelAttribute Comment requestComment,
            Model model, @RequestParam int boardId,
            @RequestParam int id) {

        model.addAttribute("boardId", boardId);
        // 로그인한 사용자 정보
        User user = (User) session.getAttribute("user_info");

        // 댓글 작성자 정보
        // 댓글 아이디 찾기
        Optional<Comment> dbComment = commentRepository.findById(id);
        // 댓글이 있다면
        if (dbComment.isPresent()) {

            Comment comment = dbComment.get();
            User commentUser = comment.getUser();

            // 로그인정보와 댓글작성자 정보가 모두 존재하면
            if (user != null && commentUser != null) {

                // 로그인 정보와 댓글 작성자 정보가 일치 시
                if (user.getEmail().equals(commentUser.getEmail())) {
                    // 댓글수정
                    Board board = new Board();
                    board.setId(boardId);
                    requestComment.setBoard(board);
                    requestComment.setUser(user);
                    requestComment.setCrDate(new Date());
                    commentRepository.save(requestComment);

                    return "redirect:/board/detail?id=" + boardId;
                }
            } else {

                return "/auth/fail3";
            }
        }
        return "/auth/fail3";

    }

    // 댓글 삭제 (작성자와 로그인이 동일시)
    @GetMapping("/comment/remove")

    public String commentRemove(@ModelAttribute Comment requestcomment,
            @RequestParam int boardId,
            @RequestParam int id,
            Model model) {

        User loggedUserId = (User) session.getAttribute("user_info");
        Optional<Comment> dbComment = commentRepository.findById(id);
        if(loggedUserId == null){
            model.addAttribute("boardId", boardId);
            return "auth/fail3";}

        if (dbComment.isPresent()) {
            Comment comment = dbComment.get();

            String saveUserId = comment.getUser().getEmail();

            if (loggedUserId.getEmail().equals(saveUserId)) {

                commentRepository.delete(comment);
                return "redirect:/board/detail?id=" + boardId;
            } else {
                model.addAttribute("boardId", boardId);
                return "/auth/fail3";
            }
        }

        return "redirect:/board/detail?id=" + boardId;
    }


    }
