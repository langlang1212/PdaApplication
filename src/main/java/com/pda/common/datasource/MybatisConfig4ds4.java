package com.pda.common.datasource;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @Classname MybatisConfig4ds3
 * @Description TODO
 * @Date 2022-11-03 20:26
 * @Created by AlanZhang
 */
@Configuration
@MapperScan(basePackages ="com.pda.api.mapper.ydhl", sqlSessionTemplateRef  = "ds4SqlSessionTemplate")
public class MybatisConfig4ds4 {

    @Bean(name="globalConfig4")
    public MybatisConfiguration globalConfiguration4() {
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setMapUnderscoreToCamelCase(true);
        return new MybatisConfiguration();
    }

    //ds3数据源
    @Bean("ds4SqlSessionFactory")
    public SqlSessionFactory ds3SqlSessionFactory(@Qualifier("ds4DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setConfiguration(globalConfiguration4());
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().
                getResources("classpath*:mapper/lis/*.xml"));
        return sqlSessionFactory.getObject();
    }

    //事务支持
    @Bean(name = "ds4TransactionManager")
    public DataSourceTransactionManager ds4TransactionManager(@Qualifier("ds4DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "ds4SqlSessionTemplate")
    public SqlSessionTemplate ds4SqlSessionTemplate(@Qualifier("ds4SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
