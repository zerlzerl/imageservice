package cn.micheal.microservice.controller;

import cn.micheal.microservice.service.ImageService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 接收上传请求的Controller
 * Created by micheal on 2017/6/26.
 */
@Controller
@RequestMapping(value = "/upload")
public class UploadController {
    private static Logger logger = LoggerFactory.getLogger(UploadController.class);

    private final ImageService imageService;

    @Autowired
    public UploadController(ImageService imageService) {
        this.imageService = imageService;
    }


    @ResponseBody
    @RequestMapping(value = "/img")
    public String uploadImage (@RequestParam("imageFile") MultipartFile file) {
        String result;
        try {
            result = imageService.handle(file);
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject error = new JSONObject();
            error.put("type", -1);
            error.put("content", e.getMessage());
            return  error.toJSONString();
        }
        JSONObject response = new JSONObject();
        response.put("type", 1);
        response.put("content", result);
        return response.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/image")
    public String image (@RequestParam("imageFile") MultipartFile image) {
        try {
            long start = System.currentTimeMillis();
            String response = imageService.handle(image);
            long end = System.currentTimeMillis();
            logger.info("Size : {} KB, Name : {}, Cost : {} ms, Response : {}",
                    image.getSize()/1024, image.getOriginalFilename(), end - start, response);
            return response;
        } catch (Exception e) {
            logger.error("", e);
            return "Internal Server Error Occurred!";
        }
    }
}
