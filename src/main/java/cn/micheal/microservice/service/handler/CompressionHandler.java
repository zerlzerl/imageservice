package cn.micheal.microservice.service.handler;

import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 压缩图处理器
 * Created by Micheal on 2017/6/29.
 */
@Component
public class CompressionHandler implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(CompressionHandler.class);
    private LocalDate date;
    private Pattern pattern;
    //注入处理的图片格式正则
    @Value("${path.image.compressed.regex}")
    private String formatRegex;
    @Value("${path.image.compressed.location}")
    private String location;
    @Value("${path.image.compressed.mapping}")
    private String mapping;

    //尺寸大小注入
    @Value("${path.image.compressed.size.width}")
    private int width;
    @Value("${path.image.compressed.size.height}")
    private int height;

    public String handle(MultipartFile image, String id) {
        try {
            //图片格式验证
            Matcher matcher = pattern.matcher(image.getOriginalFilename());
            if (!matcher.find()) {
                return null;
            }
            //获取图片本地文件夹地址
            String localPath = getLocalPath();
            //获取图片映射地址
            String mappingPath = getMappingPath();
            //创建文件夹
            File path = new File(localPath);
            if (!path.exists())
                path.mkdirs();
            //创建图片文件
            File img = new File(localPath, id + ".jpg");
            //获取image宽高
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            Thumbnails.of(bufferedImage)
                    .size(width, height)
                    .outputQuality(0.7f)
                    .toFile(img);

            return mappingPath + img.getName();
        } catch (IOException e) {
            logger.error("", e);
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

    @Override
    public void afterPropertiesSet() throws Exception {
        //对正则预编译以提高性能
        this.pattern = Pattern.compile(formatRegex);
    }
}
