package com.starblet.srb.core.controller.admin;

import com.alibaba.excel.EasyExcel;
import com.starblet.common.exception.BusinessException;
import com.starblet.common.result.R;
import com.starblet.common.result.ResponseEnum;
import com.starblet.srb.core.pojo.dto.ExcelDictDTO;
import com.starblet.srb.core.pojo.entity.Dict;
import com.starblet.srb.core.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.internal.util.xml.impl.Input;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author starblet
 * @create 2021-08-22 11:53
 */

@Api(tags = "数据字典管理")
@RestController
@RequestMapping("/admin/core/dict")
//@CrossOrigin
@Slf4j
public class AdminDictController {

    @Resource
    private DictService dictService;

    @ApiOperation("Excel批量导入数据字典")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "Excel文件",required = true)
            @RequestParam MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            dictService.importData(inputStream);
            return R.success().message("批量导入成功");
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }

    @ApiOperation("Excel数据的导出")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ExcelDictDTO.class).sheet("数据字典").doWrite(dictService.listDictData());
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.EXPORT_DATA_ERROR,e);
        }
    }

    @ApiOperation("根据上级id获取子节点数据列表")
    @GetMapping("listByParentId/{parentId}")
    public R listByParentId(
            @ApiParam(value = "上级节点id",required = true)
            @PathVariable Long parentId) {
        try {
            List<Dict> dictList = dictService.listByParentId(parentId);
            return R.success().data("dictList",dictList);
        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.QUERY_PARENT_INFO_ERROR,e);
        }
    }
    
}
