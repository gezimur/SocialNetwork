package ru.gena.itmo.SocialNetwork.SocialNetwork;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.Pattern;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.User;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
public class MyController {

    private String nameOfMySite = "https://jugglersocialnetwork.herokuapp.com";

    @RequestMapping("/css/logincss.css")
    public String style(){
        return "css/logincss.css";
    }

    @RequestMapping("/css/maincss.css")
    public String mainStyle(){
        return "css/maincss.css";
    }

    @RequestMapping("/")
    public String root(){
        return "redirect:" + nameOfMySite +"/authorization";
    }

    @RequestMapping("/login")
    public String login(@RequestParam(required = false) String message,
                        Model model){
        message = (message == null)? "" : message;
        model.addAttribute("error", message);

        return "htmlPatterns/Login";
    }

    @RequestMapping("/register")
    public String register(@RequestParam(required = false) String username,
                           @RequestParam(required = false) String password,
                           @RequestParam(required = false) String firstname,
                           @RequestParam(required = false) String lastname){
        MySource instance = MySource.getInstance();
        String message;
        if (instance.findUser(username)){
            message = " this username is already in use";
        }else{
            boolean check = instance.addUser(new User(
                    0,
                    username,
                    password,
                    "user",
                    firstname,
                    lastname));
            message = (check)? "everything is Ok" : "some problems!";
        }
        return "redirect:" + nameOfMySite + "/login?message=" + message;
    }

    @RequestMapping("/authorization")
    public String check(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        //если авторизирован, то переправить на страницу его профиля

        if (session != null && session.getAttribute("id") != null){
            String id = session.getAttribute("id").toString();
            if (!"".equals(id)){
                if ( "admin".equals(session.getAttribute("userStatus").toString()) ){
                    return  "redirect:" + nameOfMySite + "/admin_workspace";
                }
                return "redirect:" + nameOfMySite + "/profile/id" + id;
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
                String id = Designer.toNeddedForm(user.getId().toString());
                String status = user.getUsersStatus();
                session = request.getSession();
                session.setAttribute("id", id);
                session.setAttribute("userStatus", status);
                if ( "admin".equals(status) ){
                    return  "redirect:" + nameOfMySite + "/admin_workspace";
                }
                return  "redirect:" + nameOfMySite + "/profile/id" + id;
            }else{
                message = "wrong login or password";
            }
        }
        message = ("".equals(message) )? "please print your login and password" : message;
        return "redirect:" + nameOfMySite +"/login?message=" + message;
    }

    @RequestMapping("/admin_workspace")
    public String adminWorkspace(
            HttpSession session,
            Model model){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        model.addAttribute("patternsTree", Designer.createSVGtoPatternsTree(MySource.getInstance().getPatternsTree()));
        return "htmlPatterns/AdminWorkspace";
    }
//доделать
    @RequestMapping("/addPattern")
    public String addPattern(
            HttpSession session,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String descendants,
            @RequestParam(required = false) String ancestors){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        MySource.getInstance().addPattern(name, descendants, ancestors);
        return  "redirect:" + nameOfMySite + "/admin_workspace";
    }

    @RequestMapping("/editingPattern/id{id}")
    public String editingPattern(
            HttpSession session,
            @PathVariable String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String siteswap,
            @RequestParam(required = false) String description,
            Model model){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        if (name != null || siteswap != null || description != null){
            MySource.getInstance().editingPattern(id, name, siteswap, description);
        }

        Pattern p = MySource.getInstance().getPattern(Integer.parseInt(id.replaceFirst("0", "")));
        model.addAttribute("id", id);
        model.addAttribute("nameOfPattern", p.getPatternsName());
        model.addAttribute("siteswapText", p.getSiteswap());
        model.addAttribute("siteswapAnim",
                Designer.textAnalysis("&" + p.getSiteswap() + "&"));
        model.addAttribute("description", p.getDescription());
        return  "htmlPatterns/EditingPattern";
    }
// покрасить дерево javascript
    @RequestMapping("/deletePattern")
    public String deletePattern(
            HttpSession session,
            @RequestParam(required = false) String id){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        MySource.getInstance().deletePattern(Integer.parseInt(id.replaceFirst("0", "")));
        return  "redirect:" + nameOfMySite + "/admin_workspace";
    }

