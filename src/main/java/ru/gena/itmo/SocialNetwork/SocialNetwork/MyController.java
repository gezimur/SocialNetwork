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

    @RequestMapping("/css/maincss.css")
    public String mainStyle(){
        return "/css/maincss.css";
    }

    @RequestMapping("/")
    public String root(){
        return "redirect:" + nameOfSite +"/authorization";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model){
        String error = request.getParameter("message");
        model.addAttribute("error", error);
        return "htmlPatterns/Login";
    }

    @RequestMapping("/register")
    public String register(HttpServletRequest request){
        String login = request.getParameter("username");
        MySource instance = MySource.getInstance();
        String message;
        if (instance.findUser(login)){
            message = " this username is already in use";
        }else{
            boolean check = instance.addUser(new User(
                    0,
                    login,
                    request.getParameter("password"),
                    request.getParameter("firstname"),
                    request.getParameter("lastname")));
            message = (check)? "everything is Ok" : "some problems";
        }
        return "redirect:" + nameOfSite + "/login?message=" + message;
    }

    @RequestMapping("/authorization")
    public String check(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        //если авторизирован, то переправить на страницу его профиля
        if (session != null){
            String id = session.getAttribute("id").toString();
            if (!"".equals(id)){
                return "redirect:" + nameOfSite + "/profile/id" + id;
            }
        }
        //Проверка на наличие записи с переданным логином и паролем. Если такая есть авторизировать
        // и переправить на страницу профиля, иначе выдать ошибку.
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        String message = "";
        if (login != null && !"".equals(login)){
            User user = MySource.getInstance().getUser(login, password);
            if (user != null){
                String id = user.getId().toString();
                session = request.getSession();
                session.setAttribute("id", id);
                return  "redirect:" + nameOfSite + "/profile/id" + id;
            }else{
                message = "wrong login or password";
            }
        }
        message = ("".equals(message) )? "please print your login" : message;
        return "redirect:" + nameOfSite +"/login?message=" + message;
    }

    @RequestMapping("/profile/id??????")
    public String profile(HttpServletRequest request, Model model){
        String thisPath = request.getRequestURI();
        String id = thisPath.substring(thisPath.lastIndexOf('/') + 3);
        User thisUser = MySource.getInstance().getInformationOfUser(id);
        model.addAttribute("name",
                thisUser.getFirstname()
                + " "
                + thisUser.getLastname());
        return "htmlPatterns/Profile";
    }

    @RequestMapping("/editing")
    public String editing(HttpServletRequest request, Model model){
        return "htmlPatterns/EditingProfile";
    }

    @RequestMapping("/conversations")
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

    @RequestMapping("/help")
    public String help(HttpServletRequest request, Model model){
        return "htmlPatterns/Search";
    }

    @RequestMapping("/singOut")
    public String singOut(HttpServletRequest request){
        request.getSession().setAttribute("id","");
        return "redirect:" + nameOfSite + "/login";
    }
}
