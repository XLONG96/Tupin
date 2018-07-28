package com.xlong.tupin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xlong.tupin.Entity.*;
import com.xlong.tupin.TupinRepository.*;
import com.xlong.tupin.Utils.IpUtils;
import com.xlong.tupin.Utils.OtherUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class ResourceController {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ResourceController.class);

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
    private SummaryRepository summaryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value="/isLogin",method=RequestMethod.GET)
    public boolean getIsLogin(HttpServletRequest request) throws JsonProcessingException {
        return request.getSession().getAttribute("admin") != null;
    }

    @RequestMapping(value="/tupin", method=RequestMethod.GET)
    public Page getTupin(@RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "24" ) Integer size){
        Sort sort = new Sort(Sort.Direction.DESC,"publicTime");
        Pageable pageable = new PageRequest(page,size,sort);
        Page pageContent = tupinRepository.findAll(pageable);

        for (Object o : pageContent.getContent()){
            Tupin tupin = (Tupin)o;
            tupin.setTitle(tupin.getTitle().replaceAll("\n", "<br/>"));
        }

        return pageContent;
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
                           @RequestParam("theme") String theme,
                           @RequestParam(value = "size", defaultValue = "8") Integer size){
        Sort sort = new Sort(Sort.Direction.DESC, "publicTime");
        Pageable pageable = new PageRequest(page, size, sort);
        return blogRepository.findAllByTheme(pageable, theme);
    }

    @RequestMapping(value="/blog", method=RequestMethod.GET)
    public Blog getBlog(@RequestParam(value = "id") Long id) throws Exception {
        // get blog's markdown content by path
        Blog blog = blogRepository.findOne(id);

        if (blog == null){
            return null;
        }

        URL url =  new URL(blog.getMdContent());

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setConnectTimeout(3 * 1000);

        InputStream inputStream = httpURLConnection.getInputStream();

        String content = OtherUtils.convertStreamToString(inputStream);

        blog.setMdContent(content);

        // 设置上一篇和下一篇blog
        List<Blog> listBlog = blogRepository.findAllByTheme(blog.getTheme());
        Blog lastBlog;
        Blog nextBlog;
        int currentBlogIndex = listBlog.indexOf(blog);
        int lastBlogIndex = currentBlogIndex - 1;
        int nextBlogIndex = currentBlogIndex + 1;

        if (lastBlogIndex >= 0){
            lastBlog = listBlog.get(lastBlogIndex);
            blog.setLastId(lastBlog.getId());
            blog.setLastBlogTitle(lastBlog.getTitle());
        }

        if (nextBlogIndex < listBlog.size()){
            nextBlog = listBlog.get(nextBlogIndex);
            blog.setNextId(nextBlog.getId());
            blog.setNextBlogTitle(nextBlog.getTitle());
        }

        return blog;
    }

    @RequestMapping(value="/comment", method=RequestMethod.GET)
    public Page<Comment> getComment(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                    @RequestParam(value="blogId") Long blogId,
                                    @RequestParam(value = "size", defaultValue = "10" ) Integer size){
        Sort sort = new Sort(Sort.Direction.ASC,"publicTime");
        Pageable pageable = new PageRequest(page,size,sort);

        return commentRepository.findAllByBlogId(pageable, blogId);
    }

    @RequestMapping(value="/comment", method=RequestMethod.POST)
    public Comment saveComment(@RequestParam(value="name") String name,@RequestParam(value="blogId") Long blogId,
                            @RequestParam(value="content") String content,HttpServletRequest request){
        if (name != null && content != null){
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

    @RequestMapping(value="/search", method=RequestMethod.GET)
    public List<Summary> search(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                @RequestParam(value="keyword") String keyword,
                                @RequestParam(value = "size", defaultValue = "10") Integer size){
        Sort sort = new Sort(Sort.Direction.DESC, "publicTime");
        Pageable pageable = new PageRequest(page, size, sort);
        QueryBuilder qb = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title", keyword))
                .should(QueryBuilders.matchQuery("blogSummary", keyword));
        SearchQuery query =
                new NativeSearchQueryBuilder().withQuery(qb).withPageable(pageable).build();
        Page p =  summaryRepository.search(query);

        System.out.println(p.getContent());
        return p.getContent();
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
