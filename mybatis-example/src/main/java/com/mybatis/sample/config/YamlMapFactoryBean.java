package com.mybatis.sample.config;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.YamlProcessor;

/**
 * Yaml파일의 내용을 profile을 적용해서 Map으로 전환
 * 
 * see YamlPropertiesFactoryBean
 * 
 * @author airlee
 * @since 4.1
 */
public class YamlMapFactoryBean extends YamlProcessor implements FactoryBean<Map<String, Object>>, InitializingBean {

    private boolean             singleton = true;

    private Map<String, Object> map;

    /**
     * Set if a singleton should be created, or a new object on each request
     * otherwise. Default is {@code true} (a singleton).
     */
    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    @Override
    public void afterPropertiesSet() {
        if (isSingleton()) {
            this.map = createProperties();
        }
    }

    @Override
    public Map<String, Object> getObject() {
        return (this.map != null ? this.map : createProperties());
    }

    @Override
    public Class<?> getObjectType() {
        return Properties.class;
    }

    /**
     * Template method that subclasses may override to construct the object
     * returned by this factory. The default implementation returns a properties
     * with the content of all resources.
     * <p>
     * Invoked lazily the first time {@link #getObject()} is invoked in case of
     * a shared singleton; else, on each {@link #getObject()} call.
     * 
     * @return the object returned by this factory
     * @see #process(MatchCallback) ()
     */
    protected Map<String, Object> createProperties() {
        // final Properties result =
        // CollectionFactory.createStringAdaptingProperties();
        final Map<String, Object> result = new java.util.HashMap<String, Object>();
        process(new MatchCallback() {
            @Override
            public void process(Properties properties, Map<String, Object> map) {
                result.putAll(map);
            }
        });
        return result;
    }

}
