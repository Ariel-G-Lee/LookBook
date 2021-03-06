package com.pjt2.lb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pjt2.lb.common.auth.LBUserDetails;
import com.pjt2.lb.common.response.BaseResponseBody;
import com.pjt2.lb.entity.User;
import com.pjt2.lb.response.LibraryGetRes;
import com.pjt2.lb.response.UserInfoGetRes;
import com.pjt2.lb.service.LibraryService;

@CrossOrigin(
        origins = {"http://localhost:3000", "https://j5a502.p.ssafy.io/"},
        allowCredentials = "true", 
        allowedHeaders = "*", 
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS}
)
@RequestMapping("/libraries")
@RestController
public class LibraryController {
	
	@Autowired
	LibraryService libraryService;
	
	@GetMapping()
	public ResponseEntity<?> getLibrary(Authentication authentication, @RequestParam String bookIsbn, @RequestParam String libGugun){
		try {
			LBUserDetails userDetails = (LBUserDetails) authentication.getDetails();
			User user = userDetails.getUser();
			
			List<LibraryGetRes> libraryList = libraryService.getLibraryList(bookIsbn, libGugun);
			if (libraryList == null) {
				return ResponseEntity.status(401).body(BaseResponseBody.of(401, "검색하신 도서를 소장한 도서관이 없습니다."));
			}
			return ResponseEntity.status(200).body(libraryList);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return ResponseEntity.status(400).body(new UserInfoGetRes(400, "만료된 토큰입니다."));
		}
		
	}
}
