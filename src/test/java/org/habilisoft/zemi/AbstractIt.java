package org.habilisoft.zemi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.habilisoft.zemi.catalog.CatalogService;
import org.habilisoft.zemi.tenant.TenantId;
import org.habilisoft.zemi.tenant.TenantService;
import org.habilisoft.zemi.tenant.infra.TenantContext;
import org.habilisoft.zemi.user.UserService;
import org.habilisoft.zemi.user.Username;
import org.habilisoft.zemi.user.domain.RoleName;
import org.habilisoft.zemi.user.domain.RoleRepository;
import org.habilisoft.zemi.user.jwt.AuthConstants;
import org.habilisoft.zemi.user.jwt.JwtRequest;
import org.habilisoft.zemi.user.jwt.JwtService;
import org.habilisoft.zemi.user.usecase.Commands;
import org.habilisoft.zemi.util.Requests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.habilisoft.zemi.AbstractIt.TenantConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(ClearDatabase.class)
@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
@Import({TestcontainersConfiguration.class, TenantConfiguration.class})
public abstract class AbstractIt {
    @Autowired
    protected JdbcClient jdbcClient;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected CatalogService catalogService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TenantContext tenantContext;
    protected ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();

    protected final String tenantHeader = "TenantID";
    protected static final TenantId tenant = new TenantId("test");
    protected static final Username username = Username.of("test");

    @TestConfiguration
    public static class TenantConfiguration {
        @Autowired
        private TenantService tenantService;

        @Bean(name = "testTenantInitializer")
        public ApplicationRunner tenantInitializer() {
            return _ -> tenantService.register(tenant);
        }
    }

    @BeforeEach
    void setUp() {
        tenantContext.set(tenant);
        Commands.CreateRole createAdminRole = org.habilisoft.zemi.util.Commands.Users.adminRole().build();
        RoleName roleName = createAdminRole.name();
        if (!roleRepository.existsById(roleName)) {
            userService.createRole(createAdminRole);
        }
        Commands.CreateUser createUser = org.habilisoft.zemi.util.Commands.Users.createUserBuilder()
                .username(username)
                .name("Test User")
                .roles(Set.of(roleName))
                .password("password")
                .build();
        userService.createUser(createUser);
    }

    protected String serializeRequestToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected Cookie jwtToken() throws Exception {
        JwtRequest loginRequest = Requests.Users.loginBuilder()
                .username(username.value())
                .password("password")
                .build();
        MvcResult mvcResult = mockMvc.perform(post("/v1/authenticate")
                        .contentType("application/json")
                        .content(serializeRequestToJson(loginRequest))
                        .header(tenantHeader, tenant.name())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        return mvcResult.getResponse().getCookie(AuthConstants.COOKIE_ACCESS_TOKEN_NAME);
    }
}
