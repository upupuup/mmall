package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String upload(MultipartFile file, String path) {
        // 获取原始文件名
        String fileName = file.getOriginalFilename();
        // 获取扩展名,获取到.那一位
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始文件上传，上传文件的文件名：{}，上传的路径：{}，新文件名：{}", fileName, path, uploadFileName);

        // 创建文件
        File fileDir = new File(path);
        // 判断文件夹是否存在，不存在则新建一个文件夹
        if (!fileDir.exists()) {
            // 赋予写的权限
            fileDir.setWritable(true);
            // 创建目录，父目录不一定存在，也可以创建。mkdir只能创建一层文件
            fileDir.mkdirs();
        }

        File targetFile = new File(path, uploadFileName);
        try {
            // 文件已经上传成功
            file.transferTo(targetFile);
            // 将文件上传到ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            // 上传完成之后删除upload下的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常",e);
            e.printStackTrace();
            return null;
        }

        return targetFile.getName();
    }
}
