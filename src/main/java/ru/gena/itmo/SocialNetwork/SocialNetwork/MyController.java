package ru.gena.itmo.SocialNetwork.SocialNetwork;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.Message;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.Pattern;
import ru.gena.itmo.SocialNetwork.SocialNetwork.content.User;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            @RequestParam(required = false) String name){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        MySource.getInstance().deletePattern(name);
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
            Map<Integer, Integer> l = MySource.getInstance().getPatterns(Integer.valueOf(id));
            if (l != null){
                for (int i : l.keySet()) {
                    script.append("patterns.set('"); script.append(i); script.append("', ");
                    script.append(l.get(i)); script.append(");\n");
                }
            }
            script.append("for (let i of patterns.keys()){\n");
            script.append("if (patterns.get(i) == 1){\n");
            script.append("fillFunc(i);\n");
            script.append("}else{\n");
            script.append("fillFunc(i);\nfillFunc(i);\n");
            script.append("}\n}\n");
        }
        script.append("function fillFunc(id){\n");
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
        String buttonStyle = "";
        if (id.equals(session.getAttribute("id"))){
            changeFunction.append("<span class=\"smalBlock\"></span>\n");
            changeFunction.append("<span class=\"editingProfile\">");
            changeFunction.append("<input type=\"text\" name=\"name\" form=\"editing\" placeholder=\"new name\" value=\"");
            changeFunction.append(thisUser.getFirstname());
            changeFunction.append("\"><br><input type=\"text\" name=\"surname\" form=\"editing\" placeholder=\"new surname\" value=\"");
            changeFunction.append(thisUser.getLastname());
            changeFunction.append("\"><br><input type=\"submit\" form=\"editing\" placeholder=\"change\"></span>\n");
            script.append("function patSt(id){\nfillFunc(id)\n}\n");
            script.append("function sendRes(){\n");
            {
                script.append("req = new XMLHttpRequest();\n");
                script.append("url = \"/saveChanges\";\n");
                script.append("if (patterns.size != 0){\n");
                    script.append("url += '?';\n");
                    script.append("for(let i of patterns.keys()){\n");
                        script.append("url += i + '=' + patterns.get(i) + '&';\n");
                    script.append("}\nurl += 'id=' + "); script.append(id); script.append(";\n}\n");
                script.append("req.open('GET', url);\n");
                script.append("req.send();\n}\n");
            }
            buttonStyle = "display: block;";
        }else{
            script.append("function patSt(id){}\n");
        }
        model.addAttribute("changeFunction", changeFunction);
        model.addAttribute("script", script);
        model.addAttribute("buttonStyle", buttonStyle);
        model.addAttribute("patternsTree",
                Designer.createSVGtoPatternsTree(instance.getPatternsTree()));

        return "htmlPatterns/Profile";
    }
    @RequestMapping("/saveChanges")
    @ResponseBody()
    public void saveChanges(@RequestParam Map<String, String> allParams){
        if (allParams.containsKey("id")){
            int id = Integer.valueOf(allParams.get("id"));
            allParams.remove("id");
            Map<Integer, Integer> newPatterns = new HashMap<>();
            for (String k : allParams.keySet()){
                newPatterns.put(Integer.valueOf(k), Integer.valueOf(allParams.get(k)));
            }
            MySource.getInstance().updatePatterns(id, newPatterns);
            System.out.println("\n\n" + allParams.toString() + "\n\n");
        }
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

    @RequestMapping("/addUserConversation")
    @ResponseBody()
    public String addUserConversation(HttpSession session,
                                    @RequestParam Map<String, String> allParams){
        if (checkUser(session)){
            return "you have bed session";
        }
        MySource instance = MySource.getInstance();
        User user = instance.getInformationOfUser(session.getAttribute("id").toString());
        if (user != null){
            return instance.addRecordUsersConversation(user.getId(),
                    allParams.get("invited"),
                    allParams.get("conversation"));
        }else{
            return "can not find you in our database";
        }
    }
//deleteUsersConversations
    @RequestMapping("/deleteUserConversation")
    @ResponseBody()
    public String deleteUserConversation(HttpSession session,
                                  @RequestParam String conversationId){
        if (checkUser(session)){
            return "you have bed session";
        }
        MySource instance = MySource.getInstance();
        User user = instance.getInformationOfUser(session.getAttribute("id").toString());
        if (user != null){
            return instance.deleteUsersConversations(user.getId(), conversationId);
        }else{
            return "can not find you in our database";
        }
    }

    @RequestMapping("/conversation/id??????")
    public String conversation(HttpSession session){
        if (checkUser(session)){
            return "redirect:" + nameOfMySite +"/login";
        }
        return "htmlPatterns/Conversation";
    }

    @RequestMapping("/sendMessage")
    @ResponseBody()
    public String sendMessage(HttpSession session,
                              @RequestParam Map<String, String> allParams){
        if (checkUser(session)){
            return "you have bed session";
        }
        //надо сохранять сообщения
        MySource instance = MySource.getInstance();
        if (!"".equals(allParams.get("message"))) {
            instance.saveMessage(
                    allParams.get("conv"),
                    session.getAttribute("id").toString(),
                    allParams.get("message")
            );
        }
        List<Message> m = instance.getMessagesFromId(Integer.valueOf(allParams.get("lastId")));
        try {
            if (m.size() == 0) return "not";
            for (Message i : m){
                i.text = Designer.textAnalysis(i.text);
            }
            String ans = new ObjectMapper().writeValueAsString(m);
            System.out.println("\n\n" + ans + "\n\n");
            return ans;
        }catch(JsonProcessingException e){
            e.printStackTrace();
            return ";0";
        }
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
    public String singOut(HttpSession session){
        session.setAttribute("id","");
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
