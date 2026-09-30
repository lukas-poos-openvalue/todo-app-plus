package de.lpo.todo.api.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.lpo.todo.config.ConfigService;
import de.lpo.todo.config.Config;
import org.junit.jupiter.api.Test;

class OidcSecurityServiceTest {

  private final ConfigService configService = mock(ConfigService.class);
  private final IdentityService identityService = mock(IdentityService.class);

  private static Config oneConfig() {
    return new Config(
        1234,
        "",
        "",
        2345,
        "",
        "",
        "",
        "authBaseUri",
        "authRealm",
        "authClientId",
        "authSecret",
        "authCallbackUrl");
  }

  @Test
  void should_get_handlers() {
    // GIVEN
    final var config = oneConfig();
    when(configService.getConfig()).thenReturn(config);

    // WHEN
    final OidcSecurityService oidcSecurityService =
        new OidcSecurityService(configService, identityService);

    // THEN
    assertThat(oidcSecurityService.handleOidcLoginCallback("")).isNotNull();
    assertThat(oidcSecurityService.handleOidcSecurity()).isNotNull();
    assertThat(oidcSecurityService.handleAdminRoleRequired()).isNotNull();
    assertThat(oidcSecurityService.handleOidcLogout("")).isNotNull();
  }
}
