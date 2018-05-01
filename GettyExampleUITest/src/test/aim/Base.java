package test.aim;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class Base {

	public static Properties prop;
	public static AndroidDriver<AndroidElement> driver;
	public static Logger logger;

	@BeforeTest
	public static void invokeDriver() throws IOException {
		logger = LogManager.getLogger(Base.class.getName());
		prop = new Properties();
		File f = new File("src");
		File app = new File(f, "Framework.properties");
		FileInputStream fis = new FileInputStream(app.getAbsolutePath());
		prop.load(fis);

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, prop.getProperty("DEVICE_NAME"));
		capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, prop.getProperty("APP_PACKAGE"));
		capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, prop.getProperty("APP_ACTIVITY"));
		driver = new AndroidDriver<>(new URL(prop.getProperty("SERVER_URL")), capabilities);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

	}

	@Test
	public static void isAppOpen() {

		if (driver.findElementByClassName("android.widget.TextView").getText().equals("GettyExample")) {
			logger.info("On Getty Example App");
		} else {
			logger.error("Getty Example App launch failed");
		}
	}

	@Test
	public static void isSearchButtonEnabled() {

		if (driver.findElementById("android:id/search_bar").getAttribute("enabled").equals("true")) {
			logger.info("Search button is enabled");
		} else {
			logger.error("Search button is not enabled");
		}
	}

	@Test
	public static void isSearchBarEnabled() {

		if (driver.findElementById("android:id/search_bar").getAttribute("enabled").equals("true")) {
			logger.info("Search bar is enabled");
		} else {
			logger.error("Search bar is not enabled");
		}
	}
	
	
	

@Test
	public static void areGettyImagesDisplayedAfterEnteringText() throws InterruptedException {

		
		//WebDriverWait wait = new WebDriverWait(driver, 10);

		// wait.until(ExpectedConditions.visibilityOf(driver.findElementByClassName("android.widget.LinearLayout")));
		Thread.sleep(2000);
		int count = driver.findElementsByClassName("android.widget.TextView").size();
		System.out.println("Count =" + count);
		if (count > 5) {
			logger.info("Getty Images search success");
		} else {
			logger.error("Unable to populate images with the search text provided");
		}

	}

	@Test
	public static void printGettyImagesDescriptionListedOnPage() throws InterruptedException {
		areGettyImagesDisplayedAfterEnteringText();
		List<AndroidElement> list = driver.findElementsByClassName("android.widget.TextView");
        System.out.println("Text of the Getty Images listed on the page :");
		for (AndroidElement e : list) {
			System.out.println(e.getAttribute("text"));
		}

	}

	@Test
	public static void clickTheFirstGettyImageAndValidateText() throws InterruptedException {
		logger.debug("clickTheFirstGettyImageAndValidateText");
		areGettyImagesDisplayedAfterEnteringText();
		String OriginalText=driver.findElementsByClassName("android.widget.TextView").get(1).getAttribute("text");
		
		System.out.println(OriginalText);
		driver.findElementsByClassName("android.widget.TextView").get(1).click();
		
		String textOnNewPage =driver.findElementsByClassName("android.widget.TextView").get(1).getAttribute("text");
		
		System.out.println(textOnNewPage);
        
		if(OriginalText.equals(textOnNewPage)){
			logger.info("Validated that the text on the new page matches with the Getty image that was clicked ");
		}
		else{
			logger.error("Text on the new page doesn't match with  the original getty image clicked");
		}

	}
	
	@Test(dataProvider="SearchProvider")
	public static void searchWithVariousSearchInputs(String searchText) throws InterruptedException {

		driver.findElementById("android:id/search_button").click();
		driver.findElementById("android:id/search_bar").sendKeys(searchText);
		// click on Search button on app KeyPad
		TouchAction a2 = new TouchAction(driver);
		a2.tap(1300, 2200).perform();

		WebDriverWait wait = new WebDriverWait(driver, 10);

		// wait.until(ExpectedConditions.visibilityOf(driver.findElementByClassName("android.widget.LinearLayout")));
		Thread.sleep(2000);
		int count = driver.findElementsByClassName("android.widget.TextView").size();
		System.out.println("Count =" + count);
		if (count > 5) {
			logger.info("Getty Images search success");
			
		} else {
			logger.error("Unable to populate images with the search text provided");
		}
		//clearing the text in the search box
		driver.findElementById("android:id/search_close_btn").click();
		
		

	}
	
	 @DataProvider(name="SearchProvider")
	    public Object[][] getDataFromDataprovider(){
	    return new Object[][] 
	    	{
	            { "Coffee" },
	            {"123" },
	            { ":)" }
	        };

	    }

	
	@AfterClass
	public void quitDriver(){
		driver.close();
		driver.quit();
		logger.traceExit();
	}
	
	
}
