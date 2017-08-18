package mobi.puut.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by Chaklader on 8/1/17.
 */
public class WebInitializer implements WebApplicationInitializer {


    public void onStartup(ServletContext container) throws ServletException {

        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();

        ctx.register(ApplicationConfiguration.class, ServiceConfig.class,
                DatabaseConfig.class);

        ctx.setServletContext(container);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(ctx));

        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher-servlet", new DispatcherServlet(ctx));

        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }
}




