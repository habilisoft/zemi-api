package org.habilisoft.zemi.tenant.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.habilisoft.zemi.tenant.TenantId;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
class TenantFilter implements Filter {
    private static final String TENANT_HEADER = "TenantID";
    private static final String HEALTH_CHECK_URL = "/healthcheck";
    private final ObjectMapper objectMapper;
    private final TenantContext tenantContext;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String tenantHeader = req.getHeader(TENANT_HEADER);

        if (!req.getRequestURI().contains(HEALTH_CHECK_URL)) {
            if (tenantHeader != null && !tenantHeader.isEmpty()) {
                tenantContext.set(TenantId.of(req.getHeader(TENANT_HEADER)));
            } else {
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("error", "No tenant header supplied");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(objectNode.toString());
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
