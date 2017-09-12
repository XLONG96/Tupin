package com.xlong.tupin.controller;

import com.xlong.tupin.TupinRepository.TupinAlbumRepository;
import com.xlong.tupin.TupinRepository.TupinRepository;
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

@Controller
public class HomeController {
    @Autowired
    private TupinAlbumRepository tupinAlbumRepository;

    @RequestMapping(value={"/","/home"},method=RequestMethod.GET)
    public String home(){
        return "home";
    }

    @RequestMapping(value="/album",method=RequestMethod.GET)
    public String album(@RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "14" ) Integer size,Model model){

        Sort sort = new Sort(Sort.Direction.DESC,"publicTime");
        Pageable pageable = new PageRequest(page,size,sort);
        Page pages = tupinAlbumRepository.findAll(pageable);

        model.addAttribute("pages",pages);
        return "album";
    }

    @RequestMapping(value="/about",method=RequestMethod.GET)
    public String about(){
        return "about";
    }

    @RequestMapping(value="/pricing",method=RequestMethod.GET)
    public String pricing(){
        return "pricing";
    }

    @RequestMapping(value="/album-pricing",method=RequestMethod.GET)
    public String Albumpricing(){
        return "album-pricing";
    }

    @RequestMapping(value="/contact",method=RequestMethod.GET)
    public String contact(){
        return "contact";
    }
}
