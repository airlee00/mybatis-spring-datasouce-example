package com.mybatis.sample.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlProcessor.DocumentMatcher;
import org.springframework.beans.factory.config.YamlProcessor.MatchStatus;
import org.springframework.util.StringUtils;

/**
 * yml파일의 spring.profiles에 해당되는 properties를 취하기 위한 matcher임
 * 
 * profiles값이 없으면 ( -Dspring.profiles=dev ) 맨처음 정의된 값을 default 로 한다.
 * 
 * @author kclee01
 * @version 1.0
 * @since 2017.10.28 신규작성
 * @see YamlMapFactoryBean YamlProcessor
 */
public class ProfileDocumentMatcher implements DocumentMatcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private String[] envKeyName ;
    
    public ProfileDocumentMatcher(String... profile) {
        this.envKeyName = profile;
    }

    public void setEnvKeyName(String... envKeyName) {
        this.envKeyName = envKeyName;
    }

    @Override
    public MatchStatus matches(Properties properties) {
        if(logger.isDebugEnabled()){
            logger.debug("\n[fwk] properties={}", properties);
        }
        for(String key : envKeyName) {
            String propEnv = properties.getProperty(key);
            if(propEnv == null){
                return MatchStatus.ABSTAIN;
            } else {
                String systemEnv = System.getProperty(key);
                if(propEnv.equals(systemEnv)) {
                    return MatchStatus.FOUND;
                }
            }
        }
        return MatchStatus.NOT_FOUND;
    }

}
