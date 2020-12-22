package com.github.thestyleofme.datax.server.infra.autoconfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/14 16:02
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(DataxServerZookeeperInfo.class)
public class DataxServerAutoConfiguration {

    @Bean
    public DataxServerZookeeperInfo dataxServerZookeeperInfo() {
        return new DataxServerZookeeperInfo();
    }

    @Bean
    public DataxZookeeperRegister dataxZookeeperRegister(DataxServerZookeeperInfo dataxServerZookeeperInfo) {
        return new DataxZookeeperRegister(dataxServerZookeeperInfo);
    }

    @Bean
    @LoadBalanced
    public RestTemplate dataxRibbonRestTemplate(ClientHttpRequestFactory simpleClientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM,
                MediaType.TEXT_PLAIN));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(15000);
        factory.setConnectTimeout(15000);
        return factory;
    }

}
