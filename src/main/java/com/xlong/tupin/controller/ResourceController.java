package com.xlong.tupin.controller;

import com.xlong.tupin.Entity.Blog;
import com.xlong.tupin.TupinRepository.BlogRepository;
import com.xlong.tupin.TupinRepository.TupinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/api")
public class ResourceController {
    @Autowired
    private TupinRepository tupinRepository;

    @Autowired
    private BlogRepository blogRepository;

    @RequestMapping(value="/imgs", method= RequestMethod.GET)
    public Page getImgs(@RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "8" ) Integer size){
        Sort sort = new Sort(Sort.Direction.DESC,"publicTime");
        Pageable pageable = new PageRequest(page,size,sort);
        return tupinRepository.findAll(pageable);
    }

    @RequestMapping(value="/summary", method=RequestMethod.GET)
    public Page getSummary(@RequestParam(value = "page", defaultValue = "0") Integer page,
                           @RequestParam(value = "size", defaultValue = "8") Integer size){
        Sort sort = new Sort(Sort.Direction.DESC, "publicTime");
        Pageable pageable = new PageRequest(page, size, sort);
        return blogRepository.findAll(pageable);
    }

    @RequestMapping(value="/blog", method=RequestMethod.GET)
    public Blog getBlog(@RequestParam(value = "id") Long id){
        //get blog's markdown content by path
        Blog blog = blogRepository.findOne(id);

        if(blog == null){
            //404
        }

        String mdPath = blog.getMdContent();
        File file = new File(mdPath);
        FileInputStream fileInputStream;
        Long len = file.length();
        byte[] byteContent = new byte[len.intValue()];

        try {
            fileInputStream  = new FileInputStream(file);
            fileInputStream.read(byteContent);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            //404
            return null;
        }

        blog.setMdContent(new String(byteContent));
        return blog;
    }
}
