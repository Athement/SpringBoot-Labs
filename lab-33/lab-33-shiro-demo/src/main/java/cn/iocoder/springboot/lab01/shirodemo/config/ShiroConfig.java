package cn.iocoder.springboot.lab01.shirodemo.config;

import cn.iocoder.springboot.lab01.shirodemo.filter.TestFilter01;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public Realm realm() {
        // 创建 SimpleAccountRealm 对象
        SimpleAccountRealm realm = new SimpleAccountRealm();
        // 添加两个用户。参数分别是 username、password、roles 。
        realm.addAccount("admin", "admin", "ADMIN");
        realm.addAccount("normal", "normal", "NORMAL");
        return realm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        // 创建 DefaultWebSecurityManager 对象
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置其使用的 Realm
        securityManager.setRealm(this.realm());
        //自定义web session管理器,默认session为容器管理
        securityManager.setSessionManager(new DefaultWebSessionManager());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        // 创建 ShiroFilterFactoryBean 对象，用于创建 ShiroFilter 过滤器
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        // 设置 SecurityManager
        filterFactoryBean.setSecurityManager(this.securityManager());
        // 设置 URL 们
        filterFactoryBean.setLoginUrl("/login"); // 登陆 URL
        filterFactoryBean.setSuccessUrl("/login_success"); // 登陆成功 URL
        filterFactoryBean.setUnauthorizedUrl("/unauthorized"); // 无权限 URL
        // 设置过滤器
        filterFactoryBean.setFilters(filterMap());
        // 设置 URL 的权限配置
        filterFactoryBean.setFilterChainDefinitionMap(this.filterChainMap());
        return filterFactoryBean;
    }
    //过滤器配置
    private Map<String, Filter> filterMap() {
        Map<String, Filter> filterMap = new LinkedHashMap<>(); // 注意要使用有序的 LinkedHashMap ，顺序匹配
        filterMap.put("testFilter01", new TestFilter01()); // 添加自定义过滤器
        return filterMap;
    }
    //url过滤器链配置
    private Map<String, String> filterChainMap() {
        Map<String, String> filterChainMap = new LinkedHashMap<>(); // 注意要使用有序的 LinkedHashMap ，顺序匹配
        filterChainMap.put("/test/echo", "anon"); // 允许匿名访问
        filterChainMap.put("/test/normal", "roles[NORMAL]"); // 需要 NORMAL 角色
        filterChainMap.put("/test/admin", "testFilter01,roles[ADMIN]"); // 需要 ADMIN 角色
        filterChainMap.put("/logout", "logout"); // 退出
        filterChainMap.put("/**", "authc"); // 默认剩余的 URL ，需要经过认证
        return filterChainMap;
    }

}
