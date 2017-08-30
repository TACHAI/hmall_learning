package com.hmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by @Author tachai on 2017/8/29.
 *
 * @Email 1206966083@qq.com
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
