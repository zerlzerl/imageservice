package cn.micheal.microservice.service.impl;

import cn.micheal.microservice.service.ImageService;
import cn.micheal.microservice.service.handler.CompressionHandler;
import cn.micheal.microservice.service.handler.OriginHandler;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * 处理图片上传请求的服务层抽象接口实现类
 * Created by micheal on 2017/6/26.
 */
@Service
public class ImageServiceImpl implements ImageService {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Value("${localhost}")
    private String localhost;

    @Autowired
    private OriginHandler originHandler;
    @Autowired
    private CompressionHandler compressionHandler;

    @Override
    public String handle(MultipartFile image) throws Exception{
        //图片非空验证
        if (image.isEmpty()){
            return "Image Not Found!";
        }

        try{
            JSONObject response = new JSONObject();
            String imageId = UUID.randomUUID().toString().replace("-","");
            String origin = originHandler.handle(image, imageId);
            String compressed = compressionHandler.handle(image, imageId);
            if (null != origin)
                response.put("origin", localhost + origin);
            if (null != compressed)
                response.put("compressed", localhost + compressed);

            return response.toJSONString();
        } catch (Exception e) {
            logger.error("",e);
            throw new Exception("Something goes wrong in ImageServiceImpl.");
        }
    }

}
