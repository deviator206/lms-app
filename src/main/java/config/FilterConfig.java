package config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import filter.NotificationFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean notificationFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(notificationFilter());
		registration.addUrlPatterns("/search/leads");
		// registration.addInitParameter("paramName", "paramValue");
		registration.setName("notificationFilter");
		registration.setOrder(1);
		return registration;
	}

	@Bean(name = "notificationFilter")
	public NotificationFilter notificationFilter() {
		return new NotificationFilter();
	}

	/*
	 * @Bean public FilterRegistrationBean shallowEtagHeaderFilter() { }
	 */
}