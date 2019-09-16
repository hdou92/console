package com.zpk.console.web.controller;

import com.zpk.console.model.file.FileDto;
import com.zpk.console.util.HttpResponseUtils;
import com.zpk.console.web.config.FileProperties;
import com.zpk.zcommon.util.ZCollectionUtils;
import com.zpk.zcommon.util.ZConvertUtils;
import com.zpk.zcommon.util.ZFileUtils;
import com.zpk.zcommon.util.ZStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
//@RequestMapping(value = "/api/file", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequestMapping(value = "/api/file")
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    //    private static final String BASE_PATH = ClassUtils.getDefaultClassLoader().getResource("./").getPath() + "static/package/";
//    private static final String BASE_PATH = "e:/test/data/package/";

    @Autowired
    private FileProperties fileProperties;

    /**
     * 获取文件目录下的文件列表
     */
    @RequestMapping(value = "/files", method = RequestMethod.GET)
    public String[] getFiles(@ModelAttribute FileDto dto) {
        String filePath = getFilePath(dto);
        File[] files = ZFileUtils.getFiles(filePath);
        return ZCollectionUtils.isEmpty(files) ? null
                : Arrays.asList(files).stream().map(File::getName).collect(Collectors.toList()).toArray(new String[files.length]);
    }

    /**
     * 文件上传
     */
    //    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, FileDto dto) {
        if (file.isEmpty()) {
            return "文件为空";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        LOGGER.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        LOGGER.info("上传的后缀名为：" + suffixName);
        // 文件上传路径
        String filePath = getFilePath(dto);
        File dest = getFile(filePath + fileName);

        try {
            file.transferTo(dest);
            return "上传成功";
        } catch (IOException e) {
            LOGGER.error(ZStringUtils.exceptionToString(e));
        }
        return "上传失败";
    }

    /**
     * 文件上传
     */
    //    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestMapping(value = "/upload1", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "文件为空";
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        LOGGER.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        return "";
    }


    /**
     * 文件下载
     */
    @RequestMapping(value = "/download/{name}/{version}/{fileName:.+}", method = RequestMethod.GET)
    public void download(@PathVariable("name") String name,
                         @PathVariable("version") String version,
                         @PathVariable("fileName") String fileName,
                         HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpResponseUtils.writeFileBreakpoint(getFilePath(name, version) + fileName, request, response, 0, 10000);
        } catch (Exception e) {
            LOGGER.error(ZStringUtils.exceptionToString(e));
        }
    }


    /**
     * 获取文件路径
     */
    private String getFilePath(FileDto dto) {
        return getFilePath(dto.getName(), dto.getVersion());
    }

    /**
     * 获取文件路径
     */
    private String getFilePath(String name, String version) {
//        return new File(BASE_PATH + "/" + name + "/" + version + "/").getAbsolutePath() + "/";
        return new File(fileProperties.getBasePath() + "/" + name + "/" + version).getAbsolutePath() + "/";
    }

    private File getFile(String path) {
        // 解决中文问题，liunx 下中文路径，图片显示问题会出问题
        // fileName = UUID.randomUUID() + suffixName;  	//一般是使用uuid生成随机的文件名 + 截取的文件后缀。
        File dest = new File(path);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        return dest;
    }


}
