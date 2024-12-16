package TGRSanity;

import java.time.Duration;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GitCodeTest {
	public static WebDriver driver;
	public static XSSFSheet sheet;
	public static void Delay(String a)
	{
		 WebElement guru99seleniumlink;
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			guru99seleniumlink= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'"+a+"')]")));
			
	}
	public static String getCellVal(XSSFSheet SheetName,int i,int j) {
		XSSFCell row = SheetName.getRow(i).getCell(j);
        return row.toString();	
	}
	public static void CreateInstance() throws InterruptedException
	{
		System.setProperty("webdriver.chrome.driver", "C:\\Webdriver\\chromedriver.exe");
		
		// ChromeOptions options = new ChromeOptions();
	        // Load the ad blocker extension
	      //  options.addExtensions(new File("C:\\Users\\codilar\\eclipse-workspace\\TGR\\Adblock.crx"));
	        driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.get("https://storeview-staging.seedsman.com/eu-en");
		driver.manage().window().maximize();	
		Thread.sleep(10000);
	}
	public static void countrySelection() throws InterruptedException
	{
		driver.findElement(By.xpath("//div[@class='SelectCountry-SearchDropDown']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[contains(text(),'United Kingdom')]")).click();
		driver.findElement(By.xpath("//input[@name='confirm_country']")).click();
		driver.findElement(By.xpath("//button[contains(text(),'Agree & Enter')]")).click();
	}
	public static String getCellVal2(XSSFSheet SheetName,int i,int j) {
		XSSFCell row = SheetName.getRow(i).getCell(j);
		try {
			 return row.toString();
		}
		catch(Exception NullPointerException)
		{
			return null;
		
		}	
	}

}
