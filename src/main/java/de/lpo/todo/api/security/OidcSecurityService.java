package de.lpo.todo.api.security;

import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import de.lpo.todo.config.ConfigService;
import de.lpo.todo.model.Role;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.pac4j.core.config.Config;
import org.pac4j.core.matching.matcher.DefaultMatchers;
import org.pac4j.core.matching.matcher.csrf.CsrfTokenGeneratorMatcher;
import org.pac4j.core.matching.matcher.csrf.DefaultCsrfTokenGenerator;
import org.pac4j.javalin.CallbackHandler;
import org.pac4j.javalin.LogoutHandler;
import org.pac4j.javalin.SecurityHandler;
import org.pac4j.oidc.client.KeycloakOidcClient;
import org.pac4j.oidc.config.KeycloakOidcConfiguration;

/**
 * Central service for managing web security. The service utilizes 'Pac4j' and OIDC for
 * authentication and authorization.
 */
public class OidcSecurityService {
  public static final String CLIENT_NAME = KeycloakOidcClient.class.getSimpleName();

  private final Config securityConfig;
  private final IdentityService identityService;

  /**
   * Builds the Pac4j security config for OIDC login against Keycloak.
   *
   * @param configService the service to read the OIDC settings from
   * @param identityService the service to update on successful login
   */
  public OidcSecurityService(
      final ConfigService configService, final IdentityService identityService) {
    final var config = configService.getConfig();

    // We fill the security config with a client / authorizers / matchers
    // Client: KeycloakOidcClient
    final var client =
        new KeycloakOidcClient(
                (KeycloakOidcConfiguration)
                    new KeycloakOidcConfiguration()
                        .setBaseUri(config.authBaseUri())
                        .setRealm(config.authRealm())
                        .setClientAuthenticationMethod(
                            ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .setClientId(config.authClientId())
                        .setSecret(config.authSecret()))
            .setCallbackUrl(config.authCallbackUrl());

    // Matcher: Modify CSRF Token matcher, so that the client can access the CSRF-Token in the
    // Cookies (This matcher performs a side effect and sets a response cookie, but not in an
    // accessible way)
    final var csrfTokenMatcher = new CsrfTokenGeneratorMatcher(new DefaultCsrfTokenGenerator());
    csrfTokenMatcher.setHttpOnly(false);

    this.securityConfig = new Config();
    this.securityConfig.addClient(client);
    this.securityConfig.addMatcher(DefaultMatchers.CSRF_TOKEN, csrfTokenMatcher);
    this.identityService = identityService;
  }

  /**
   * This handler that takes care of OIDC login callbacks and then updates the user database.
   *
   * @param redirectUrl the url to redirect to after the login callback has been taken care of
   * @return the login callback handler
   */
  public Handler handleOidcLoginCallback(final String redirectUrl) {
    final Handler loginCallback = new CallbackHandler(securityConfig, redirectUrl, true);
    return ctx -> {
      loginCallback.handle(ctx);
      final var identity = identityService.getFromContext(ctx);
      identityService.save(identity);
    };
  }

  /**
   * This handler takes care of OIDC logouts. This logout destroys the entire session.
   *
   * @param redirectUrl the url to redirect to after the logout has been taken care of
   * @return the logout handler
   */
  public Handler handleOidcLogout(final String redirectUrl) {
    final var handler = new LogoutHandler(securityConfig, redirectUrl);
    handler.centralLogout = true;
    handler.destroySession = true;
    return handler;
  }

  /**
   * This handler protects any endpoint that it is applied to. If the client is not authenticated
   * yet, the authentication flow is started automatically. After that, the default authenticators
   * and matchers are applied, so the client has to provide the CSRF token from the last response
   * cookie if the request is anything other than a GET-request.
   *
   * @return the OIDC security handler
   */
  public Handler handleOidcSecurity() {
    return new SecurityHandler(securityConfig, CLIENT_NAME);
  }

  /**
   * This handler protects assigned endpoints by looking at the authenticated user's role. Requests
   * from authenticated users that do not have the role are rejected.
   *
   * @return the admin role required handler
   */
  public Handler handleAdminRoleRequired() {
    return ctx -> {
      final var identity = identityService.getFromContext(ctx);
      if (identity.role() != Role.ADMIN) {
        throw new UnauthorizedResponse("Cannot access resource that is protected by admin role!");
      }
    };
  }
}
