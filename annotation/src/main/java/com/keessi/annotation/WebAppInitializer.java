package com.keessi.annotation;

import ch.qos.logback.ext.spring.web.LogbackConfigListener;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.keessi.annotation.config.RootConfig;
import com.keessi.annotation.config.WebConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        //add listener
        servletContext.setInitParameter("logbackConfigLocation", "classpath:log/logback.xml");
        LogbackConfigListener listener = new LogbackConfigListener();
        servletContext.addListener(listener);

        //add filter
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        FilterRegistration.Dynamic encodingFilterRegistration = servletContext.addFilter("encodingFilter", encodingFilter);
        encodingFilterRegistration.addMappingForUrlPatterns(null, false, "/*");

        DelegatingFilterProxy shiroFilterProxy = new DelegatingFilterProxy();
        shiroFilterProxy.setTargetFilterLifecycle(true);
        FilterRegistration.Dynamic shiroFilterRegistration = servletContext.addFilter("shiroFilter", shiroFilterProxy);
        shiroFilterRegistration.setAsyncSupported(true);
        shiroFilterRegistration.addMappingForUrlPatterns(null, false, "/*");

        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistration.Dynamic webStatFilterRegistration = servletContext.addFilter("webStatFilter", webStatFilter);
        webStatFilterRegistration.setInitParameter("exclusions", "/static/*,/druid/*");
        webStatFilterRegistration.addMappingForUrlPatterns(null, false, "/*");


        //add servlet
        StatViewServlet servlet = new StatViewServlet();
        ServletRegistration.Dynamic registration = servletContext.addServlet("druidStatView", servlet);
        registration.setInitParameter("resetEnable", "true");
        registration.setInitParameter("loginUsername", "admin");
        registration.setInitParameter("loginPassword", "123456");
        registration.addMapping("/druid/*");
    }
}
