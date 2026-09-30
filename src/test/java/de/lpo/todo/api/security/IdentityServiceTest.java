package de.lpo.todo.api.security;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import de.lpo.todo.model.Identity;
import de.lpo.todo.model.Role;
import de.lpo.todo.repository.IdentityRepo;
import io.javalin.http.Context;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.pac4j.oidc.profile.keycloak.KeycloakOidcProfile;

class IdentityServiceTest {

  private final IdentityRepo identityRepo = mock(IdentityRepo.class);
  private final IdentityService identityService = new IdentityService(identityRepo);

  @Test
  void should_read_from_context() {
    // GIVEN
    // - Profile
    final var userProfile = mock(KeycloakOidcProfile.class);
    when(userProfile.getId()).thenReturn("id");
    when(userProfile.getUsername()).thenReturn("name");
    when(userProfile.getEmail()).thenReturn("mail");
    when(userProfile.getDisplayName()).thenReturn("display");
    when(userProfile.getRoles()).thenReturn(Set.of("a", "b", Role.ADMIN.name(), "c"));

    // - Context
    final var ctx = mock(Context.class);
    final var userProfiles = Map.of(OidcSecurityService.CLIENT_NAME, userProfile);
    when(ctx.sessionAttribute(IdentityService.PROFILES_SESSION_ATTR)).thenReturn(userProfiles);

    // WHEN
    final var result = identityService.getFromContext(ctx);

    // THEN
    assertThat(result)
        .extracting(
            Identity::userId,
            Identity::userName,
            Identity::email,
            Identity::displayName,
            Identity::role)
        .containsExactly("id", "name", "mail", "display", Role.ADMIN);
  }

  @Test
  void should_save() {
    // GIVEN
    final var identity = new Identity("id", "name", "mail", "display", Role.USER);

    // WHEN
    identityService.save(identity);

    // THEN
    verify(identityRepo).save(identity);
  }
}
