package filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import security.JwtTokenReader;

public class NotificationFilter implements Filter {

	@Autowired
	private JwtTokenReader jwtTokenReader;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain)
			throws IOException, ServletException {

		System.out.println("Remote Host:" + request.getRemoteHost());
		System.out.println("Remote Address:" + request.getRemoteAddr());
		System.out.println("Response Before --------------  >> " + response.toString());

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Enumeration<String> headerNames = httpRequest.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				System.out.println("Header: " + httpRequest.getHeader(headerNames.nextElement()));
			}
		}

		filterchain.doFilter(request, response);
		System.out.println("Response After -------------- >> " + response.toString());
	}

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {
	}
}
