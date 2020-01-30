package com.joezeo.community.controller;

import com.joezeo.community.dto.JsonResult;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.provider.UCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 图床
 */
@Controller
public class GalleryController {

    @Autowired
    private UCloudProvider uCloudProvider;

    @GetMapping("/gallery")
    public String htmGallery() {
        return "gallery";
    }

    @PostMapping("/gallery/upload")
    @ResponseBody
    public JsonResult upload(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request;
        MultipartFile file = mulReq.getFile("pic-name");

        try {
            String url = uCloudProvider.uploadImg(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            return JsonResult.okOf(url);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_FIILED);
        }
    }
}
