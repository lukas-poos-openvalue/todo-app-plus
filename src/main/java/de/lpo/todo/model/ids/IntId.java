package de.lpo.todo.model.ids;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

/** Implementation of an ID having an integer value. */
public final class IntId extends Id<Integer> {

  /**
   * Creates an ID from its raw integer value.
   *
   * @param id the raw value
   * @return the created ID
   */
  public static Id<Integer> ofValue(final int id) {
    return new IntId(id, null);
  }

  /**
   * Creates an ID from its short, public hash, or {@code null} if the hash is blank.
   *
   * @param hash the hash to decode
   * @return the created ID, or {@code null} if {@code hash} is blank
   */
  @Nullable
  public static Id<Integer> ofHash(@Nullable final String hash) {
    if (StringUtils.isBlank(hash)) {
      return null;
    }
    return new IntId(null, hash);
  }

  private IntId(@Nullable final Integer value, @Nullable final String hash) {
    super(value, hash);
  }

  @Override
  protected String convertValueToHash(final Integer value) {
    return SQIDS.encode(List.of((long) value));
  }

  @Override
  protected Integer convertHashToValue(final String hash) {
    return SQIDS.decode(hash).getFirst().intValue();
  }
}
