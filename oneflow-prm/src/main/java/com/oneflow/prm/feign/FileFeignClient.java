package com.oneflow.prm.feign;

import com.oneflow.prm.core.common.R;
import com.oneflow.prm.core.handler.FeignHandler;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件 Feign
 *
 * @Author:
 * @Date: 2022/3/25 11:21
 */
@FeignClient(name = "ecoflow-file", path = "/ecoflow/file", configuration = FeignHandler.class)
public interface FileFeignClient {

    /**
     * 上传文件到文件服务器
     *
     * @param file
     * @return
     */
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<String> upload(@RequestPart(value = "file") MultipartFile file);

    /**
     * 自定义文件名上传到文件服务器
     *
     * @param file
     * @return
     */
    @PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<String> upload(@RequestPart(value = "file") MultipartFile file, @RequestPart(value = "fileName") String fileName);

}
