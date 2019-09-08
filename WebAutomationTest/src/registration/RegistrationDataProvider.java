package registration;

import org.testng.annotations.DataProvider;

public class RegistrationDataProvider {

  @DataProvider (name="RegistrationDataProvider")
  public Object[][] dp() {
    return new Object[][] {
      new Object[] { "first", "last", "gmail@email.com", "username", "pwd", "" },			// blank field (last field)
      new Object[] { "first", "last", "email", "username", "pwd", "pwd" },					// bad email
      new Object[] { "first", "last", "gmail@email.com", "username", "pwd", "pwd1" },		// pw mismatch
      new Object[] { "@@", "last", "gmail@email.com", "username", "pwd", "pwd" },			// invalid first name
      new Object[] { "first", "@@", "gmail@email.com", "username", "pwd", "pwd" },			// invalid last name
      new Object[] { "first", "last", "thanos@marvel.com", "username", "pwd", "pwd" },		// existing email
      new Object[] { "first", "last", "gmail@email.com", "admin", "pwd", "pwd" },			// existing username
      new Object[] { "first", "last", "tmail@email.com", "tran", "123", "123" },			// all valid - account created
    };
  }
}
