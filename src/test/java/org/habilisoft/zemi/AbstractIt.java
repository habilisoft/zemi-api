package org.habilisoft.zemi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.habilisoft.zemi.catalog.CatalogService;
import org.habilisoft.zemi.catalog.category.domain.Category;
import org.habilisoft.zemi.catalog.category.domain.CategoryRepository;
import org.habilisoft.zemi.catalog.product.application.RegisterProduct;
import org.habilisoft.zemi.catalog.product.domain.Product;
import org.habilisoft.zemi.catalog.product.domain.ProductRepository;
import org.habilisoft.zemi.sales.SalesService;
import org.habilisoft.zemi.sales.customer.domain.CustomerRepository;
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
import org.habilisoft.zemi.user.usecase.UserCommands;
import org.habilisoft.zemi.util.Commands;
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
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.habilisoft.zemi.AbstractIt.TenantConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(ClearDatabase.class)
@SpringBootTest(properties = "spring.flyway.clean-disabled=false")
@Import({TestcontainersConfiguration.class, TenantConfiguration.class, AbstractIt.Context.class})
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
    protected Context.CatalogContext catalogContext;
    @Autowired
    protected Context.UserContext userContext;
    @Autowired
    protected Context.SalesContext salesContext;

    @Autowired
    private TenantContext tenantContext;
    protected CatalogFixtures catalogFixtures = new CatalogFixtures();
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

    @TestConfiguration
    protected static class Context {
        @Component
        public static class CatalogContext {
            @Autowired
            public CatalogService catalogService;
            @Autowired
            public ProductRepository productRepository;
            @Autowired
            public CategoryRepository categoryRepository;
        }

        @Component
        protected static class UserContext {
            @Autowired
            private UserService userService;
            @Autowired
            private RoleRepository roleRepository;
        }

        @Component
        public static class SalesContext {
            @Autowired
            public SalesService salesService;
            @Autowired
            public CustomerRepository customerRepository;
        }
    }

    @BeforeEach
    void setUp() {
        tenantContext.set(tenant);
        UserCommands.CreateRole createAdminRole = Commands.Users.adminRole().build();
        RoleName roleName = createAdminRole.name();
        if (!userContext.roleRepository.existsById(roleName)) {
            userContext.userService.createRole(createAdminRole);
        }
        UserCommands.CreateUser createUser = Commands.Users.createUserBuilder()
                .username(username)
                .name("Test User")
                .roles(Set.of(roleName))
                .password("password")
                .build();
        userContext.userService.createUser(createUser);
    }

    protected String serializeRequestToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected Cookie jwtToken(Username username) throws Exception {
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


    @Accessors(fluent = true)
    protected class CatalogFixtures {
        @Getter(lazy = true)
        private final Product product1 = product();
        @Getter(lazy = true)
        private final Category category1 = category();

        protected Product product() {
            RegisterProduct createProduct = Commands.Catalog.registerProductBuilder()
                    .name("Pizza").build();

            return catalogContext.productRepository.findById(catalogContext.catalogService.registerProduct(createProduct))
                    .orElseThrow();
        }

        protected Category category() {
            Commands.Catalog.CreateCategoryBuilder createCategory = Commands.Catalog.createCategoryBuilder()
                    .name("Food");
            return catalogContext.categoryRepository.findById(catalogContext.catalogService.createCategory(createCategory.build()))
                    .orElseThrow();
        }
    }
}
