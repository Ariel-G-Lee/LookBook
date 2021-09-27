package com.pjt2.lb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pjt2.lb.common.auth.LBUserDetails;
import com.pjt2.lb.entity.User;
import com.pjt2.lb.response.BookInfoGetRes;
import com.pjt2.lb.response.BookListInfoRes;
import com.pjt2.lb.response.UserInfoGetRes;
import com.pjt2.lb.service.BookService;

@RequestMapping("/books")
@RestController
public class BookController {

	@Autowired
	BookService bookService;
	
	@GetMapping("/{bookIsbn}")
	public ResponseEntity<?> getBookInfo(Authentication authentication, @PathVariable(required = true) String bookIsbn){
		try {
			LBUserDetails userDetails = (LBUserDetails) authentication.getDetails();
			User user = userDetails.getUser();
			
			BookInfoGetRes bookInfo = bookService.getBookInfo(bookIsbn, user);
			return ResponseEntity.status(200).body(bookInfo);
		} catch (NullPointerException e) {
			
			return ResponseEntity.status(400).body(new UserInfoGetRes(400, "만료된 토큰입니다."));
		}
		
	}
	
	@GetMapping()
	public ResponseEntity<?> getSearchBookInfo(Authentication authentication, @RequestParam String searchKey, @RequestParam String searchWord){

		try {
			LBUserDetails userDetails = (LBUserDetails) authentication.getDetails();
			User user = userDetails.getUser();
			
			List<BookListInfoRes> searchBookList = bookService.getSearchBookInfo(searchKey, searchWord);
			Map<String, List> map = new HashMap<String, List>();
			map.put("searchBookList", searchBookList);
			
			return ResponseEntity.status(200).body(map);
		} catch (NullPointerException e) {
			
			return ResponseEntity.status(400).body(new UserInfoGetRes(400, "만료된 토큰입니다."));
		}
	}
	
}
