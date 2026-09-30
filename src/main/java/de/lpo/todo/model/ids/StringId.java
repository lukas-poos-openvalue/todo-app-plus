package de.lpo.todo.model.ids;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

/**
 * Implementation of an ID having a string value. The conversion between {@link #value()} and {@link
 * #hash()} is complexer than for numbers.
 */
public final class StringId extends Id<String> {

  /**
   * Creates an ID from its raw string value.
   *
   * @param id the raw value
   * @return the created ID
   */
  public static Id<String> ofValue(final String id) {
    return new StringId(id, null);
  }

  /**
   * Creates an ID from its short, public hash, or {@code null} if the hash is blank.
   *
   * @param hash the hash to decode
   * @return the created ID, or {@code null} if {@code hash} is blank
   */
  @Nullable
  public static Id<String> ofHash(@Nullable final String hash) {
    if (StringUtils.isBlank(hash)) {
      return null;
    }
    return new StringId(null, hash);
  }

  private StringId(@Nullable final String value, @Nullable final String hash) {
    super(value, hash);
  }

  @Override
  protected String convertValueToHash(final String value) {
    // Convert String into UTF-8 bytes
    final var valueBytes = value.getBytes(StandardCharsets.UTF_8);

    // Convert these bytes to longs
    final var bytesAsLongs = new ArrayList<Long>(valueBytes.length);
    for (final var valueByte : valueBytes) {
      bytesAsLongs.add((long) valueByte);
    }

    // Encode all longs into one hash
    return SQIDS.encode(bytesAsLongs);
  }

  @Override
  protected String convertHashToValue(final String hash) {
    // Decode the hash into multiple longs (each representing an UTF-8 character)
    final var bytesAsLongs = SQIDS.decode(hash);

    // Convert the List<Long> into byte[]
    final var valueBytes = new byte[bytesAsLongs.size()];
    for (int i = 0; i < bytesAsLongs.size(); i++) {
      valueBytes[i] = bytesAsLongs.get(i).byteValue();
    }

    // Recover the original String from the UTF-8 encoded byte array
    return new String(valueBytes, StandardCharsets.UTF_8);
  }
}
