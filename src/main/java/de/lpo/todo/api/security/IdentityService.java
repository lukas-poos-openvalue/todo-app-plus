package de.lpo.todo.api.security;

import de.lpo.todo.model.Identity;
import de.lpo.todo.model.Role;
import de.lpo.todo.repository.IdentityRepo;
import io.javalin.http.Context;
import java.util.Map;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.oidc.profile.OidcProfile;

/** The service to manage {@link Identity} objects. */
public final class IdentityService {
  public static final String PROFILES_SESSION_ATTR = Pac4jConstants.USER_PROFILES;

  private final IdentityRepo identityRepo;

  /**
   * Creates a service backed by the given repository.
   *
   * @param identityRepo the repository to delegate to
   */
  public IdentityService(final IdentityRepo identityRepo) {
    this.identityRepo = identityRepo;
  }

  /**
   * Tries to find a current OIDC profile in the session attributes and provide a {@link Identity}
   * for the found profile.
   *
   * @param ctx the {@link Context} containing the session attributes
   * @return The value of the OIDC profile as a {@link Identity} or an empty response
   */
  public Identity getFromContext(final Context ctx) {
    // Check if the session contains OIDC profiles
    final Map<String, OidcProfile> userProfiles = ctx.sessionAttribute(PROFILES_SESSION_ATTR);
    if (userProfiles == null) {
      throw new IllegalArgumentException("Session does not provide OIDC profiles!");
    }

    // The profiles are keyed by the OIDC client; find the correct one
    final var profile = userProfiles.get(OidcSecurityService.CLIENT_NAME);
    if (profile == null) {
      throw new IllegalArgumentException(
          "Session does not provide OIDC profile for Keycloak client!");
    }

    // Map the profile data to an AuthUser
    return new Identity(
        profile.getId(),
        profile.getUsername(),
        profile.getEmail(),
        profile.getDisplayName(),
        Role.tryFirstValueOf(profile.getRoles()).orElseThrow());
  }

  /**
   * Saves the given identity, updating the record if it already exists.
   *
   * @param identity the {@link Identity} to store. The record is updated when it already exists.
   */
  public void save(final Identity identity) {
    identityRepo.save(identity);
  }
}
