package com.xlong.tupin.controller;

import com.xlong.tupin.Entity.Admin;
import com.xlong.tupin.TupinRepository.AdminRepository;
import com.xlong.tupin.TupinRepository.TupinAlbumRepository;
import com.xlong.tupin.TupinRepository.TupinRepository;
import com.xlong.tupin.Utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    @Autowired
    private TupinAlbumRepository tupinAlbumRepository;

    @Autowired
    private AdminRepository adminRepository;

    @RequestMapping(value={"/","/home"},method=RequestMethod.GET)
    public String home(){
        return "home";
    }

    @RequestMapping(value="/album",method=RequestMethod.GET)
    public String album(@RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "24" ) Integer size,Model model){

        Sort sort = new Sort(Sort.Direction.DESC,"publicTime");
        Pageable pageable = new PageRequest(page,size,sort);
        Page pages = tupinAlbumRepository.findAll(pageable);

        model.addAttribute("pages",pages);
        return "album";
    }

    @RequestMapping(value="/summary",method=RequestMethod.GET)
    public String summary(){
        return "summary";
    }

    @RequestMapping(value="/blog",method=RequestMethod.GET)
    public String blog(){
        return "blog";
    }

    @RequestMapping(value="/login",method=RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String loginAndVerify(@RequestParam("username") String name, @RequestParam("password") String password,
                                Model model, HttpServletRequest request){
        boolean error = false;
        Admin admin = adminRepository.findAdminByName(name);

        if(admin == null){
            error = true;
            model.addAttribute("error",error);
            return "login";
        }
        else{
            //MD5
            password = MD5Utils.getMD5(password);
            if(!password.equals(admin.getPassword())){
                error = true;
                model.addAttribute("error",error);
                return "login";
            }
        }

        model.addAttribute("error",error);
        request.getSession().setAttribute("admin",admin);
        return "personal";
    }

    private boolean isLogin(HttpServletRequest request){
        return request.getSession().getAttribute("admin") != null;
    }

    @RequestMapping(value="/personal",method=RequestMethod.GET)
    public String personal(HttpServletRequest request){
        if(isLogin(request)){
            return "personal";
        }
        else{
            return "login";
        }
    }

    @RequestMapping(value="/personal-album",method=RequestMethod.GET)
    public String personalAlbum(HttpServletRequest request){
        if(isLogin(request)){
            return "personal-album";
        }
        else{
            return "login";
        }
    }

    @RequestMapping(value="/personal-blog",method=RequestMethod.GET)
    public String personalBlog(HttpServletRequest request){
        if(isLogin(request)){
            return "personal-blog";
        }
        else{
            return "login";
        }
    }

    @RequestMapping(value="/personal-cover",method=RequestMethod.GET)
    public String personalCover(HttpServletRequest request){
        if(isLogin(request)){
            return "personal-cover";
        }
        else{
            return "login";
        }
    }

    @RequestMapping(value="/about",method=RequestMethod.GET)
    public String about(){
        return "about";
    }
}
