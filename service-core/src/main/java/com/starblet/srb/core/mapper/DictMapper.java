package com.starblet.srb.core.mapper;

import com.starblet.srb.core.pojo.dto.ExcelDictDTO;
import com.starblet.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author starblet
 * @since 2021-07-28
 */
public interface DictMapper extends BaseMapper<Dict> {

    void insertBatch(List<ExcelDictDTO> list);
}
