package com.xlong.tupin.controller;

import com.xlong.tupin.Utils.OSSCilentUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class OSSResourceController {
/*
    @RequestMapping(value="images/{name}")
    public void getImages(@PathVariable(name = "name") String name,
                          HttpServletRequest request, HttpServletResponse response) {
        byte[] data = OSSCilentUtils.OSSDownload("images/" + name);
        try {
            response.setContentType("image/jpeg");
            OutputStream output = response.getOutputStream();
            output.write(data);
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}
