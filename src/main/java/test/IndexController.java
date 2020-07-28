package test;




import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class IndexController {
    

    @ResponseBody
    @RequestMapping("/ip")
    String ip(HttpServletRequest request)
    {
    	 if (request.getHeader("x-forwarded-for") == null) { 
    		   return request.getRemoteAddr(); 
    		  }else{
    			  System.out.println("proxyip:"+ request.getHeader("x-forwarded-for"));
    		  }
    	 	 
    		  return request.getHeader("x-forwarded-for"); 
    }
    
    @RequestMapping("/")
    String home()
    {
        return "index";
    }
 
    
}