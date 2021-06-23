package kr.co.springboot.main;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/")
public class MainController {
	
	@RequestMapping("/")
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("main/index");
	}
}