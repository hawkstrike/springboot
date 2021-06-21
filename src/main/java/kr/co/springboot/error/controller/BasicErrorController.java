package kr.co.springboot.error.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/")
public class BasicErrorController implements ErrorController {
	
	@RequestMapping("/error")
	public ModelAndView error(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("/error/error.html");
		String errorCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString();
		
		mav.addObject("errorCode", errorCode);
		
		return mav;
	}
}