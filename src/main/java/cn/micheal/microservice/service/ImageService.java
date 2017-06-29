package cn.micheal.microservice.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 处理图片上传请求的服务层抽象接口类
 * Created by micheal on 2017/6/26.
 */
public interface ImageService {
    /**
     * 对图片进行处理的抽象接口定义
     * @param image MultipartFile类型的对象，为要进行处理的image的对象实例
     * @return 返回处理的结果字符串
     * @throws Exception 处理过程的异常抛出
     */
    String handle (MultipartFile image) throws Exception;
}
