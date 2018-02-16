package com.xlong.tupin.controller;

import com.xlong.tupin.Entity.Blog;
import com.xlong.tupin.Entity.Tupin;
import com.xlong.tupin.Entity.TupinAlbum;
import com.xlong.tupin.TupinRepository.BlogRepository;
import com.xlong.tupin.TupinRepository.TupinAlbumRepository;
import com.xlong.tupin.TupinRepository.TupinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Controller
public class UploadController {
    private static Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    TupinRepository tupinRepository;

    @Autowired
    TupinAlbumRepository tupinAlbumRepository;

    @Autowired
    BlogRepository blogRepository;

    @RequestMapping(value="/pic-upload",method=RequestMethod.POST)
    public String picUpload(@RequestParam("title") String title, @RequestPart("pic") MultipartFile pic, HttpServletRequest request) throws IOException {
        logger.info("XXX in picload");

        String filedir = request.getSession().getServletContext().getRealPath("/")+
                "images";
        String filename = UUID.randomUUID().toString()+".jpg";

        if(!pic.isEmpty()){
            File dir = new File(filedir);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            File targetFile = new File(filedir+"/"+filename);
            pic.transferTo(targetFile);

            Tupin tupin = new Tupin();
            Date date = new Date();

            tupin.setTitle(title);
            tupin.setImg("images/"+filename);
            tupin.setPublicTime(date);

            tupinRepository.saveAndFlush(tupin);
        }

        return "personal";
    }

    @RequestMapping(value="/album-upload", method=RequestMethod.POST)
    public String albumUpload(@RequestParam("title") String title, @RequestPart("album") MultipartFile album, HttpServletRequest request) throws IOException {
        String filedir = request.getSession().getServletContext().getRealPath("/")+
                "images/headImg";
        String filename = UUID.randomUUID().toString()+".jpg";

        if(!album.isEmpty()){
            File dir = new File(filedir);
            if(!dir.exists()) {
                dir.mkdirs();
            }

            File targetFile = new File(filedir+"/"+filename);
            album.transferTo(targetFile);

            TupinAlbum tupinAlbum = new TupinAlbum();
            Date date = new Date();

            if(title.equals("") || title == null){
                title = "default";
            }

            tupinAlbum.setTitle(title);
            tupinAlbum.setImg("images/headImg/"+filename);
            tupinAlbum.setPublicTime(date);

            tupinAlbumRepository.saveAndFlush(tupinAlbum);
        }


        return "personal-album";
    }

    @RequestMapping(value="/blog-upload", method=RequestMethod.POST)
    public String blogUpload(@RequestParam("title") String title,
            @RequestParam("theme") String theme,@RequestParam("summary") String summary,
                             @RequestPart("fblog") MultipartFile fblog, HttpServletRequest request) throws IOException {
        String filedir = request.getSession().getServletContext().getRealPath("/")+
                "blogs/";

        if(!fblog.isEmpty()){
            String filename = fblog.getOriginalFilename();
            File dir = new File(filedir);

            if(!dir.exists()) {
                dir.mkdirs();
            }

            File targetFile = new File(filedir + filename);
            fblog.transferTo(targetFile);

            Blog blog = new Blog();
            Date date = new Date();

            if(title == null){
                title = "";
            }

            if(theme.equals("") || theme == null){
                theme = "Other";
            }

            blog.setTitle(title);
            blog.setTheme(theme);
            blog.setVisitNum(0);
            blog.setPublicTime(date);
            blog.setSummary(summary + "...");
            blog.setMdContent(filedir + filename);

            blogRepository.saveAndFlush(blog);
        }

        return "personal-blog";
    }

    @RequestMapping(value="/cover-upload", method=RequestMethod.POST)
    public String coverUpload(@RequestPart("cover") MultipartFile cover, HttpServletRequest request) throws IOException {
        String filedir = request.getSession().getServletContext().getRealPath("/")+
                "images";

        if(!cover.isEmpty()){
            File dir = new File(filedir);

            if(!dir.exists()) {
                dir.mkdirs();
            }

            File targetFile = new File(filedir + "/cover_img.jpg");
            cover.transferTo(targetFile);
        }

        return "summary";
    }
}