    @RequestMapping("/profile/id{id}")
    public String profile(HttpSession session,
                          @PathVariable String id,
                          Model model){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        MySource instance = MySource.getInstance();
        User thisUser = instance.getInformationOfUser(id);
        model.addAttribute("name",
                thisUser.getFirstname()
                + " "
                + thisUser.getLastname());
        StringBuilder changeFunction = new StringBuilder();
        StringBuilder script = new StringBuilder();
        {
            script.append("\nlet patterns = new Map();\n");
            List<Integer> l = MySource.getInstance().getPatterns(Integer.valueOf(id), "PATTERNSINPROCESS");
            if (l != null){
                for (int i : l) {
                    script.append("patterns.set('"); script.append(i); script.append("', 1);\n");
                }
            }
            l = MySource.getInstance().getPatterns(Integer.valueOf(id), "LEARNEDPATTERNS");
            if (l != null) {
                for (int i : l) {
                    script.append("patterns.set('"); script.append(i); script.append("', 2);\n");
                }
            }
            script.append("for (let i of patterns.keys()){\n");
            script.append("if (patterns.get(i) == 1){\n");
            script.append("patSt(i);\n");
            script.append("}else{\n");
            script.append("patSt(i);\npatSt(i);\n");
            script.append("}\n}\n");
        }
        String bottomStyle = "";
        if (id.equals(session.getAttribute("id"))){
            changeFunction.append("<span class=\"smalBlock\"></span>\n");
            changeFunction.append("<span class=\"editingProfile\">");
            changeFunction.append("<input type=\"text\" name=\"name\" form=\"editing\" placeholder=\"new name\"><br>");
            changeFunction.append("<input type=\"text\" name=\"surname\" form=\"editing\" placeholder=\"new surname\"><br>");
            changeFunction.append("<input type=\"submit\" form=\"editing\" placeholder=\"change\"></span>\n");
            script.append("function patSt(id){\n");
            {
                script.append("e = document.getElementById(id);\n");
                script.append("fill = e.getAttribute('fill')\n");
                script.append("if (fill == 'black' || fill == null){\n");
                    script.append("e.setAttribute('fill', 'orange');\n");
                    script.append("patterns.set(id, 1);\n");
                script.append("}else if (fill == 'orange'){\n");
                    script.append("e.setAttribute('fill', 'red');\n");
                    script.append("patterns.set(id, 2);\n");
                script.append("}else{\n");
                    script.append("e.setAttribute('fill', 'black');\n");
                    script.append("patterns.set(id, 0);\n");
                script.append("}\n}\n");
            }
            script.append("function sendRes(){\n");
            {
                script.append("req = new XMLHttpRequest();\n");
                script.append("url = \"/saveChanges\";\n");
                script.append("if (patterns.size != 0){\n");
                    script.append("url += '?';\n");
                    script.append("for(let i of patterns.keys()){\n");
                        script.append("url += i + '=' + patterns.get(i) + '&';\n");
                    script.append("}\nurl += 'patSize=' + patterns.size;\n}\n");
                script.append("req.open('GET', url);\n");
                script.append("req.send();\n}\n");
            }
            bottomStyle = "display: block;";
        }else{
            script.append("function patSt(id){}\n");
        }
        model.addAttribute("changeFunction", changeFunction);
        model.addAttribute("script", script);
        model.addAttribute("bottomStyle", bottomStyle);
        model.addAttribute("patternsTree",
                Designer.createSVGtoPatternsTree(instance.getPatternsTree()));

        return "htmlPatterns/Profile";
    }
    @RequestMapping("/saveChanges")
    @ResponseBody()
    public void saveChanges(@RequestBody String reqB){
        System.out.println("\n\n" + reqB + "\n\n");
    }
    @RequestMapping("/editing")
    public String editing(HttpSession session,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String surname){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        String id = (String)session.getAttribute("id");
        MySource.getInstance().changeInformationOfUser(name, surname, id);
        return  "redirect:" + nameOfMySite + "/profile/id" + id;
    }

    @RequestMapping("/conversations")
    public String conversations(HttpSession session, Model model){
        String id = session.getAttribute("id").toString();
        String list = MySource.getInstance().getPagingOfUsersConversations(id, 0, 10);
        model.addAttribute("list_of_conv", list);
        return "htmlPatterns/Conversations";
    }

    @RequestMapping("/conversation/id??????")
    public String conversation(HttpSession session){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        return "htmlPatterns/Conversation";
    }

    @RequestMapping("/pattern/id{id}")//
    public String pattern(HttpSession session,
                          @PathVariable String id,
                          Model model){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        if ("admin".equals(session.getAttribute("userStatus").toString())){
            return "redirect:" + nameOfMySite + "/editingPattern/id" + id;
        }
        Pattern p = MySource.getInstance()
                .getPattern(Integer.parseInt(id.replaceFirst("0", "")));
        model.addAttribute("nameOfPattern", p.getPatternsName());
        model.addAttribute("siteswap",
                 Designer.textAnalysis("&" + p.getSiteswap() + "&"));
        model.addAttribute("description", p.getDescription());
        return "htmlPatterns/Pattern";
    }

    @RequestMapping("/search")
    public String search(HttpSession session,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String surname,
                         Model model){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        name = (name == null)? "" : name;
        surname = (surname == null)? "" : surname;
        model.addAttribute("list_of_users",
                MySource.getInstance().getPagingOfUsers(name,surname,0, 10));
        return "htmlPatterns/Search";
    }

    @RequestMapping("/help")
    public String help(HttpSession session){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        return "htmlPatterns/Help";
    }

    @RequestMapping("/singOut")
    public String singOut(HttpServletRequest request){
        request.getSession().setAttribute("id","");
        return "redirect:" + nameOfMySite + "/login";
    }

    private boolean checkUser(HttpSession session) {
        if (session == null) {
            return true;
        } else if (session.getAttribute("id") != null && session.getAttribute("userStatus") != null){
            return "".equals(session.getAttribute("id").toString());
        }else{
            return true;
        }
    }
}
