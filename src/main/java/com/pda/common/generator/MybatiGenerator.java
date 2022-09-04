package com.pda.common.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @Classname MybatiGenerator
 * @Description TODO
 * @Date 2022-07-31 12:10
 * @Created by AlanZhang
 */
public class MybatiGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/pda-ydyh?serverTimezone=GMT%2B8&characterEncoding=utf8&useSSL=true", "root", "zl12121111")
                .globalConfig(builder -> {
                    builder.author("baomidou") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("D:\\java\\projects\\PdaApplication\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.pda.api") // 设置父包名
                            .moduleName("domain") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D:\\java\\projects\\PdaApplication\\src\\main\\resources\\mapper\\ydyh")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("order_type_dict") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
