package servlets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SetupTest {

  private static final String VALID_AUTH = "a1b2c3d4-0000-0000-0000-000000000000";

  @Test
  public void validateHostPort_bothEmpty_isValid() {
    assertNull(Setup.validateHostPort("", ""));
  }

  @Test
  public void validateHostPort_bothProvided_isValid() {
    assertNull(Setup.validateHostPort("localhost", "3306"));
  }

  @Test
  public void validateHostPort_onlyHostProvided_isInvalid() {
    assertNotNull(Setup.validateHostPort("localhost", ""));
  }

  @Test
  public void validateHostPort_onlyPortProvided_isInvalid() {
    assertNotNull(Setup.validateHostPort("", "3306"));
  }

  @Test
  public void validateHostPort_bothNull_isValid() {
    assertNull(Setup.validateHostPort(null, null));
  }

  @Test
  public void validateHostPort_hostNullPortProvided_isInvalid() {
    assertNotNull(Setup.validateHostPort(null, "3306"));
  }

  @Test
  public void validateHostPort_hostProvidedPortNull_isInvalid() {
    assertNotNull(Setup.validateHostPort("localhost", null));
  }

  // A successful install deletes the auth file, so on an installed instance the token read comes
  // back empty. Treating that as a match would let anyone POST an empty dbauth and re-run the
  // schema, destroying every user and score.

  @Test
  public void isAuthorised_missingAuthFileAndEmptySuppliedToken_isRejected() {
    assertFalse(Setup.isAuthorised(null, ""));
  }

  @Test
  public void isAuthorised_emptyExpectedAndEmptySupplied_isRejected() {
    assertFalse(Setup.isAuthorised("", ""));
  }

  @Test
  public void isAuthorised_missingAuthFileAndNullSuppliedToken_isRejected() {
    assertFalse(Setup.isAuthorised(null, null));
  }

  @Test
  public void isAuthorised_blankSuppliedToken_isRejected() {
    assertFalse(Setup.isAuthorised(VALID_AUTH, "   "));
  }

  @Test
  public void isAuthorised_wrongToken_isRejected() {
    assertFalse(Setup.isAuthorised(VALID_AUTH, "00000000-0000-0000-0000-000000000000"));
  }

  @Test
  public void isAuthorised_correctToken_isAccepted() {
    assertTrue(Setup.isAuthorised(VALID_AUTH, VALID_AUTH));
  }

  @Test
  public void isAuthorised_correctTokenWithSurroundingWhitespace_isAccepted() {
    assertTrue(Setup.isAuthorised(VALID_AUTH, "  " + VALID_AUTH + "\n"));
  }
}
