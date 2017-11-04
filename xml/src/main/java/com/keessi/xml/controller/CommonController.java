package com.keessi.xml.controller;

import com.keessi.xml.service.FStaffService;
import com.keessi.xml.util.file.FileHelper;
import com.keessi.xml.util.file.LocalFileHelper;
import com.keessi.xml.util.file.PdfHelper;
import com.keessi.xml.util.file.RemoteFileHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Controller
public class CommonController {
    private final FStaffService service;

    @Autowired
    public CommonController(FStaffService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String toLoginPage() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            return "index";
        } else {
            return "common/login";
        }
    }

    @PostMapping("/login")
    public String checkLogin(String username, String password, String rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject currentUser = SecurityUtils.getSubject();
        try {
            if (!currentUser.isAuthenticated()) {
                token.setRememberMe(rememberMe.equals("true"));
                currentUser.login(token);
            }
        } catch (Exception e) {
            return "common/login";
        }
        return "index";
    }

    @GetMapping("/logout")
    public String logout() {
        return "common/login";
    }

    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "common/unauthorized";
    }

    @GetMapping("/exportPdf")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "FDD173526");
        PdfHelper helper = new PdfHelper(request, response);
        helper.createPdf("index", "单据.pdf", attributes);
    }

    @PostMapping("/uploadLocal")
    public String uploadLocal(@RequestParam("file") MultipartFile file) throws IOException {
        FileHelper helper = new LocalFileHelper();
        return "redirect:" + helper.uploadOne(file);
    }

    @PostMapping("/uploadRemote")
    @ResponseBody
    public Map<String, Object> uploadRemote(@RequestParam("imgFile") MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        FileHelper helper = new RemoteFileHelper();
        map.put("error", 0);
        map.put("url", helper.uploadOne(file));
        return map;
    }

    @GetMapping("/toJson")
    @ResponseBody
    public Map<String, Object> toJson() {
        Map<String, Object> model = new HashMap<>();
        model.put("one", "a list");
        model.put("two", service.findAll());
        return model;
    }
}
