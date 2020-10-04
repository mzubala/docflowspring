package pl.com.bottega.docflowjee.hr.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.POST, "/employees", "/departments").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/employees/{id}").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/employees/{id}")
                .access("#oauth2.hasAnyScope(\"docflow\", \"catalog\", \"confirmations\") OR hasRole(\"ADMIN\")");
        super.configure(http);
    }
}
