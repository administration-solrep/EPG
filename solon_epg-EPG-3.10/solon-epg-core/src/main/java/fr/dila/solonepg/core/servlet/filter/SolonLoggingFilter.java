package fr.dila.solonepg.core.servlet.filter;

import static org.jboss.seam.ScopeType.APPLICATION;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.MDC;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.web.Filter;
import org.jboss.seam.web.AbstractFilter;

@Scope(APPLICATION)
@Name("fr.dila.solonepg.core.servlet.filter.solonLoggingFilter")
@Install(precedence = Install.APPLICATION)
@BypassInterceptors
@Filter(around={"org.jboss.seam.web.exceptionFilter"})
public class SolonLoggingFilter extends AbstractFilter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			if (request instanceof HttpServletRequest) {
				HttpServletRequest httpServletRequest = (HttpServletRequest) request;
				MDC.put("X-Solon-URL", String.format("%s%s%s",
						httpServletRequest.getContextPath(),
						httpServletRequest.getRequestURI(),
						httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : ""));
			}
			chain.doFilter(request, response);
		} finally {
			MDC.remove("X-Solon-URL");
		}
	}

}
