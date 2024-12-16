package TGRSanity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrderPlacing extends BaseClass{
	public static String ExpectedResult;
	public static String ActualResult;
	public static PrintWriter pw;
	public static  void ProductAddToCart(String productInfo, int Total,XSSFSheet sheetname, int k,XSSFRow row) throws InterruptedException
	{
		String productinformation[] = productInfo.split("#");
		String category = productinformation[0];
		String product = productinformation[1];
		Thread.sleep(2000);
		driver.findElement(By.xpath("//li[@class='Menu-Item'][1]")).click();
		Thread.sleep(3000);
		//String category = requiredCategory;
		try 
		{
		String categories[] = category.split("//") ;
		String Maincategory = categories[0];
		String Subcategory = categories[1];
		System.out.println(Maincategory);
		System.out.println(Subcategory);
		Actions actions = new Actions(driver);
		WebElement Level1category =driver.findElement(By.xpath("//span[contains(text(),'"+Maincategory+"')]"));
		actions.moveToElement(Level1category).perform();
		driver.findElement(By.xpath("//span[contains(text(),'"+Subcategory+"')]")).click();
		ProductSelection(product,Total,k,row);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			driver.findElement(By.xpath("//span[contains(text(),'"+category+"')]")).click();
			ProductSelection(product,Total,k,row);
		}
		catch(NoSuchElementException e2)
		{
		ExpectedResult = getCellVal(sheet2, k, 3);
		ActualResult="Invalid category or subcategory";
		String Result = "PASS";
		pw.println(row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+ExpectedResult+","+ActualResult+","+Result);
			System.out.println("Invalid category or subcategory");
			driver.navigate().refresh();
		}
	}
	public static void ProductSelection (String requiredproductname, int Totalcount,int k,XSSFRow row) throws InterruptedException
	{
		ExpectedResult = getCellVal(sheet2, k, 3);
		String productname = null;
		String quantity = null;
		int Quantity1 =0;
		String configtype = null;
		int outofstockquantity = 0;
		int warehouse = 0;
		int lessquantity = 0;
		try
		{
			String products[] = requiredproductname.split("!") ;
		    productname = products[0];
			quantity = products[1];
			Quantity1=Integer.parseInt(quantity); 
		    configtype =products[2]; 
			System.out.println(productname);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			String products[] = requiredproductname.split("!") ;
		    productname = products[0];
			quantity = products[1];
			Quantity1=Integer.parseInt(quantity); 
			System.out.println(productname);
		}
		Thread.sleep(2000);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,1500)");
		Thread.sleep(2000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@aria-label='Next page']")));
		WebElement button = driver.findElement(By.xpath("//a[@aria-label='Next page']"));
		String NextButton = button.getAttribute("class");
			while(!NextButton.equalsIgnoreCase("PaginationLink PaginationLink_isArrow PaginationLink_isArrowInActive"))
			{
				String cartquantity1 = driver.findElement(By.xpath("//span[@aria-label='Items in cart']")).getText();
				String requiredproduct = productname;
		List<WebElement> Products = driver.findElements(By.xpath("//p[@class='ProductCard-Name']"));
		for(int i=0;i<Products.size();i++)
		{		
			Thread.sleep(5000);
			String productname1 = Products.get(i).getText();
			System.out.println(productname1);
			if(productname1.equalsIgnoreCase(requiredproduct))
			{
				//String clicklnk = Keys.chord(Keys.CONTROL,Keys.CONTROL);
				System.out.println(productname1);
				Thread.sleep(2000);
				WebElement description=	Products.get(i);
				Actions actions = new Actions(driver);
		        Action action = actions.keyDown(Keys.CONTROL).click(description).keyUp(Keys.CONTROL).build();
		        action.perform();
				Set<String> windows=driver.getWindowHandles();
				Iterator<String> it =windows.iterator();
				String ParentId=it.next();
				while(it.hasNext())
				{
					driver.switchTo().window(it.next());
					Thread.sleep(5000);
					driver.navigate().refresh();
				//	jse.executeScript("window.scrollBy(0,5)");
					Thread.sleep(5000);
					try {
					driver.findElement(By.xpath("//div[@class='ProductConfigurableAttributes-ChoiceWrapper']")).click();
					Thread.sleep(2000);
						driver.findElement(By.xpath("//span[contains(text(),'"+configtype+"')]")).click();
						System.out.println("111");
						Thread.sleep(2000);
						driver.findElement(By.xpath("//button[@class='SideOverlay-CloseButtonButton']")).click();
						int requiredquantity = Quantity1;
						String PresentQuatity = driver.findElement(By.xpath("//input[@name='item_qty']")).getAttribute("value");
						int PresentNumberQuatity=Integer.parseInt(PresentQuatity); 
						if(requiredquantity==PresentNumberQuatity)
						{
							driver.findElement(By.xpath("//button[@class='Button AddToCart Button AddToCart_layout_grid ProductActions-AddToCart']")).click();
						Thread.sleep(3000);
							
						}
						else
						{
							for(int l=1;l<requiredquantity;l++)
							{
								try {
						driver.findElement(By.xpath("//button[@aria-label='Add']")).click();
								}
								catch(Exception e7)
								{
									driver.findElement(By.xpath("//button[@class='Button AddToCart Button AddToCart_layout_grid ProductActions-AddToCart']")).click();
									break;
								}
							}
							driver.findElement(By.xpath("//button[@class='Button AddToCart Button AddToCart_layout_grid ProductActions-AddToCart']")).click();
							Thread.sleep(3000);
							
						}
						
							
					}
					catch(Exception e1)
					{
							int requiredquantity = Quantity1;
							String PresentQuatity = driver.findElement(By.xpath("//input[@name='item_qty']")).getAttribute("value");
							int PresentNumberQuatity=Integer.parseInt(PresentQuatity); 
							if(requiredquantity==PresentNumberQuatity)
							{
								driver.findElement(By.xpath("//button[@class='Button AddToCart Button AddToCart_layout_grid ProductActions-AddToCart']")).click();
							Thread.sleep(3000);							
							}
							else
							{
								for(int l=1;l<requiredquantity;l++)
								{
									try {
							driver.findElement(By.xpath("//button[@aria-label='Add']")).click();
									}
									catch(Exception e7)
									{
										driver.findElement(By.xpath("//button[@class='Button AddToCart Button AddToCart_layout_grid ProductActions-AddToCart']")).click();
										break;
									}
								}	
								driver.findElement(By.xpath("//button[@class='Button AddToCart Button AddToCart_layout_grid ProductActions-AddToCart']")).click();
								Thread.sleep(3000);
						
					}
				}
					System.out.println("aaaaa");
					driver.close();
					driver.switchTo().window(ParentId);
					driver.navigate().refresh();
			}
				break;
		}
		}
			Thread.sleep(5000);
			String cartquantity2 = driver.findElement(By.xpath("//span[@aria-label='Items in cart']")).getText();
			System.out.println(cartquantity2);
			System.out.println("cartquantity"+cartquantity1);
			if(!cartquantity1.equalsIgnoreCase(cartquantity2))
			{
				break;			
			}
			else
			{
				//skip below 2 lines, added to handle insider popups
				driver.navigate().refresh();
		    	Thread.sleep(5000);	
				WebElement next = driver.findElement(By.xpath("//span[contains(text(),'Next')]"));
				Thread.sleep(2000);
				next.click();
				Thread.sleep(10000);
	}
		}
			}
	
	public static void productSplit(int m) throws InterruptedException
	{
		 
				XSSFRow row=sheet2.getRow(m);
		   String a= getCellVal(sheet2, m, 2);
			String a1[] = a.split(";");
			int numberofproducts =a1.length;
			System.out.println(numberofproducts);
			for(int i=0;i<numberofproducts;i++)
			{
				String ai = a1[i];
			ProductAddToCart(ai,numberofproducts,sheet2,m,row);
			}
	}
	public static void miniCart(int m) throws InterruptedException
	{
		driver.findElement(By.xpath("//button[@class='Header-MinicartButtonWrapper']")).click();
		driver.findElement(By.xpath("//div[@data-content='Apply discount']")).click();
		driver.findElement(By.xpath("//input[@id='couponCode']")).sendKeys(getCellVal(sheet2, m, 3));
		driver.findElement(By.xpath("//button[@class='CartCoupon-Button CartCoupon-Button_isHollow']")).click();
		Thread.sleep(6000);
			Thread.sleep(2000);
			driver.findElement(By.xpath("//a[@class=' CartOverlay-CartButton Button']")).click();
		}
	public static void cartPage(int m) throws InterruptedException
	{
		List<WebElement>selection = driver.findElements(By.xpath("//div[@class='FieldSelect-Clickable']"));
		selection.get(1).click();
		Thread.sleep(3000);
		List<WebElement> options = driver.findElements(By.xpath("//div[@class='FieldSelect-OptionsWrapper FieldSelect-OptionsWrapper_isExpanded']/li"));
		for(int k=0;k<options.size();k++)
		{
		String AvailableOptions = 	options.get(k).getText();
		String ExpectedOption = getCellVal(sheet2, m, 4);
		if(AvailableOptions.equalsIgnoreCase(ExpectedOption))
		{
			options.get(k).click();
		}
		}
		driver.findElement(By.xpath("//button[@class='CartPage-CheckoutButton Button']")).click();
	}
	public static void checkout(int m) throws InterruptedException
	{
		Thread.sleep(5000);
		if(getCellVal(sheet2, m, 5)==null)
		{
			
		}
		else
		{
		driver.findElement(By.xpath("//input[@id='points']")).sendKeys(getCellVal(sheet2, m, 5));
		driver.findElement(By.xpath("//button[contains(text(),'Apply')]")).click();
		Thread.sleep(3000);
		}
		driver.findElement(By.xpath("//button[@class='CheckoutAddressBook-Button']")).click();
		driver.findElement(By.xpath("//input[@name='firstname']")).sendKeys(getCellVal(sheet2, m, 6));
		driver.findElement(By.xpath("//input[@name='lastname']")).sendKeys(getCellVal(sheet2, m, 7));
		driver.findElement(By.xpath("//input[@name='telephone']")).sendKeys(getCellVal(sheet2, m, 8));
		driver.findElement(By.xpath("//input[@name='postcode']")).sendKeys(getCellVal(sheet2, m, 9));
		driver.findElement(By.xpath("//*[@id=\"address-country-id_wrapper\"]/div")).click();
		Thread.sleep(2000);
		List<WebElement> country = driver.findElements(By.xpath("//li[@class='FieldSelect-Option FieldSelect-Option_isExpanded FieldSelect-Option_isHovered_undefined']"));
		String requiredCountry = getCellVal(sheet2, m, 10);
		for(int i=0;i<country.size();i++)
		{
			String country1 = country.get(i).getText();
			if(country1.equalsIgnoreCase(requiredCountry))
			{
				country.get(i).click();
				break;
			}
		}
		Thread.sleep(2000);
		try {
		driver.findElement(By.xpath("//input[@name='street']")).sendKeys(getCellVal(sheet2, m, 11));
		}
		catch(Exception ab)
		{
			driver.findElement(By.xpath("//input[@name='street_0']")).sendKeys(getCellVal(sheet2, m, 11));
		}
		driver.findElement(By.xpath("//input[@name='city']")).sendKeys(getCellVal(sheet2, m, 12));
		driver.findElement(By.xpath("//input[@name='alternate_phone_number']")).sendKeys(getCellVal(sheet2, m, 13));
		String addresstype = getCellVal(sheet2, m, 14);
		
			driver.findElement(By.xpath("//input[@id='address-"+addresstype+"']")).click();
			String billing =getCellVal(sheet2, m, 15);
			if(billing=="Yes")
			{
				driver.findElement(By.xpath("//input[@name='default_billing']")).click();
			}
			String shipping =getCellVal(sheet2, m, 16);
			if(shipping=="Yes")
			{
				driver.findElement(By.xpath("//input[@name='default_shipping']")).click();
			}
		driver.findElement(By.xpath("//input[@name='default_address_concent']")).click();
		driver.findElement(By.xpath("//button[@class='Button MyAccount-Button']")).click(); 
	Thread.sleep(5000);
		JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,400)");
        driver.findElement(By.xpath("//button[@class='Button CheckoutShipping-ContinueButton']")).click();
        Thread.sleep(4000);
        List<WebElement> deliveryOptions = driver.findElements(By.xpath("//button[@class='CheckoutDeliveryOption-Button']"));
        String requiredDeliveryMethod = getCellVal(sheet2, m, 17);
        for(int k=0;k<deliveryOptions.size();k++)
        {
        	String actualdelivery = deliveryOptions.get(k).getText();
        	if(actualdelivery.contains(requiredDeliveryMethod))
        	{
        		deliveryOptions.get(k).click();
        		break;
        	}
        }
        jse.executeScript("window.scrollBy(0,400)");
        driver.findElement(By.xpath("//button[@class='Button CheckoutShipping-Button']")).click();
        Thread.sleep(4000);
	}
	public static void payment(int m, XSSFRow row) throws InterruptedException
	{
		List<WebElement> payments = driver.findElements(By.xpath("//li[@class='CheckoutPayment']"));
		String requiredPayment = getCellVal(sheet2, m, 18);
		for(int i=0;i<payments.size();i++)
		{
		String actualPayment = payments.get(i).getText();
		if(actualPayment.equalsIgnoreCase(requiredPayment))
		{
			payments.get(i).click();
	        WebElement element = payments.get(i);
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		}
		}
		Thread.sleep(2000);
		if(requiredPayment.equalsIgnoreCase("ACH (Automated Clearing House)"))
		{
			driver.findElement(By.xpath("//input[@name='Routing_Number']")).sendKeys("021000021");
			driver.findElement(By.xpath("//input[@name='Account_Number']")).sendKeys("1234567890");
			driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
			driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[@class='CheckoutSuccess-Heading']")));
		String ordeId = driver.findElement(By.xpath("//span[@class='OrderNumber']/span")).getText();
		System.out.println("Order placed Successfully Your Order ID is "+ordeId+"");
		String success = "Order Placed Successfully with the "+requiredPayment+" and the order id will be "+ordeId+"";
		pw.println(row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3)+","+row.getCell(4)+","+row.getCell(5)+","+row.getCell(6)+","+row.getCell(7)+","+row.getCell(8)+","+row.getCell(9)+","+row.getCell(10)+","+row.getCell(11)+","+row.getCell(12)+","+row.getCell(13)+","+row.getCell(14)+","+row.getCell(15)+","+row.getCell(16)+","+row.getCell(17)+","+row.getCell(18)+","+success);
			}
		if(requiredPayment.equalsIgnoreCase("Bitcoin and Altcoins via CoinGate"))
				{
			driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
			driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
			Thread.sleep(10000);		
			driver.findElement(By.xpath("//button[contains(text(),'Continue')]")).click();
			driver.findElement(By.xpath("//input[@name='email']")).sendKeys("vilas.hj@codilar.com");
			driver.findElement(By.xpath("//button[contains(text(),'Continue')]")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("//button[@class='MuiButton-root MuiButton-outlined MuiButton-outlinedSuccess MuiButton-sizeMedium MuiButton-outlinedSizeMedium MuiButtonBase-root  css-hfr1fg']")).click();
			Thread.sleep(4000);	
			driver.findElement(By.xpath("//button[contains(text(),'Return to Merchant Page')]")).click();
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[@class='CheckoutSuccess-Heading']")));
			String ordeId = driver.findElement(By.xpath("//span[@class='OrderNumber']/span")).getText();
			System.out.println("Order placed Successfully Your Order ID is "+ordeId+"");
			String success = "Order Placed Successfully with the "+requiredPayment+" and the order id will be "+ordeId+"";
			pw.println(row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3)+","+row.getCell(4)+","+row.getCell(5)+","+row.getCell(6)+","+row.getCell(7)+","+row.getCell(8)+","+row.getCell(9)+","+row.getCell(10)+","+row.getCell(11)+","+row.getCell(12)+","+row.getCell(13)+","+row.getCell(14)+","+row.getCell(15)+","+row.getCell(16)+","+row.getCell(17)+","+row.getCell(18)+","+success);
				}
		if(requiredPayment.equalsIgnoreCase("Pay with Bitcoin"))
		{
	driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
	driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
//	String success = "Order Placed Successfully with the "+requiredPayment+" and the order id will be "+ordeId+"";
//	pw.println(row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3)+","+row.getCell(4)+","+row.getCell(5)+","+row.getCell(6)+","+row.getCell(7)+","+row.getCell(8)+","+row.getCell(9)+","+row.getCell(10)+","+row.getCell(11)+","+row.getCell(12)+","+row.getCell(13)+","+row.getCell(14)+","+row.getCell(15)+","+row.getCell(16)+","+row.getCell(17)+","+row.getCell(18)+","+success);
	
		}
		if(requiredPayment.equalsIgnoreCase("Zelle £4.51"))
		{
	driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
	driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[@class='CheckoutSuccess-Heading']")));
	String ordeId = driver.findElement(By.xpath("//span[@class='OrderNumber']/span")).getText();
	System.out.println("Order placed Successfully Your Order ID is "+ordeId+"");
	String success = "Order Placed Successfully with the "+requiredPayment+" and the order id will be "+ordeId+"";
	pw.println(row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3)+","+row.getCell(4)+","+row.getCell(5)+","+row.getCell(6)+","+row.getCell(7)+","+row.getCell(8)+","+row.getCell(9)+","+row.getCell(10)+","+row.getCell(11)+","+row.getCell(12)+","+row.getCell(13)+","+row.getCell(14)+","+row.getCell(15)+","+row.getCell(16)+","+row.getCell(17)+","+row.getCell(18)+","+success);
		}
		if(requiredPayment.equalsIgnoreCase("SnapScan"))
		{
	driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
	driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
	
		}
		if(requiredPayment.equalsIgnoreCase("Pay by Credit / Debit Card - Zion"))
		{
	driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
	driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
	
	driver.findElement(By.xpath("//select[@name='paymentBrand']")).click();
	driver.findElement(By.xpath("//option[contains(text(),'Carte Bleue')]")).click();
	driver.findElement(By.xpath("//iframe[@name='card.number']")).sendKeys("");
	driver.findElement(By.xpath("//input[@placeholder='MM / YY']")).sendKeys("");
	driver.findElement(By.xpath("//input[@name='card.holder']")).sendKeys("");
	driver.findElement(By.xpath("//iframe[@name='card.cvv']")).sendKeys("");
	driver.findElement(By.xpath("//button[contains(text(),'Pay now')]")).click();
	
		}
		if(requiredPayment.equalsIgnoreCase("Bank Transfer Payment £4.51"))
		{
	driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
	driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[@class='CheckoutSuccess-Heading']")));
	String ordeId = driver.findElement(By.xpath("//span[@class='OrderNumber']/span")).getText();
	System.out.println("Order placed Successfully Your Order ID is "+ordeId+"");
	String success = "Order Placed Successfully with the "+requiredPayment+" and the order id will be "+ordeId+"";
	pw.println(row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3)+","+row.getCell(4)+","+row.getCell(5)+","+row.getCell(6)+","+row.getCell(7)+","+row.getCell(8)+","+row.getCell(9)+","+row.getCell(10)+","+row.getCell(11)+","+row.getCell(12)+","+row.getCell(13)+","+row.getCell(14)+","+row.getCell(15)+","+row.getCell(16)+","+row.getCell(17)+","+row.getCell(18)+","+success);
	
		}
		if(requiredPayment.equalsIgnoreCase("Fibonatix Credit / Debit Card £4.51"))
		{
	driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
	driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='cardNumber']")));
	driver.findElement(By.xpath("//input[@id='cardNumber']")).sendKeys("4100000000100009");
	driver.findElement(By.xpath("//input[@id='cardHolderName']")).sendKeys("Vilas");
	driver.findElement(By.xpath("//input[@id='expiryDate']")).sendKeys("08/25");
	driver.findElement(By.xpath("//input[@id='cvv']")).sendKeys("123");
	driver.findElement(By.xpath("//button[@class='MuiButtonBase-root MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeLarge MuiButton-containedSizeLarge MuiButton-colorPrimary MuiButton-fullWidth MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeLarge MuiButton-containedSizeLarge MuiButton-colorPrimary MuiButton-fullWidth css-6bwm04']")).click();	
	//WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[@class='CheckoutSuccess-Heading']")));
