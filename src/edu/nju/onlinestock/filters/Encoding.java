package edu.nju.onlinestock.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebFilter("/*")
public class Encoding implements Filter{
	
	/*
	 * default constructor
	 */
	public Encoding() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		if(request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpReq = (HttpServletRequest)request;
			HttpServletResponse httpResp = (HttpServletResponse)response;
			httpReq.setCharacterEncoding("UTF-8");
			httpResp.setContentType("text/html;charset=utf-8");
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		Filter.super.destroy();
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		Filter.super.init(filterConfig);
	}
	

}
