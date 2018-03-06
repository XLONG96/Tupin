package com.xlong.tupin.Filter;

import com.xlong.tupin.Entity.CustomIP;
import com.xlong.tupin.TupinRepository.CustomIPRepository;
import com.xlong.tupin.Utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@WebFilter(urlPatterns = "/*")
public class IpRecordFilter implements Filter {
    @Autowired
    CustomIPRepository customIPRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String ip = IpUtils.getIpAddr((HttpServletRequest) servletRequest);

        //System.out.println("XXX do Filter"+" ip :"+ip);

        if (customIPRepository == null){
            filterChain.doFilter(servletRequest, servletResponse);
        }

        // 如果ip不存在，则生成新纪录，访问数次初始化为1
        // 如果ip存在，则在原纪录的访问次数自增1
        System.out.println("XXX custom Respository:"+customIPRepository);

        CustomIP customIP = customIPRepository.findByIpAddr(ip);
        if (customIP != null){
            // 每一个ip每隔24小时访问则会自增1
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(customIP.getVisitTime());
            calendar.add(Calendar.HOUR_OF_DAY, 24);
            if(date.after(calendar.getTime())){
                customIP.setVisitNum(customIP.getVisitNum() + 1);
            }
        }else {
            customIP = new CustomIP();
            customIP.setIpAddr(ip);
            customIP.setVisitNum(1);
            customIP.setVisitTime(new Date());
        }

        customIPRepository.saveAndFlush(customIP);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
