package com.starblet.srb.core.service;

import com.starblet.srb.core.pojo.dto.ExcelDictDTO;
import com.starblet.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author starblet
 * @since 2021-07-28
 */
public interface DictService extends IService<Dict> {

    void importData(InputStream inputStream);

    List<ExcelDictDTO> listDictData();


    List<Dict> listByParentId(Long parentId);
}
