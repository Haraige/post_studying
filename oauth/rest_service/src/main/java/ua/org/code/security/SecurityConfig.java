package ua.org.code.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.RS256;
import static org.springframework.security.oauth2.jose.jws.SignatureAlgorithm.RS512;

@KeycloakConfiguration
@EnableGlobalMethodSecurity(securedEnabled = true)
@PropertySource("classpath:application.properties")
public class SecurityConfig extends
        KeycloakWebSecurityConfigurerAdapter {

    private final String clientId;
    private final String clientName;
    private final String clientSecret;
    private final String issuerUri;
    private final String redirectUri;
    private final String authorizationUri;
    private final String tokenUri;
    private final String userInfoUri;
    private final String userNameAttribute;
    private final String clientAuthenticationMethod;
    private final String authorizationGrantType;

    private final String jwksUri;

    public SecurityConfig(@Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.client-name}") String clientName,
            @Value("${keycloak.client-secret}") String clientSecret,
            @Value("${keycloak.issuer-uri}") String issuerUri,
            @Value("${keycloak.redirect-uri}") String redirectUri,
            @Value("${keycloak.authorization-uri}") String authorizationUri,
            @Value("${keycloak.token-uri}") String tokenUri,
            @Value("${keycloak.user-info-uri}") String userInfoUri,
            @Value("${keycloak.user-name-attribute}") String userNameAttribute,
            @Value("${keycloak.client-authentication-method}") String clientAuthenticationMethod,
            @Value("${keycloak.authorization-grant-type}") String authorizationGrantType,
            @Value("${keycloak.jwks-uri}") String jwksUri) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientSecret = clientSecret;
        this.issuerUri = issuerUri;
        this.redirectUri = redirectUri;
        this.authorizationUri = authorizationUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
        this.userNameAttribute = userNameAttribute;
        this.clientAuthenticationMethod = clientAuthenticationMethod;
        this.authorizationGrantType = authorizationGrantType;
        this.jwksUri = jwksUri;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(keycloakAuthenticationProvider());
    }

    @Override
    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(getKeycloakClientRegistration());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .antMatchers("/sso/login*").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and().oauth2ResourceServer().jwt().decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwksUri)
                .jwsAlgorithm(RS256).build();
    }

    private class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            final Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            return ((List<String>)realmAccess.get("roles")).stream()
                    .map(roleName -> "ROLE_" + roleName) // prefix to map to a Spring Security "role"
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtConverter;
    }

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakConfigResolver() {
            private KeycloakDeployment keycloakDeployment;

            @Override
            public KeycloakDeployment resolve(HttpFacade.Request request) {
                if (keycloakDeployment != null) {
                    return keycloakDeployment;
                }

                String path = "/keycloak.json";
                InputStream configInputStream = getClass().getResourceAsStream(path);

                if (configInputStream == null) {
                    throw new RuntimeException("Could not load Keycloak deployment info: " + path);
                } else {
                    keycloakDeployment = KeycloakDeploymentBuilder.build(configInputStream);
                }

                return keycloakDeployment;
            }
        };
    }

    private ClientRegistration getKeycloakClientRegistration() {
        return ClientRegistration.withRegistrationId("keycloak")
                .clientId(clientId)
                .clientName(clientName)
                .clientSecret(clientSecret)
                .issuerUri(issuerUri)
                .redirectUri(redirectUri)
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .userInfoUri(userInfoUri)
                .clientAuthenticationMethod(new ClientAuthenticationMethod(clientAuthenticationMethod))
                .authorizationGrantType(new AuthorizationGrantType(authorizationGrantType))
                .userNameAttributeName(userNameAttribute)
                .build();
    }
}
