package ua.org.code.configuration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container)
            throws ServletException {
        AnnotationConfigWebApplicationContext context
                = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);
        container.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher
                = container.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.addMapping("/api/*");
    }
}
