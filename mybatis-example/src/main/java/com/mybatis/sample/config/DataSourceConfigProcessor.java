package com.mybatis.sample.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class DataSourceConfigProcessor implements BeanFactoryPostProcessor { //, BeanDefinitionRegistryPostProcessor, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ObjectMapper mapper = new ObjectMapper();
    {
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    /** yml 위치 */
    private Resource[] resources;

    public void setResources(Resource... resources) {
        this.resources = resources;
    }

    /**
     * application.yml의 모든 내용을 Map으로 변환
     * 
     * @return
     * @throws IOException
     */
    private Map<String, Object> convertYamlToMap(){// throws IOException {
        YamlMapFactoryBean yaml = new YamlMapFactoryBean();
        yaml.setResources(resources);
        yaml.setDocumentMatchers(new ProfileDocumentMatcher("spring.profiles"));
        Map<String, Object> map = yaml.getObject();

        if (logger.isInfoEnabled()) {
            logger.info("[fwk] datasource info={}", map);
        }
        return map;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("------------------------------------------------------2222222222222222");
        Map<String, Object> map = convertYamlToMap();
        // biz datasource setting
        List<Map> list = (ArrayList) map.get("context.datasource.biz");
        //System.out.println(inner);
        //List<DataSourceVo> list = mapper.convertValue(inner, List.class);
        //System.out.println(list);
        int index = 1;
        Map<String, SqlSessionTemplate> dataSourceMap = new HashMap<String, SqlSessionTemplate>();
        for (Map inner : list) {
            System.out.println("-------------------" + index);
            DataSourceVo vo  = mapper.convertValue(inner, DataSourceVo.class);
            String indexString = String.format("%02d", index);
            DataSource ds = dataSource(vo);
            beanFactory.registerSingleton("bizDataSource" + indexString, ds);
            PlatformTransactionManager tm = transactionManager(ds);
            
            beanFactory.registerSingleton("transactionManager" + indexString, tm);

            try {
                SqlSessionFactory sf = sqlSessionFactory(ds);
                SqlSessionTemplate st = sqlSessionTemplate(sf);
                beanFactory.registerSingleton("sqlSessionFactory" + indexString, sf);
                beanFactory.registerSingleton("sqlSessionTemplate" + indexString, st);
                dataSourceMap.put(vo.getName(), st);
                if(index == 1) {
                    beanFactory.registerSingleton("dataSource" , ds);
                    beanFactory.registerSingleton("sqlSessionTemplate", st);
                    beanFactory.registerSingleton("sqlSessionFactory" , sf);
                    beanFactory.registerSingleton("transactionManager" , tm);
                }
            } catch (Exception e) {
                logger.error("[fwk] create datasource error", e);
            }
            
            index++;
        }
         try {
            beanFactory.registerSingleton("sqlSessionTemplateHolder", sqlSessionTemplateHolder(dataSourceMap));
        } catch (Exception e) {
            logger.error("[fwk] create sqlSessionTemplateHolder error", e);
        }

    }

    private DataSource dataSource(DataSourceVo info) {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUsername(info.getUsername());
        dataSource.setUrl(info.getUrl());
        dataSource.setPassword(info.getPassword());
        return dataSource;
    }

    public DataSourceTransactionManager transactionManager(DataSource datasource) {
        return new DataSourceTransactionManager(datasource);
    }

    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] mapperResources = resolver.getResources("classpath:/mapper/**/*Mapper.xml");
        sessionFactory.setMapperLocations(mapperResources);
        sessionFactory.setConfigLocation(resolver.getResource("classpath:config/mybatis/mybatis-config.xml"));
        // sessionFactory.setTypeAliasesPackage("org.lanyonm.playground.domain");
        return sessionFactory.getObject();
    }

    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate t = new SqlSessionTemplate(sqlSessionFactory);
        return t;
    }

    /**
     * sqlSessionTemplateHolder
     * 
     * @throws Exception
     */
    public SqlSessionTemplateHolder sqlSessionTemplateHolder(Map<String, SqlSessionTemplate> dataSourceMap) throws Exception {
        SqlSessionTemplateHolder holder = new SqlSessionTemplateHolder();
        holder.setSqlSessionTemplate(dataSourceMap);
        return holder;
    }

    /////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        new DataSourceConfigProcessor().test();
    }

    public void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //
        // InputStream in =
        // DataSourceConfigProcessor.class.getResourceAsStream("/application.yml");
        // JsonNode node = mapper.readTree(in);
        // System.out.println(node.toString());
        // JsonNode inner = node.path("context.datasource.biz");
        // inner.forEach( n -> {
        // System.out.println(n.get("driverClassName"));
        // });
        // List<DataSourceVo> list = mapper.readValue(inner.toString(),
        // List.class);
        // System.out.println(list);
        Resource resources = new ClassPathResource("application.yml");
        YamlMapFactoryBean yaml = new YamlMapFactoryBean();
        yaml.setResources(resources);
        yaml.setDocumentMatchers(new ProfileDocumentMatcher("spring.profiles"));
        // propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        Map<String, Object> map = yaml.getObject();

        System.out.println("====:" + map);
        System.out.println("=ffff===:" + map.get("context.datasource.framework"));

        List inner = (ArrayList) map.get("context.datasource.biz");
        // final List<DataSourceVo> list = new java.util.ArrayList<>();
        // inner.forEach( item ->{
        // DataSourceVo vo = new DataSourceVo();
        // BeanUtils.copyProperties(vo, item);
        // list.add(vo);
        // });
        // System.out.println("-----------vo-----------");
        // System.out.println(list);

        ObjectMapper m = new ObjectMapper();
        List<DataSourceVo> list2 = m.convertValue(inner, List.class);
        System.out.println("-----------vo2-----------");
        System.out.println(list2);
    }
    
//
//    @Override
//    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//       // GenericBeanDefinition bd = new GenericBeanDefinition();
//        //bd.setBeanClass(beanClass);
//       // registry..getBeanDefinition("bizDataSource01").setPrimary(true);
//        System.out.println("------------------------------------------------------11111111111111");
////        String[] beanNames = beanFactory.getBeanDefinitionNames();//.getBeanNamesForType(DataSource.class);
////        for(String name  : beanNames) {
////            System.out.println(name);
////            if(name.endsWith("01")) {
////                GenericBeanDefinition gbd = new GenericBeanDefinition();
////                gbd.setBeanClass(SqlSessionTemplate.class);
////                beanFactory.set
////                BeanDefinition b =  beanFactory.getBeanDefinition(name);//.setPrimary(true);
////                b.setPrimary(true);
////            }
////            
////        }
//        
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        // TODO Auto-generated method stub
//        System.out.println("------------------------------------------------------333333333333333333333");
//        
//    }
}
