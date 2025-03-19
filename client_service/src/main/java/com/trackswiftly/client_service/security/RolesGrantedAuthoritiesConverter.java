package com.trackswiftly.client_service.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

import com.trackswiftly.client_service.utils.TenantContext;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@NoArgsConstructor
@Slf4j
public class RolesGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>>{
    private String authorityPrefix = "";


    public RolesGrantedAuthoritiesConverter setAuthorityPrefix(String authorityPrefix) {
        Assert.notNull(authorityPrefix, "authorityPrefix cannot be null");
        this.authorityPrefix = authorityPrefix;
        return this;
    }

    
    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // Extract organization ID and set tenant context
        extractAndSetTenantId(source);
        
        // Extract roles from realm_access
        extractRealmRoles(source, authorities);
        
        return authorities;


    }



    private void extractAndSetTenantId(Jwt jwt) {
        Map<String, Object> organization = jwt.getClaim("organization");
        if (Objects.nonNull(organization) && !organization.isEmpty()) {
            // Get the first organization entry
            Map.Entry<String, Object> firstOrgEntry = organization.entrySet().iterator().next();
            
            @SuppressWarnings("unchecked")
            Map<String, Object> orgDetails = (Map<String, Object>) firstOrgEntry.getValue();
            
            String orgId = (String) orgDetails.get("id");
            if (Objects.nonNull(orgId)) {
                TenantContext.setTenantId(orgId);
            }
        }
    }



    private void extractRealmRoles(Jwt jwt, Set<GrantedAuthority> authorities) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (Objects.nonNull(realmAccess)) {
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            
            if (Objects.nonNull(roles)) {
                roles.stream()
                    .filter(role -> !role.equals("uma_authorization") && 
                                  !role.equals("offline_access") &&
                                  !role.startsWith("default-roles-"))
                    .map(role -> new SimpleGrantedAuthority(authorityPrefix + role))
                    .forEach(authorities::add);
            }
        }
    }

}