String ordeId = driver.findElement(By.xpath("//span[@class='OrderNumber']/span")).getText();
System.out.println("Order placed Successfully Your Order ID is "+ordeId+"");
String success = "Order Placed Successfully with the "+requiredPayment+" and the order id will be "+ordeId+"";
pw.println(row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3)+","+row.getCell(4)+","+row.getCell(5)+","+row.getCell(6)+","+row.getCell(7)+","+row.getCell(8)+","+row.getCell(9)+","+row.getCell(10)+","+row.getCell(11)+","+row.getCell(12)+","+row.getCell(13)+","+row.getCell(14)+","+row.getCell(15)+","+row.getCell(16)+","+row.getCell(17)+","+row.getCell(18)+","+success);
	
		}
		if(requiredPayment.equalsIgnoreCase("Pay by Cash £4.51"))
		{
	driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
	driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[@class='CheckoutSuccess-Heading']")));
	String ordeId = driver.findElement(By.xpath("//span[@class='OrderNumber']/span")).getText();
	System.out.println("Order placed Successfully Your Order ID is "+ordeId+"");
	String success = "Order Placed Successfully with the "+requiredPayment+" and the order id will be "+ordeId+"";
	pw.println(row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3)+","+row.getCell(4)+","+row.getCell(5)+","+row.getCell(6)+","+row.getCell(7)+","+row.getCell(8)+","+row.getCell(9)+","+row.getCell(10)+","+row.getCell(11)+","+row.getCell(12)+","+row.getCell(13)+","+row.getCell(14)+","+row.getCell(15)+","+row.getCell(16)+","+row.getCell(17)+","+row.getCell(18)+","+success);
		}
		if(requiredPayment.equalsIgnoreCase("Pay by Alternative means £4.51"))
		{
	driver.findElement(By.xpath("//input[@name='confirm_payment']")).click();
	driver.findElement(By.xpath("//button[@class='Button CheckoutPayment-Button']")).click();
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h3[@class='CheckoutSuccess-Heading']")));
	String ordeId = driver.findElement(By.xpath("//span[@class='OrderNumber']/span")).getText();
	String success = "Order Placed Successfully with the "+requiredPayment+" and the order id will be "+ordeId+"";
	pw.println(row.getCell(0)+","+row.getCell(1)+","+row.getCell(2)+","+row.getCell(3)+","+row.getCell(4)+","+row.getCell(5)+","+row.getCell(6)+","+row.getCell(7)+","+row.getCell(8)+","+row.getCell(9)+","+row.getCell(10)+","+row.getCell(11)+","+row.getCell(12)+","+row.getCell(13)+","+row.getCell(14)+","+row.getCell(15)+","+row.getCell(16)+","+row.getCell(17)+","+row.getCell(18)+","+success);
	System.out.println("Order placed Successfully Your Order ID is "+ordeId+"");
		}
	}
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		OrderPlacing obj = new OrderPlacing();
		obj.CreateInstance();
		obj.Delay2();
		sheet2= obj.Data_Provider2();
		String country = "United Kingdom";
		obj.countrySelection(country);
		Thread.sleep(2000);
		obj.signIn();
		FileOutputStream outputstream = new FileOutputStream("C:\\Users\\codilar\\eclipse-workspace\\TGR\\OrderPlacingOutput.csv",true);
		pw = new PrintWriter(outputstream);
		pw.println("TEST CASE NO,TEST CASE DESCRIPITION,PRODUCTINFO,COUPON CODE,DELIVERY INSURANCE,LOYALTY POINTS,FIRST NAME,LAST NAME,TELEPHONE,POST CODE,COUNTRY,STREET,CITY,ALTERNATE NUMBER,ADDRESS TYPE,BILLING,SHIPPING,DELIVERY METHOD,PAYMENT METHOD");
		for(int k=1;k<3;k++)
		{
			pw.println();
			XSSFRow row=sheet2.getRow(k);
		obj.productSplit(k);
		 obj.miniCart(k);
		   Thread.sleep(5000);
		   obj.cartPage(k);
		   obj.checkout(k);
		   obj.payment(k,row);
		}
	/* FileOutputStream outputstream = new FileOutputStream("C:\\Users\\codilar\\eclipse-workspace\\TGR\\SeedsmanAddToCartOutput.csv",true);
		 pw = new PrintWriter(outputstream);
		int rowCount=sheet2.getLastRowNum();
		System.out.println(rowCount);
	   pw.println("TEST CASE NO,TEST CASE DESCRIPITION,PRODUCTINFO,EXPECTED RESULT,ACTUAL RESULT,RESULT");*/
		 pw.close();		 
			System.out.println("Completed");

	}

}
