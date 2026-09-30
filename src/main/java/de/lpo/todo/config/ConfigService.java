package de.lpo.todo.config;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Service for providing the profile and the configuration that this app is started with. */
public final class ConfigService {
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final Profile profile =
      io.avaje.config.Config.getEnum(Profile.class, "env.profile", Profile.PROD);
  private final Config config =
      new Config(
          io.avaje.config.Config.getInt("server.port"),
          io.avaje.config.Config.get("db.driver"),
          io.avaje.config.Config.get("db.host"),
          io.avaje.config.Config.getInt("db.port"),
          io.avaje.config.Config.get("db.name"),
          io.avaje.config.Config.get("db.username"),
          io.avaje.config.Config.get("db.password"),
          io.avaje.config.Config.get("auth.baseUri"),
          io.avaje.config.Config.get("auth.realm"),
          io.avaje.config.Config.get("auth.clientId"),
          io.avaje.config.Config.get("auth.secret"),
          io.avaje.config.Config.get("auth.callbackUrl"));

  /** Reads the profile and configuration from the environment and logs them. */
  public ConfigService() {
    LOG.info("Loaded the following configuration:");
    LOG.info("Profile: {}", profile);
    LOG.info("Config: {}", config.toDisplayString());
  }

  /**
   * Returns the profile that the application has been started with.
   *
   * @return the profile
   */
  public Profile getProfile() {
    return profile;
  }

  /**
   * Returns the config that the application has been started with.
   *
   * @return the config
   */
  public Config getConfig() {
    return config;
  }
}
