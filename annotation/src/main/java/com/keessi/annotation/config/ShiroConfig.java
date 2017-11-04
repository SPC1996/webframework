package com.keessi.annotation.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public EhCacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:cache/ehcache.xml");
        return cacheManager;
    }

    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        return credentialsMatcher;
    }

    @Bean
    @Autowired
    public JdbcRealm jdbcRealm(HashedCredentialsMatcher credentialsMatcher, DataSource dataSource) {
        JdbcRealm realm = new JdbcRealm();

        realm.setPermissionsLookupEnabled(true);
        realm.setCredentialsMatcher(credentialsMatcher);

        realm.setDataSource(dataSource);
        realm.setAuthenticationQuery("SELECT password FROM f_staff WHERE username = ?");
        realm.setUserRolesQuery("SELECT R.name FROM f_staff_role SR " +
                "LEFT JOIN f_role R ON SR.role_id=R.id " +
                "LEFT JOIN f_staff S ON SR.staff_id=S.id " +
                "WHERE username = ?");
        realm.setPermissionsQuery("SELECT P.name FROM f_role_permission RP " +
                "LEFT JOIN f_role R ON RP.role_id=R.id " +
                "LEFT JOIN f_permission P ON RP.permission_id=P.id " +
                "WHERE R.name = ?");

        realm.setCachingEnabled(true);
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthenticationCacheName("authenticationCache");
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName("authorizationCache");

        return realm;
    }

    @Bean
    @Autowired
    public WebSecurityManager securityManager(JdbcRealm jdbcRealm, CacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jdbcRealm);
        securityManager.setCacheManager(cacheManager);
        return securityManager;
    }

    @Bean
    @Autowired
    public MethodInvokingFactoryBean invokingFactoryBean(WebSecurityManager securityManager) {
        MethodInvokingFactoryBean invokingFactoryBean = new MethodInvokingFactoryBean();
        invokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        invokingFactoryBean.setArguments(new Object[]{securityManager});
        return invokingFactoryBean;
    }

    @Bean("shiroFilter")
    @Autowired
    public ShiroFilterFactoryBean shiroFilter(WebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login");
        shiroFilter.setUnauthorizedUrl("/unauthorized");

        Map<String, String> filterChains = new HashMap<>();
        filterChains.put("/login", "authc");
        filterChains.put("/logout", "logout");
        filterChains.put("/unauthorized", "anon");
        filterChains.put("/static/**", "anon");
        filterChains.put("/druid/**", "anon");
        filterChains.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterChains);

        return shiroFilter;
    }

    @Bean
    public LifecycleBeanPostProcessor beanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
