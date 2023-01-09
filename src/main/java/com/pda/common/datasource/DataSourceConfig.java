package com.pda.common.datasource;

/**
 * @Classname DataSourceConfig
 * @Description TODO
 * @Date 2022-08-06 9:28
 * @Created by AlanZhang
 */
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 多数据源配置
 */
@Configuration
public class DataSourceConfig {

    //主数据源配置 ds1数据源
    @Primary
    @Bean(name = "ds1DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.ds1")
    public DataSourceProperties ds1DataSourceProperties() {
        return new DataSourceProperties();
    }

    //主数据源 ds1数据源
    @Primary
    @Bean(name = "ds1DataSource")
    public DataSource ds1DataSource(@Qualifier("ds1DataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    //第二个ds2数据源配置
    @Bean(name = "ds2DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.ds2")
    public DataSourceProperties ds2DataSourceProperties() {
        return new DataSourceProperties();
    }

    //第二个ds2数据源
    @Bean("ds2DataSource")
    public DataSource ds2DataSource(@Qualifier("ds2DataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    //第三个ds3数据源配置
    @Bean(name = "ds3DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.ds3")
    public DataSourceProperties ds3DataSourceProperties() {
        return new DataSourceProperties();
    }

    //第三个ds3数据源
    @Bean("ds3DataSource")
    public DataSource ds3DataSource(@Qualifier("ds3DataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    //第四个ds4数据源配置
    @Bean(name = "ds4DataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.ds4")
    public DataSourceProperties ds4DataSourceProperties() {
        return new DataSourceProperties();
    }

    //第四个ds4数据源
    @Bean("ds4DataSource")
    public DataSource ds4DataSource(@Qualifier("ds4DataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

}

