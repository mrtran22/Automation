package login;


import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;

public class LoginTest {	
	
  public WebDriver driver;
  
  
  @Test (priority=0, groups={"login"}, dataProvider="LoginProvider", dataProviderClass=LoginDataProvider.class)
  public void login(String email, String pwd) throws InterruptedException {
	  String stringArray[] = email.split("@");
	  String userName = stringArray[0];
	  System.out.println("\nlogin() started:");
	  System.out.println("=====================");
	  System.out.println("Thread ID: " + Thread.currentThread().getId());
	  System.out.println(email + " : " + pwd);
	  driver.findElement(By.xpath("//*[@class='login_text']")).sendKeys(email);
	  driver.findElement(By.xpath("//*[@class='login']/child::input[2]")).sendKeys(pwd);				// via xpath
	  String s = driver.findElement(By.xpath("//*[@name='pwd']")).getAttribute("value");				// get input field value
	  driver.findElement(By.id("login_submit")).click();
	  //driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);									// implicit wait, buggy
	  
	  String url = driver.getCurrentUrl();
	  if (url.contains("login=invalid")) {
		  Assert.assertTrue(driver.findElement(By.id("login_error")).isDisplayed(), "invalid user failed");
		  System.out.println("login error message: " + driver.findElement(By.id("login_error")).getText());
	  } else {
		  WebDriverWait wait = new WebDriverWait(driver, 3);
		  WebElement e = wait.until(ExpectedConditions.
				  visibilityOf(driver.findElement(By.xpath("//*[@id='home']/label"))));					// explicit wait
		  String user = e.getText();

		  if (user.equals("admin")) {
			  assertEquals(user, "admin");
			  assertTrue(driver.findElement(By.name("edit")).isDisplayed());
			  assertTrue(driver.findElement(By.name("delete")).isDisplayed());
			  assertTrue(driver.findElement(By.name("delete")).isDisplayed());	
			  System.out.println("USER: " + user);
			  System.out.println("admin buttons displayed");
			  search();
		  } else {
			  assertTrue(driver.findElement(By.name("update")).isDisplayed());
			  assertEquals(user, userName);
			  System.out.println("USER: " + user);
			  System.out.println("user buttons displayed");
			  
			  SoftAssert soft = new SoftAssert();												// soft Assert
			  soft.assertEquals(user, "foo", "mismatch");
			  //soft.assertAll();
			  //assertEquals(user, "foo", "mismatch");
			  search();
		  }
		  Thread.sleep(2000);  
		  driver.findElement(By.name("logout")).click();
	  }
  }
  
  @Test (priority=1)
  public void guestLogin() throws InterruptedException {
	  System.out.println("\nguestLogin() started:");
	  System.out.println("=====================");
	  System.out.println("Thread ID: " + Thread.currentThread().getId());
	  driver.findElement(By.id("guest_submit")).click();
	  String user = driver.findElement(By.xpath("//*[@id='home']/label")).getText();
	  assertEquals(user, "GUEST");
	  System.out.println("USER: " + user);
	  search();
  }
  
  @Test (enabled=false)
  public void search() throws InterruptedException {
	driver.findElement(By.name("value")).sendKeys("thanos");
	Thread.sleep(2000);
	driver.findElement(By.name("search")).click();
	String color = driver.findElement(By.xpath("//*[contains(text(), 'thanos')]/parent::tr")).getAttribute("bgcolor");
	assertEquals(driver.findElement(By.className("search_msg")).getText(), "Result highlighted");
	assertEquals(color, "#FFE37B", "highlight color mismatch");
	System.out.println("SEARCH button works. User found + assertion passed");
  }
  
  @BeforeTest
  @Parameters("browser")
  public void beforeTest(String browser) throws InterruptedException {
//	  System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Selenium\\"
//		+ "chromedriver_win32\\chromedriver.exe"); 
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
