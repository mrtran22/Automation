package login;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {

  @DataProvider (name = "LoginProvider")
  public Object[][] getLoginData() {
    return new Object[][] {
      new Object[] { "invalidUsername", "123" },					// invalid user
      new Object[] { "admin@marvel.com", "123" },					// admin user
      new Object[] { "newguy@gmail.com", "123" },					// regular user
    };
  }
}
