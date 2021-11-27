package SampleCI;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.CustomField;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import com.perfecto.utilities.applications.ApplicationUtilities;
import com.perfecto.utilities.device.Location;
import com.perfecto.utilities.uiObjects.UIObjectsUtilities;
//import com.perfecto.utilities.uiObjects.UIObjectsUtilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class WazeTestCase1 {

	AppiumDriver driver;
	ReportiumClient reportiumClient;

	String device;
	String osname;

	@Parameters({ "deviceName", "OS" })
	@BeforeTest
	public void setUp(String deviceName, String OS) throws Exception {
		String browserName = null;
		String host = "demo.perfectomobile.com";
		String securityToken = "";
		osname = OS;
		device = deviceName;
		// driver for launching Android/iOS
		if (OS.equalsIgnoreCase("ios")) {
			DesiredCapabilities capabilitiesMobile = new DesiredCapabilities(browserName, "", Platform.ANY);
			browserName = "mobileOS";
			capabilitiesMobile.setCapability("securityToken", securityToken);
			capabilitiesMobile.setCapability("deviceName", deviceName);
			capabilitiesMobile.setCapability("bundleId", "com.mobily.mobilyapp");
			driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub/fast"),
					capabilitiesMobile);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		} else {
			DesiredCapabilities capabilitiesMobile = new DesiredCapabilities(browserName, "", Platform.ANY);
			browserName = "mobileOS";
			capabilitiesMobile.setCapability("securityToken", securityToken);
			capabilitiesMobile.setCapability("deviceName", deviceName);
			//capabilitiesMobile.setCapability("app", "PUBLIC:Waze.apk");
			capabilitiesMobile.setCapability("appPackage", "com.waze");
			//capabilitiesMobile.setCapability("unicodeKeyboard", "true");
			capabilitiesMobile.setCapability("autoInstrument", "true");
			capabilitiesMobile.setCapability("enableAppiumBehavior", true);
			driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub/fast"),
					capabilitiesMobile);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			driver.resetApp();
		}

		String JOB_NAME = System.getProperty("reportium-job-name", "Waze Demo");
		String JOB_NUMBER = System.getProperty("reportium-job-number", "1");

		// Reporting client. For more details, see
		// http://developers.perfectomobile.com/display/PD/Reporting
		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
				.withProject(new Project("Waze Demo", "1.0")).withJob(new Job(JOB_NAME, Integer.parseInt(JOB_NUMBER)))
				.withContextTags("TC1").withWebDriver(driver).build();
		reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

	}

	@Test
	public void test() throws InterruptedException {
		try {

			reportiumClient.testStart("WazeTestCase1",
					new TestContext.Builder().withTestExecutionTags("SetLocation","AppSwitching", "Functional")
							.withCustomFields(new CustomField("developer", "waze")).build());

			if (osname.equalsIgnoreCase("android")) {
				reportiumClient.stepStart("1. Open app");

				reportiumClient.stepStart("2. Set location to Kyiv(50.43971401, 30.50405502, 1000)");
				Location.setDeviceLocationByCordinates(driver, "50.43971, 30.5040");

				driver.resetApp();
				driver.findElement(By.xpath(
						"//*[@resource-id='com.android.packageinstaller:id/permission_allow_button']|//*[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']"))
						.click();

				if(device.equals("RFCN90KKCGZ")) {
					driver.findElementByXPath("//*[@resource-id=\"com.waze:id/authWelcomeTopButtonText\"]").click();
					driver.findElement(By.id("com.waze:id/btnScrollToBottom")).click();
					driver.findElement(By.id("AcceptButtonText")).click();
					driver.findElementByXPath("//*[@text=\"Sign up later\"]").click();
				}
				else {
					driver.findElement(By.id("authWelcomeBottomButton")).click();
					driver.findElement(By.id("com.waze:id/btnScrollToBottom")).click();
					driver.findElement(By.id("AcceptButtonText")).click();
					reportiumClient.stepStart("3. Login with creds: ");
					//Thread.sleep(5000);
					driver.findElement(By.id("input")).sendKeys("cloudtestus");
					driver.findElement(By.id("emailNextButton")).click();
					driver.findElement(By.id("input")).sendKeys("Cloud1234");
					driver.findElement(By.id("next")).click();

				}
				System.out.println(driver.getPageSource());
				try{ocrTextSelect(driver,"Never");}
				catch(Exception e){

				}
				reportiumClient.stepStart("4. Open main menu  (Click My waze)");
				if (driver.findElement(By.xpath("//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]")).isDisplayed())
					reportiumClient.reportiumAssert("Login successful", true);
				else
					reportiumClient.reportiumAssert("Login successful", false);

				System.out.println(driver.getPageSource());
					try{ocrTextSelect(driver,"Tap to");}
				catch(Exception e){

				}

				reportiumClient.stepStart("6. Click on Search field");

				new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]")));


				driver.findElement(By.xpath(
						"//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]"))
						.click();

				reportiumClient.stepStart("7. Type 'cafe Solomon'");
				driver.findElement(By.id("com.waze:id/searchBox")).click();
				driver.findElement(By.id("com.waze:id/searchBox")).sendKeys("cafe solomon");

				reportiumClient.stepStart("8. Choose the first search result");
				driver.findElement(By.xpath("(//*[@resource-id='com.waze:id/cellTitle'])[1]|(//*[@resource-id='com.waze:id/imgAutoCompleteIcon'])[1]")).click();

				reportiumClient.stepStart("9. Click 'Go' button");
				driver.findElement(By.id("com.waze:id/addressPreviewGoButtonText")).click();

				reportiumClient.stepStart("10. Click 'Go now' button and be sure navigation was started");
				try {
					driver.findElement(By.id("com.waze:id/lblGo")).click();
				} catch (Exception NoSuchElementException) {
				}

				reportiumClient.stepStart("11. Notice which route the app is offer");
				String initialDistance = driver.findElement(By.id("lblDistanceToDestination")).getAttribute("text");

				reportiumClient.stepStart("12. Open any phone browser");
				ApplicationUtilities.startApplicationByName(driver, "Chrome");

				reportiumClient.stepStart("13. Open deep link ");
				driver.get("http://waze.com/ul?ll=50,30&navigate=yes");


				reportiumClient.stepStart("14. Choose 'Open in the app'");
				reportiumClient.stepStart("15. Ensure the app offer another route to the point");
				String changedDistance = driver.findElement(By.id("lblDistanceToDestination")).getAttribute("text");
				if (!initialDistance.equalsIgnoreCase(changedDistance))
					reportiumClient.reportiumAssert("Another route offered", true);
				else
					reportiumClient.reportiumAssert("Another route offered", false);

				reportiumClient.stepStart("16. Open any phone browser");
				ApplicationUtilities.startApplicationByName(driver, "Chrome");


				reportiumClient.stepStart("17. Open deep link ");
				driver.get("http://waze.com/ul?a=stop_navigate");

				reportiumClient.stepStart("18. Choose 'Open in the app'");
				reportiumClient.stepStart("19. Ensure the navigation was stopped");

				if (driver.findElement(By.id("com.waze:id/leftMenuButtonText")).isDisplayed())
					reportiumClient.reportiumAssert("Navigation was stopped", true);
				else
					reportiumClient.reportiumAssert("Navigation was stopped", false);
			}

			reportiumClient.stepStart("20. Enter code ##@username");
			driver.findElementByXPath("//*[@resource-id='com.waze:id/searchBox']").click();

			clickKeyPad("18,18,77,49,47,33,46,42,29,41,33");
			clickKeyPad("66");
			reportiumClient.stepStart("21. Expected result - current user name should be desplayed in popup");
			if(driver.findElementByXPath("//*[@resource-id=\"com.waze:id/confirmText\"]").getAttribute("text").equalsIgnoreCase("cloudtestus") | driver.findElementByXPath("//*[@resource-id=\"com.waze:id/confirmText\"]").getAttribute("text").contains("world"))
				reportiumClient.reportiumAssert("Current user name displayed",true);
			else
				reportiumClient.reportiumAssert("Current user name displayed",false);

			driver.findElementByXPath("//*[@resource-id=\"com.waze:id/confirmSendText\"]").click();
			driver.findElementByXPath("//*[@content-desc=\"mapViewSearchBox\"]").clear();

			reportiumClient.stepStart("22. Enter code ##@il");
			driver.findElementByXPath("//*[@resource-id='com.waze:id/searchBox']").click();

			clickKeyPad("18,77,37,40");
			clickKeyPad("66");

			reportiumClient.stepStart("22. Check the popup \"Your location was changed\" appears");
			if(driver.findElementByXPath("//*[@resource-id=\"com.waze:id/confirmText\"]").getAttribute("text").equalsIgnoreCase("You're using Waze in a different country/region - restart Waze to continue"))
				reportiumClient.reportiumAssert("Location change popup appeared",true);
			else
				reportiumClient.reportiumAssert("Location change popup appeared",false);

			driver.findElementByXPath("//*[@resource-id=\"com.waze:id/confirmSendText\"]").click();

			reportiumClient.stepStart("23. Restart the app");
			reportiumClient.stepStart("22. Check the map is displayed");

			reportiumClient.testStop(TestResultFactory.createSuccess());
		} catch (Exception e) {
			reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
			e.printStackTrace();
		}
	}

	@AfterTest
	public void tearDown() throws Exception {

		// Close driver
		driver.closeApp();

		// Quit Instance
		driver.quit();

	}

	public void clickKeyPad(String str1) {
		String[] str=str1.split(",");
		for (int i=0;i<str.length;i++) {
			Map<String, Object> pars = new HashMap<>();
			pars.put("key", str[i]);
			pars.put("metastate", "0");
			String reStr = (String) driver.executeScript("mobile:key:event", pars);
		}
	}

	public static void ocrTextSelect(AppiumDriver driver, String textToBeFound)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("content", textToBeFound);
		params.put("timeout", "30");
		driver.executeScript("mobile:text:select", params);
	}
}
