package mobi.puut.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Chaklader on 8/1/17.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"mobi.puut.services"})
public class ServiceConfig {

}
