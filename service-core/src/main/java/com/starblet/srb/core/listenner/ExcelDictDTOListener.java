package com.starblet.srb.core.listenner;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.starblet.srb.core.mapper.DictMapper;
import com.starblet.srb.core.pojo.dto.ExcelDictDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author starblet
 * @create 2021-08-22 11:46
 */

@Slf4j
@NoArgsConstructor
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {

    // 用于存储插入的数据
    private List<ExcelDictDTO> list = new ArrayList<>();

    // 用来定义每次存储的数据数量
    private static final int BATCH_COUNT = 5;

    private DictMapper dictMapper;

    // 由于该Listenner不能被Spring管理，所以只能用构造器注入的方式注入mapper，而不能自动注入
    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(ExcelDictDTO data, AnalysisContext analysisContext) {
        log.info("解析到一条记录：{}",data);
        // 每次解析到一条数据，就保存数据
        list.add(data);

        // 当存储的数据大于等于定义的数据数量，就执行mapper的批量保存方法，存储到数据库
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }

        // 调用mapper层方法，保存到数据库
    }

    /**
     * 数据全部读取结束后执行的方法
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 保存不满足BATCH_COUNT条件的剩余数据
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 存储到数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", list.size());
        // 执行批量保存，插入到数据库
        dictMapper.insertBatch(list);
        log.info("存储数据库成功！");
    }
}
