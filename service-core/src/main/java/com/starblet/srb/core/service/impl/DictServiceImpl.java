package com.starblet.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starblet.srb.core.listenner.ExcelDictDTOListener;
import com.starblet.srb.core.pojo.dto.ExcelDictDTO;
import com.starblet.srb.core.pojo.entity.Dict;
import com.starblet.srb.core.mapper.DictMapper;
import com.starblet.srb.core.service.DictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author starblet
 * @since 2021-07-28
 */
@Slf4j
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)  // 导入过程如果出现错误，则回滚
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelDictDTO.class, new ExcelDictDTOListener(baseMapper)).doReadAll();
        log.info("importData finished");
    }

    @Override
    public List<ExcelDictDTO> listDictData() {
        List<Dict> dictList = baseMapper.selectList(null);
        // 将Dict中的数据复制到ExcelDictDTO中
        ArrayList<ExcelDictDTO> ExcelDictDTOList = new ArrayList<>();
//        ExcelDictDTO excelDictDTO = new ExcelDictDTO();
        dictList.forEach(dict -> {
            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict,excelDictDTO);
            ExcelDictDTOList.add(excelDictDTO);
        });

        return ExcelDictDTOList;
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {

        List<Dict> dictList = null;

        // 先查询redis中是否存在数据列表
        try {
            // 因为是分级查询，所以数据最好也是分开使用不同的键来存储
            dictList = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
            if (dictList != null) {
                log.info("从redis中取数据字典");
                return dictList;
            }
        } catch (Exception e) {
            // 此处不抛异常，只是打印日志；因为redis只是使项目运行速度加快，不能影响整个项目的运行
            log.info("redis服务器异常：" + ExceptionUtils.getStackTrace(e));
        }

        // 如果redis中没有，则从数据库中查询数据列表

        log.info("从数据库中查询数据字典");
        // 封装查询条件
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id",parentId);
        // 查询列表
        dictList = baseMapper.selectList(dictQueryWrapper);

        dictList.forEach(dict -> {
            // 如果有子节点，则是非叶子节点，说明还有下一级
            boolean hasChildren = hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });

        // 将数据列表存入redis
        try {
            // 设置超时时间，便于和数据库同步
            redisTemplate.opsForValue().set("srb:core:dictList:" + parentId,dictList,5, TimeUnit.MINUTES);
            log.info("将数据字典存入redis");
        } catch (Exception e) {
            log.info("redis服务器异常：" + ExceptionUtils.getStackTrace(e));
        }

        return dictList;
    }

    /**
     * 判断该节点是否有子节点
     * @param id
     * @return
     */
    private boolean hasChildren(Long id){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<Dict>().eq("parent_id", id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
}
