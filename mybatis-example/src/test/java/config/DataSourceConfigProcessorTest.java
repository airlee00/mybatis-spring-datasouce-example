package config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.mybatis.sample.config.DataSourceVo;
import com.mybatis.sample.config.ProfileDocumentMatcher;
import com.mybatis.sample.config.YamlMapFactoryBean;

public class DataSourceConfigProcessorTest{
    /////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        new DataSourceConfigProcessorTest().test();
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
        System.out.println("===total map : " + map);
        System.out.println("===api.address.group1 : " + map.get("api.address.group1"));
        System.out.println("===spring.data.mongodb : " + map.get("spring.data.mongodb.database"));

        YamlPropertiesFactoryBean yaml2 = new YamlPropertiesFactoryBean();
        yaml2.setResources(resources);
        yaml2.setDocumentMatchers(new ProfileDocumentMatcher("spring.profiles"));
        // propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        Properties p = yaml2.getObject();
        System.out.println("===total properties : " + p);
        System.out.println("===spring.data.mongodb : " + p.getProperty("spring.data.mongodb.database"));
        
        
        Map datasourceMap = (Map) map.get("context.datasource");
        List inner = (List)datasourceMap.get("biz");
        System.out.println("-----------vo-----------");
        System.out.println(inner);
        
        Map fwkMap = (Map) datasourceMap.get("framework");
        System.out.println("=ffff===:" + fwkMap);

    }
    
}
