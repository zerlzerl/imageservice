package cn.micheal.microservice.service.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 原图处理器
 * Created by micheal on 2017/6/29.
 */
@Component
public class OriginHandler implements InitializingBean{
    private static Logger logger = LoggerFactory.getLogger(OriginHandler.class);
    private LocalDate date;
    private Pattern pattern;
    //注入处理的图片格式正则
    @Value("${path.image.origin.regex}")
    private String formatRegex;
    @Value("${path.image.origin.location}")
    private String location;
    @Value("${path.image.origin.mapping}")
    private String mapping;

    public String handle(MultipartFile image, String id){
        try{
            //图片格式验证
            Matcher matcher = pattern.matcher(image.getOriginalFilename());
            if(!matcher.find()){
                return null;
            }
            //图片后缀名
            String suffix = matcher.group();
            //获取图片本地文件夹地址
            String localPath = getLocalPath();
            //获取图片映射地址
            String mappingPath = getMappingPath();
            //创建文件夹
            File path = new File(localPath);
            if(!path.exists())
                path.mkdirs();
            //创建图片文件
            File img = new File(localPath, id + suffix);
            //创建输入输出流
            InputStream inputStream = image.getInputStream();
            OutputStream outputStream = new FileOutputStream(img);
            //保存
            save(inputStream, outputStream);
            return mappingPath + img.getName();
        } catch (Exception e) {
            logger.error("",e);
            return null;
        }

    }

    private String getPath () {
        date = LocalDate.now();
        return date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth() + "/";
    }

    private String getLocalPath () {
        return location + getPath();
    }

    private String getMappingPath () {
        return mapping + getPath();
    }

    private void save (InputStream is, OutputStream os) throws IOException {
        byte buffer[] = new byte[1024];
        while (is.read(buffer) != -1) {
            os.write(buffer);
        }
        is.close();
        os.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //对正则预编译以提高性能
        this.pattern = Pattern.compile(formatRegex);
    }
}
