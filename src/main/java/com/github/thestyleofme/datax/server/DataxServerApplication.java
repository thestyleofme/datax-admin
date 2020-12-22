package com.github.thestyleofme.datax.server;

import com.github.thestyleofme.datax.ribbon.DataxRibbonConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/14 16:05
 * @since 1.0.0
 */
@SpringBootApplication(exclude = {
        FreeMarkerAutoConfiguration.class
})
@MapperScan({
        "com.github.thestyleofme.**.mapper"
})
@RibbonClients(value = {
        @RibbonClient(name = "DATAX", configuration = DataxRibbonConfiguration.class)
})
public class DataxServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataxServerApplication.class, args);
    }
}
