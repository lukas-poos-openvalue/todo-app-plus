package de.lpo.todo.model.ids;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import org.jspecify.annotations.Nullable;
import org.sqids.Sqids;

/**
 * Base class for an ID. An ID can provide a {@link #hash()}, which is a unique, short
 * representation of the ID's {@link #value()}, which can be used externally.
 */
public abstract sealed class Id<T> permits IntId, StringId {
  /** Generator for short unique representations of the ID's value. */
  protected static final Sqids SQIDS =
      Sqids.builder()
          .minLength(5)
          .alphabet("k3G7QAe51FCsPW92uEOyq4Bg6Sp8YzVTmnU0liwDdHXLajZrfxNhobJIRcMvKt")
          .build();

  private @Nullable T value;
  private @Nullable String hash;

  /**
   * Creates an ID from a value, a hash, or both.
   *
   * @param value the raw value of the ID, or {@code null} if only the hash is known
   * @param hash the short, public representation of the ID, or {@code null} if only the value is
   *     known
   */
  protected Id(@Nullable final T value, @Nullable final String hash) {
    if (value == null && hash == null) {
      throw new IllegalArgumentException("ID cannot be constructed without value and hash!");
    }
    this.value = value;
    this.hash = hash;
  }

  /**
   * Returns the raw value of this ID, lazily decoding it from the hash if necessary.
   *
   * @return the raw value
   */
  public T value() {
    if (value == null) {
      // Lazily convert hash into the original value
      value = convertHashToValue(requireNonNull(hash));
    }
    return value;
  }

  /**
   * Returns the short, public representation of this ID, lazily encoding it from the value if
   * necessary.
   *
   * @return the hash
   */
  public String hash() {
    if (hash == null) {
      // Lazily convert the ID into the hash
      hash = convertValueToHash(requireNonNull(value));
    }
    return hash;
  }

  /**
   * Encodes the given value into its short, public hash representation.
   *
   * @param value the raw value to encode
   * @return the encoded hash
   */
  protected abstract String convertValueToHash(final T value);

  /**
   * Decodes the given hash back into its raw value.
   *
   * @param hash the hash to decode
   * @return the decoded value
   */
  protected abstract T convertHashToValue(final String hash);

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Id<?> otherId)) {
      return false;
    }

    // Use getters here, so that for comparisons, the value and the hash are set
    return Objects.equals(value(), otherId.value()) && Objects.equals(hash(), otherId.hash());
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, hash);
  }

  @Override
  public String toString() {
    return "Id{" + "value=" + value + ", hash='" + hash + '\'' + '}';
  }
}
