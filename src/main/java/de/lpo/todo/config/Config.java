package de.lpo.todo.config;

import org.apache.commons.lang3.RegExUtils;

/** The supported configuration for this application. */
public record Config(
    Integer serverPort,
    String databaseDriver,
    String databaseHost,
    Integer databasePort,
    String databaseName,
    String databaseUsername,
    String databasePassword,
    String authBaseUri,
    String authRealm,
    String authClientId,
    String authSecret,
    String authCallbackUrl) {
  /**
   * Returns the shorthand for the JDBC connection URL.
   *
   * @return the JDBC connection URL
   */
  public String databaseUrl() {
    return "jdbc:%s://%s:%s/%s".formatted(databaseDriver, databaseHost, databasePort, databaseName);
  }

  /**
   * Returns a pretty-printed version of {@link #toString()}, with credentials removed from the
   * output.
   *
   * @return the display string
   */
  public String toDisplayString() {
    var result = toString();
    result = result.replace("[", "[\n    ");
    result = result.replace(", ", ",\n    ");
    result = result.replace("]", "\n]");
    result = RegExUtils.replacePattern(result, "databaseUsername=[^,]*", "databaseUsername=****");
    result = RegExUtils.replacePattern(result, "databasePassword=[^,]*", "databasePassword=****");
    result = RegExUtils.replacePattern(result, "authClientId=[^,]*", "authClientId=****");
    result = RegExUtils.replacePattern(result, "authSecret=[^,]*", "authSecret=****");
    return result;
  }
}
