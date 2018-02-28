package com.xlong.tupin.controller;

import com.xlong.tupin.Entity.Blog;
import com.xlong.tupin.Entity.Music;
import com.xlong.tupin.Entity.Tupin;
import com.xlong.tupin.Entity.TupinAlbum;
import com.xlong.tupin.TupinRepository.BlogRepository;
import com.xlong.tupin.TupinRepository.MusicRepository;
import com.xlong.tupin.TupinRepository.TupinAlbumRepository;
import com.xlong.tupin.TupinRepository.TupinRepository;
import com.xlong.tupin.Utils.OSSCilentUtils;
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
public class OSSUploadController {
    private static Logger logger = LoggerFactory.getLogger(OSSUploadController.class);

    @Autowired
    TupinRepository tupinRepository;

    @Autowired
    TupinAlbumRepository tupinAlbumRepository;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    MusicRepository musicRepository;


    @RequestMapping(value="/tupin-upload",method=RequestMethod.POST)
    public String picUpload(@RequestParam("title") String title, @RequestPart("pic") MultipartFile pic, HttpServletRequest request) throws IOException {
        logger.info("XXX in tupin-upload");

        String filedir = "images/";
        String filename = UUID.randomUUID().toString()+".jpg";

        if(!pic.isEmpty()){

            OSSCilentUtils.createFolder(filedir);
            String url = OSSCilentUtils.OSSUpload(pic.getInputStream(), filedir + filename);

            Tupin tupin = new Tupin();
            Date date = new Date();

            tupin.setLikeNum(0);
            tupin.setTitle(title);
            tupin.setImg(url);
            tupin.setPublicTime(date);

            tupinRepository.saveAndFlush(tupin);
        }

        return "personal";
    }

    @RequestMapping(value="/album-upload", method=RequestMethod.POST)
    public String albumUpload(@RequestParam("title") String title, @RequestPart("album") MultipartFile album, HttpServletRequest request) throws IOException {
        String filedir = "albums/";
        String filename = UUID.randomUUID().toString()+".jpg";

        if(!album.isEmpty()){

            OSSCilentUtils.createFolder(filedir);
            String url = OSSCilentUtils.OSSUpload(album.getInputStream(), filedir + filename);

            TupinAlbum tupinAlbum = new TupinAlbum();
            Date date = new Date();

            tupinAlbum.setLikeNum(0);
            tupinAlbum.setTitle(title);
            tupinAlbum.setImg(url);
            tupinAlbum.setPublicTime(date);

            tupinAlbumRepository.saveAndFlush(tupinAlbum);
        }


        return "personal-album";
    }

    @RequestMapping(value="/blog-upload", method=RequestMethod.POST)
    public String blogUpload(@RequestParam("title") String title,
                             @RequestParam("theme") String theme, @RequestParam("summary") String summary,
                             @RequestPart("fblog") MultipartFile fblog,
                             HttpServletRequest request) throws IOException {

        String filedir = "blogs/";

        if(!fblog.isEmpty()){
            String filename = fblog.getOriginalFilename();

            OSSCilentUtils.createFolder(filedir);
            String url = OSSCilentUtils.OSSUpload(fblog.getInputStream(), filedir + filename);

            Blog blog = new Blog();
            Date date = new Date();

            if(theme.equals("")){
                theme = "Other";
            }

            blog.setTitle(title);
            blog.setTheme(theme);
            blog.setVisitNum(0);
            blog.setPublicTime(date);
            blog.setSummary(summary + "...");
            blog.setMdContent(url);

            blogRepository.saveAndFlush(blog);
        }

        return "personal-blog";
    }

    @RequestMapping(value="/cover-upload", method=RequestMethod.POST)
    public String coverUpload(@RequestPart("cover") MultipartFile cover, HttpServletRequest request) throws IOException {
        String filedir = "images/";
        String filename = "cover_img.jpg";

        if(!cover.isEmpty()){

            OSSCilentUtils.createFolder(filedir);
            String url = OSSCilentUtils.OSSUpload(cover.getInputStream(), filedir + filename);
        }

        return "personal-cover";
    }

    @RequestMapping(value="/music-upload", method=RequestMethod.POST)
    public String musicUpload(@RequestParam("cover") MultipartFile cover, @RequestPart("src") MultipartFile src,
                              HttpServletRequest request) throws IOException {
        String coverdir = "music/cover/";
        String srcdir = "music/";
        String filename = UUID.randomUUID().toString()+".jpg";

        if(!cover.isEmpty() && !src.isEmpty()){

            String srcFileName = src.getOriginalFilename();

            OSSCilentUtils.createFolder(coverdir);
            String coverUrl = OSSCilentUtils.OSSUpload(cover.getInputStream(), coverdir + filename);

            OSSCilentUtils.createFolder(srcdir);
            String srcUrl = OSSCilentUtils.OSSUpload(src.getInputStream(), srcdir + srcFileName);


            Music music = new Music();

            music.setCover(coverUrl);
            music.setSrc(srcUrl);
            music.setTitle(srcFileName);

            musicRepository.saveAndFlush(music);
        }

        return "personal-music";
    }
}
