package com.starblet.srb.core;

import com.starblet.srb.core.mapper.DictMapper;
import com.starblet.srb.core.pojo.entity.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author starblet
 * @create 2021-08-29 12:50
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTemplateTests {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private DictMapper dictMapper;

    @Test
    public void saveTest() {
        Dict dict = dictMapper.selectById(1);
        // 向redis中存储String类型的键值对，过期时间为5分钟
        System.out.println(dict);
        redisTemplate.opsForValue().set("dict",dict,5, TimeUnit.MINUTES);

        Dict dict1 = (Dict) redisTemplate.opsForValue().get("dict");
        System.out.println(dict1);
    }

    @Test
    public void getSpringVersion() {
        String version = SpringVersion.getVersion();
        String version1 = SpringBootVersion.getVersion();
        System.out.println(version);
        System.out.println(version1);
    }

}
