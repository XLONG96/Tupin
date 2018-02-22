package com.xlong.tupin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xlong.tupin.Entity.Blog;
import com.xlong.tupin.Entity.Comment;
import com.xlong.tupin.Entity.Music;
import com.xlong.tupin.TupinRepository.*;
import com.xlong.tupin.Utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ResourceController {
    @Autowired
    private TupinRepository tupinRepository;

    @Autowired
    private TupinAlbumRepository tupinAlbumRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value="/isLogin",method=RequestMethod.GET)
    public boolean getIsLogin(HttpServletRequest request) throws JsonProcessingException {
        return request.getSession().getAttribute("admin") != null;
    }

    @RequestMapping(value="/tupin", method= RequestMethod.GET)
    public Page getTupin(@RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "24" ) Integer size){
        Sort sort = new Sort(Sort.Direction.DESC,"publicTime");
        Pageable pageable = new PageRequest(page,size,sort);
        return tupinRepository.findAll(pageable);
    }

    @Transactional
    @RequestMapping(value="/tupin", method=RequestMethod.DELETE)
    public void delTupin(@RequestParam("id") Long tupinId){
        tupinRepository.deleteTupinById(tupinId);
    }

    @Transactional
    @RequestMapping(value="/album", method=RequestMethod.DELETE)
    public void delAlbum(@RequestParam("id") Long albumId){
        tupinAlbumRepository.deleteTupinAlbumById(albumId);
    }

    @Transactional
    @RequestMapping(value="/summary", method=RequestMethod.DELETE)
    public void delSummary(@RequestParam("id") Long blogId){
        blogRepository.deleteBlogById(blogId);
    }

    @RequestMapping(value="/summary", method=RequestMethod.GET)
    public Page getSummary(@RequestParam(value = "page", defaultValue = "0") Integer page,
                           @RequestParam(value = "size", defaultValue = "8") Integer size){
        Sort sort = new Sort(Sort.Direction.DESC, "publicTime");
        Pageable pageable = new PageRequest(page, size, sort);
        return blogRepository.findAll(pageable);
    }

    @RequestMapping(value="/blog", method=RequestMethod.GET)
    public Blog getBlog(@RequestParam(value = "id") Long id) throws Exception {
        //get blog's markdown content by path
        Blog blog = blogRepository.findOne(id);

        if(blog == null){
            return null;
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
            return null;
        }

        blog.setMdContent(new String(byteContent));

        //设置上一篇和下一篇blog
        Blog lastBlog = blogRepository.findOne(id-1);
        Blog nextBlog = blogRepository.findOne(id+1);

        if(lastBlog != null){
            blog.setLastId(lastBlog.getId());
            blog.setLastBlogTitle(lastBlog.getTitle());
        }

        if(nextBlog != null){
            blog.setNextId(nextBlog.getId());
            blog.setNextBlogTitle(nextBlog.getTitle());
        }

        return blog;
    }

    @RequestMapping(value="/comment", method=RequestMethod.GET)
    public List<Comment> getComment(@RequestParam(value="blogId") Long blogId){
        List<Comment> list =  commentRepository.findAllByBlogId(blogId);
        return list;
    }

    @RequestMapping(value="/comment", method=RequestMethod.POST)
    public Comment saveComment(@RequestParam(value="name") String name,@RequestParam(value="blogId") Long blogId,
                            @RequestParam(value="content") String content,HttpServletRequest request){
        if(name != null && content != null){
            Comment comment = new Comment();
            Date date = new Date();

            comment.setName(name);
            comment.setBlogId(blogId);
            comment.setPublicTime(date);
            comment.setContent(content);
            comment.setIpAddr(IpUtils.getIpAddr(request));

            commentRepository.saveAndFlush(comment);
            return comment;
        }
        return null;
    }

    @Transactional
    @RequestMapping(value="/comment", method=RequestMethod.DELETE)
    public void delComment(@RequestParam(value="id") Long commentId){
       commentRepository.deleteCommentById(commentId);
    }

    @RequestMapping(value="/music", method=RequestMethod.GET)
    public List<Music> getMusic(){
        List<Music> list = musicRepository.findAll();
        return list;
    }

    @Transactional
    @RequestMapping(value="/visitNum", method=RequestMethod.GET)
    public String updateVisitNum(@RequestParam(value="blogId") Long blogId){
        blogRepository.increaseVisitNum(blogId);
        return Long.toString(blogRepository.findOne(blogId).getVisitNum());
    }

    @Transactional
    @RequestMapping(value="/addLikeNum", method=RequestMethod.GET)
    public String addLikeNum(@RequestParam(value="id") Long tupinId){
        tupinRepository.increaseLikeNum(tupinId);
        return Long.toString(tupinRepository.findOne(tupinId).getLikeNum());
    }

    @Transactional
    @RequestMapping(value="/subLikeNum", method=RequestMethod.GET)
    public String subLikeNum(@RequestParam(value="id") Long tupinId){
        tupinRepository.decreaseLikeNum(tupinId);
        return Long.toString(tupinRepository.findOne(tupinId).getLikeNum());
    }
}
