package com.starblet.srb.core.controller.admin;

import com.starblet.common.exception.Assert;
import com.starblet.common.exception.BusinessException;
import com.starblet.common.result.R;
import com.starblet.common.result.ResponseEnum;
import com.starblet.srb.core.pojo.entity.IntegralGrade;
import com.starblet.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author starblet
 * @create 2021-07-28 22:21
 */

//@CrossOrigin
@Slf4j
@Api(tags = "积分等级管理")
@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {

    @Resource
    private IntegralGradeService integralGradeService;

    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public R listAll(){
//        log.info("info info info info");
//        log.warn("warn warn warn warn");
//        log.error("error error error error ");

        List<IntegralGrade> list = integralGradeService.list();
        return R.success().data("list",list).message("获取列表成功");
    }

    @ApiOperation(value = "根据id删除积分等级", notes = "逻辑删除")
    @DeleteMapping("/remove/{id}")
    public R removeId(
            @ApiParam(value = "数据id",example = "1",required = true)
            @PathVariable Long id){
        boolean result = integralGradeService.removeById(id);
        if(result) {
            return R.success().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }
    }

    @PostMapping("/save")
    @ApiOperation("新增积分等级")
    public R save(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade){
//        if(integralGrade.getBorrowAmount() == null) {
//            throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
//        }
        // 使用断言来替代if，使得代码更加简洁
        Assert.notNull(integralGrade.getBorrowAmount(),ResponseEnum.BORROW_AMOUNT_NULL_ERROR);

        boolean save = integralGradeService.save(integralGrade);
        if(save) {
            return R.success().message("保存成功");
        }else{
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("根据id获取等级积分")
    @GetMapping("/get/{id}")
    public R getById(
            @ApiParam(value = "数据id",required = true,example = "1")
            @PathVariable Long id){
        IntegralGrade byId = integralGradeService.getById(id);
        if(byId != null) {
            return R.success().data("record",byId);
        }else {
            return R.error().message("数据不存在");
        }
    }


    @PutMapping("/update")
    @ApiOperation("更新积分等级")
    public R updateById(
            @ApiParam(value = "积分等级对象",required = true)
            @RequestBody IntegralGrade integralGrade) {

        boolean update = integralGradeService.updateById(integralGrade);
        if (update) {
            return R.success().message("更新成功");
        }else {
            return R.error().message("更新失败");
        }

    }


}
