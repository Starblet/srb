package com.starblet.srb.oss.controller.api;

import com.starblet.common.exception.BusinessException;
import com.starblet.common.result.R;
import com.starblet.common.result.ResponseEnum;
import com.starblet.srb.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author starblet
 * @create 2021-09-06 22:56
 */

@Api(tags = "阿里云文件管理")
//@CrossOrigin
@RestController
@RequestMapping("/api/oss/file")
public class FileController {

    @Resource
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public R upload(
            @ApiParam(value = "文件",required = true)
            @RequestParam("file") MultipartFile file,

            @ApiParam(value = "模块",required = true)
            @RequestParam("module") String module) {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String uploadUrl = fileService.upload(inputStream, module, originalFilename);

            return R.success().message("文件上传成功").data("url",uploadUrl);
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }

    @ApiOperation("根据OSS文件地址删除文件")
    @DeleteMapping("/delete")
    public R removeFile(
            @ApiParam(value = "要删除文件的路径",required = true)
            @RequestParam("url") String url) {

        try {
            fileService.removeFile(url);
            return R.success().message("文件删除成功");
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.FILE_DELETE_ERROR,e);
        }

    }
}
