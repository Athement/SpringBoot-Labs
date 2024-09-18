package cn.iocoder.springboot.lab01.shirodemo.filter;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class TestFilter01 extends PathMatchingFilter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        logger.info("经过自定义过滤器01");
        return true;
    }
}
