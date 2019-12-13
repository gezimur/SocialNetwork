package ru.gena.itmo.SocialNetwork.SocialNetwork;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.User;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MyController {

    private String nameOfSite = "http://localhost:8080";

    @RequestMapping("/css/logincss.css")
    public String style(){
        return "/css/logincss.css";
    }

    @RequestMapping("/")
    public String root(){
        return "redirect:" + nameOfSite +"/authorization";
    }

    @RequestMapping("/login")
    public String login(){
        return "htmlPatterns/Login";
    }

    @RequestMapping("/register")
    public String register(HttpServletRequest request){
        int id = 123456;
        String username = request.getParameter("username");
        return "redirect:" + nameOfSite + "/profile/id" + id;
    }

    @RequestMapping("/authorization")
    public String check(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null){
            String id = session.getAttribute("id").toString();
            return "redirect:" + nameOfSite + "/profile/id" + id;
        }
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        if (login != null){
            User user = MySource.getInstance().getUser(login, password);
            if (user != null){
                String id = user.getId().toString();
                session = request.getSession();
                session.setAttribute("id", id);
                return  "redirect:" + nameOfSite + "/profile/id" + id;
            }else{
                return "redirect:" + nameOfSite +"/login";
            }
        }
        return "redirect:" + nameOfSite + "/login";
    }

    @RequestMapping("/profile/id??????")
    public String profile(HttpServletRequest request, Model model){
        String thisPath = request.getRequestURI();
        String name = thisPath.substring(thisPath.lastIndexOf('/') + 3);
        model.addAttribute("name", name);
        return "htmlPatterns/Profile";
    }

    @RequestMapping("/profile/id??????/editing")
    public String editing(HttpServletRequest request, Model model){
        return "htmlPatterns/EditingProfile";
    }

    @RequestMapping("/profile/id??????/conversations")
    public String conversations(HttpServletRequest request, Model model){
        return "htmlPatterns/Conversations";
    }

    @RequestMapping("/conversation/id??????")
    public String conversation(HttpServletRequest request, Model model){
        return "htmlPatterns/Conversation";
    }

    @RequestMapping("/pattern/id??????")
    public String pattern(HttpServletRequest request, Model model){
        return "htmlPatterns/Pattern";
    }

    @RequestMapping("/search")
    public String search(HttpServletRequest request, Model model){
        return "htmlPatterns/Search";
    }
}
