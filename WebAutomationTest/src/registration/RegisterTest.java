package registration;

import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class RegisterTest {
	
	public WebDriver driver;
	public String emailTemp;
	public String pwdTemp;
	public String username;
	
	@DataProvider (name="LoginDataProvider")
	public Object[][] getLoginData() {
		return new Object[][] {
			new Object[] {emailTemp, pwdTemp, username},
		};
	}
	
	
  @Test (priority=0, groups={"register"}, dataProvider="RegistrationDataProvider", dataProviderClass=RegistrationDataProvider.class )
  public void register(String first, String last, String email, String username, String pwd1, String pwd2) throws InterruptedException {
	  driver.findElement(By.name("firstName")).sendKeys(first);
	  driver.findElement(By.name("lastName")).sendKeys(last);
	  driver.findElement(By.xpath("//*[@id='select']/child::input[1]")).click();									// click both radio button
	  String radio = driver.findElement(By.xpath("//*[@id='select']/child::input[1]")).getAttribute("value");
	  //System.out.println(radio);
	  driver.findElement(By.xpath("//*[@id='select']/child::input[2]")).click();									// click both radio button
	  radio = driver.findElement(By.xpath("//*[@id='select']/child::input[2]")).getAttribute("value");
	  driver.findElement(By.xpath("//*[@id='register']/form/child::input[3]")).sendKeys(email);;
	  driver.findElement(By.name("username")).sendKeys(username);
	  driver.findElement(By.name("pwd1")).sendKeys(pwd1);
	  driver.findElement(By.name("pwd2")).sendKeys(pwd2);
	  Thread.sleep(3000);
	  //driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);												// works intermittently, buggy
	  driver.findElement(By.id("reg_submit")).click();
	  
	  System.out.println("Running register():   ");
	  System.out.println("======================");
	  System.out.println("Thread ID: " + Thread.currentThread().getId());
	  String url = driver.getCurrentUrl();
	  if (url.contains("register=success")) {																		// acct created successfully
		  assertTrue(driver.findElement(By.className("success")).isDisplayed());
		  this.emailTemp = email;																					// set new values for login dataprovider
		  this.pwdTemp = pwd1;
		  this.username = username;
		  System.out.println(driver.findElement(By.className("success")).getText());
		  System.out.println("User/email created: " +emailTemp + " | " + username + " | " + pwdTemp);
	  } else {
		  assertTrue(driver.findElement(By.className("fields")).isDisplayed());										// acct unsuccessful
		  System.out.println("Error msg: " + driver.findElement(By.className("fields")).getText());
		  System.out.println("Data provider: " + first + " | " + last + " | " + email + " | " + username 
				  + " | " + pwd1 + " | " + pwd2);
	  }
  }
  
  @Test (priority=1, groups={"function"}, dataProvider="LoginDataProvider")
  public void userEditAcct(String emailTemp, String pwdTemp, String username) throws InterruptedException {
	  driver.findElement(By.xpath("//*[@class='login_text']")).sendKeys(emailTemp);
	  driver.findElement(By.xpath("//*[@class='login']/child::input[2]")).sendKeys(pwdTemp);						// xpath getting child element[2]
	  driver.findElement(By.id("login_submit")).click();
	  
	  System.out.println("\nRunning userEditAcct():   ");
	  System.out.println("============================");
	  System.out.println("Thread ID: " + Thread.currentThread().getId());
	  assertTrue(driver.findElement(By.xpath("//*[contains(text(), '" + emailTemp + "')]")).isDisplayed());			// xpath page/table contains text
	  System.out.println("Email found: " + emailTemp);
	  System.out.println("username updating ...");
	  driver.findElement(By.name("password")).sendKeys(pwdTemp);
	  driver.findElement(By.name("oldUser")).sendKeys(username);
	  driver.findElement(By.name("newUser")).sendKeys("newUser");
	  driver.findElement(By.name("update")).click();
	  
	  assertTrue(driver.findElement(By.xpath("//*[contains(text(), '" + "newUser" + "')]")).isDisplayed());		
	  assertTrue(driver.findElement(By.className("update_success")).isDisplayed());
	  System.out.println("username updated success + new user found: newUser");
	  Thread.sleep(3000);
	  driver.findElement(By.name("logout")).click();    
  }
  
  @Test (priority=2, groups={"function"}, dataProvider="LoginDataProvider")										// same class dataprovider
  public void adminEditAcct(String emailTemp, String pwd, String username) throws InterruptedException {
	  driver.findElement(By.xpath("//*[@class='login_text']")).sendKeys("admin@marvel.com");
	  driver.findElement(By.xpath("//*[@class='login']/child::input[2]")).sendKeys("123");	
	  driver.findElement(By.id("login_submit")).click();
	  
	  System.out.println("\nRunning adminEditAcct():   ");
	  System.out.println("=============================");
	  System.out.println("Thread ID: " + Thread.currentThread().getId());
	  driver.findElement(By.xpath("//*[@id='data']/tbody/tr[last()]/td[1]/input")).click();
	  driver.findElement(By.name("edit")).click();
	  
	  String etFirst = "testFirst";
	  String etLast = "testLast";
	  String etEmail = "test@email.com";
	  String etUser = "testUser";
	  String etPwd = "test123";
	  
	  String etArray[] = {etFirst, etLast, etEmail, etUser, etPwd};
	  driver.findElement(By.name("first")).sendKeys(etFirst);
	  driver.findElement(By.name("last")).sendKeys(etLast);
	  driver.findElement(By.name("email")).sendKeys(etEmail);
	  driver.findElement(By.name("newUser")).sendKeys(etUser);
	  driver.findElement(By.name("password")).sendKeys(etPwd);
	  driver.findElement(By.name("update")).click();
	  
	  int cnt = 3;
	  for(int i=0;i<etArray.length;i++) {
		  String at = driver.findElement(By.xpath("//*[@id='data']/tbody/tr[last()]/td["+cnt+"]")).getText();		// get <tr> last child
		  assertEquals(at, etArray[i]);
		  System.out.println("User updated (actual vs expect): " + at + " | " + etArray[i]);		  
		  cnt++;
	  }
	  Thread.sleep(3000);
	  driver.findElement(By.className("logout")).click();
	  this.emailTemp = etArray[2];																					// set new email, username, pwd for @dataprovider
	  this.pwdTemp = etArray[3];
	  this.username = etArray[4];
  }

  @Test (priority=3, groups={"function"}, dataProvider="LoginDataProvider")											// same class dataprovider
  public void adminDeleteAcct(String emailTemp, String pwd, String username) throws InterruptedException {			// String 'pwd' not used
	  driver.findElement(By.xpath("//*[@class='login_text']")).sendKeys("admin@marvel.com");
	  driver.findElement(By.xpath("//*[@class='login']/child::input[2]")).sendKeys("123");	
	  driver.findElement(By.id("login_submit")).click();
	  
	  System.out.println("\nRunning adminDeleteAcct():   ");
	  System.out.println("===============================");
	  System.out.println("Thread ID: " + Thread.currentThread().getId());
	  for(int i=0; i<2; i++) {
		  assertTrue(driver.findElement(By.xpath("//*[contains(text(), '" + emailTemp + "')]")).isDisplayed());			// page contains text
		  System.out.println("Email found: " + emailTemp);
		  driver.findElement(By.xpath("//*[contains(text(), '" + emailTemp + "')]/parent::tr/td/input")).click();		// get parent tag <tr> of variable 'emailTemp'
		  driver.findElement(By.name("delete")).click();
		  Thread.sleep(3000);
		  
		  if (i==0) {
			  driver.findElement(By.name("no")).click();
			  String msg = driver.findElement(By.xpath("//*[@class='invalid']")).getText();
			  assertEquals(msg, "Delete cancelled");
			  assertTrue(driver.findElement(By.xpath("//*[contains(text(), '" + emailTemp + "')]")).isDisplayed());		// page contains text
			  System.out.println("message: " + msg);
		  } else {
			  driver.findElement(By.name("yes")).click();
			  String msg = driver.findElement(By.xpath("//*[@class='invalid']")).getText();
			  assertEquals(msg, "Record deleted");
			  assertTrue(driver.findElement(By.xpath("//*[not(contains(text(), '" + emailTemp + "'))]")).isDisplayed());	// page/table does not contains text		  
			  System.out.println("message: " + msg);
		  }
	  }
  }
  
  
  @BeforeTest
  @Parameters("browser")
  public void beforeTest(String browser) throws InterruptedException {
//	  System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Selenium\\"
//		  		+ "chromedriver_win32\\chromedriver.exe"); 
//	  driver = new ChromeDriver();
	  
	  if (browser.equalsIgnoreCase("Firefox")) {
		  System.setProperty("webdriver.gecko.driver", "C:\\Program Files\\Selenium\\"
		  		+ "geckodriver-v0.22.0-win64\\geckodriver.exe"); 
		  driver = new FirefoxDriver();
	  } else if (browser.equalsIgnoreCase("Chrome")) {
		  System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Selenium\\"
		  		+ "chromedriver_win32\\chromedriver.exe"); 
		  driver = new ChromeDriver();
	  }
	  driver.get("http://www.half-and-half.com/accounts/index.php");  
	  
	  JavascriptExecutor js = (JavascriptExecutor)driver;
	  js.executeScript("alert('Test Case execution started ...');");
	  Thread.sleep(3000);
	  driver.switchTo().alert().accept();
  }

  @AfterClass
  public void afterClass() {
	  driver.close();
  }
  
}
