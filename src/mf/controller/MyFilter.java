package mf.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig)
        throws ServletException
    {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;       
        String url = httpServletRequest.getRequestURI();
        System.out.println("url: "+url);
        if(url == null) {
            String contextPath = httpServletRequest.getContextPath();
            httpServletResponse.sendRedirect(contextPath + "/login.do");
            return;
        }
//        int len = url.split("/")[1].length()+2;
//        url = url.substring(len);
//        System.out.println("url: "+url);
        if (!url.endsWith(".do") && !url.startsWith("/assets"))
        {
            String contextPath = httpServletRequest.getContextPath();
            httpServletResponse.sendRedirect(contextPath + "/login.do");
            return;
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
        
    }
    
    @Override
    public void destroy()
    {
    }
}
