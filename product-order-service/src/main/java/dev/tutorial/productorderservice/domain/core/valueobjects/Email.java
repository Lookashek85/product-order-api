package dev.tutorial.productorderservice.domain.core.valueobjects;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Email(String value) {

  public Email {
    if (value == null || value.isEmpty()) {
      throw new IllegalArgumentException("Email cannot be empty");
    }
    if (!isValidEmail(value)) {
      throw new IllegalArgumentException("Invalid email format");
    }
  }

  private static boolean isValidEmail(String email) {
    String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = emailPattern.matcher(email);
    return matcher.find();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Email email = (Email) o;
    return Objects.equals(value, email.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
