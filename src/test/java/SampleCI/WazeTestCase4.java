package SampleCI;

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

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

//import com.perfecto.utilities.uiObjects.UIObjectsUtilities;

public class WazeTestCase4 {

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
			capabilitiesMobile.setCapability("bundleId", "com.waze.iphone");
			capabilitiesMobile.setCapability("enableAppiumBehavior", true);
			driver = new IOSDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub/fast"),
					capabilitiesMobile);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		} else {
			DesiredCapabilities capabilitiesMobile = new DesiredCapabilities(browserName, "", Platform.ANY);
			browserName = "mobileOS";
			capabilitiesMobile.setCapability("securityToken", securityToken);
			capabilitiesMobile.setCapability("deviceName", deviceName);
			capabilitiesMobile.setCapability("app", "PUBLIC:Waze.apk");
			capabilitiesMobile.setCapability("appPackage", "com.waze");
			//capabilitiesMobile.setCapability("unicodeKeyboard", "true");
			capabilitiesMobile.setCapability("autoInstrument", "true");
			capabilitiesMobile.setCapability("enableAppiumBehavior", true);
			driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub/fast"),
					capabilitiesMobile);
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}

		String JOB_NAME = System.getProperty("reportium-job-name", "Waze Demo");
		String JOB_NUMBER = System.getProperty("reportium-job-number", "1");

		// Reporting client. For more details, see
		// http://developers.perfectomobile.com/display/PD/Reporting
		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
				.withProject(new Project("Waze Demo", "1.0")).withJob(new Job(JOB_NAME, Integer.parseInt(JOB_NUMBER)))
				.withContextTags("TC4").withWebDriver(driver).build();
		reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

	}

	@Test
	public void test() throws InterruptedException {
		try {

			reportiumClient.testStart("WazeTestCase4",
					new TestContext.Builder().withTestExecutionTags("iOS Execution","Parallel execution")
							.withCustomFields(new CustomField("developer", "waze")).build());


				reportiumClient.stepStart("1. Open app");
				//driver.resetApp();
				driver.context("NATIVE_APP");
				reportiumClient.stepStart("2. Set location to Kyiv(50.43971401, 30.50405502, 1000)");
				Location.setDeviceLocationByCordinates(driver, "50.43971, 30.5040");
				//Thread.sleep(5000);
			if(osname.equalsIgnoreCase("ios")) {
				//ApplicationUtilities.startApplicationByName(driver, "Waze");
				//Thread.sleep(5000);
				//ApplicationUtilities.cleanApplicationByName(driver, "Waze");
				//Thread.sleep(5000);
				ApplicationUtilities.closeApplicationByName(driver, "Waze");
				Thread.sleep(5000);
				ApplicationUtilities.startApplicationByName(driver, "Waze");
//				try {
//					UIObjectsUtilities.selectText(driver,"Stop",95);
//					UIObjectsUtilities.selectText(driver,"No thanks",95);
//					//driver.findElementByXPath("//*[@value=\"Stop\"]").click();
//					//driver.findElementByXPath("//*[@value=\"No thanks\"]").click();
//				} catch (Exception NoSuchElementException) {
//				}
				try {
					driver.findElementByXPath("//*[@value='Stop']").click();
				} catch (Exception NoSuchElementException) {
				}
			}
			if (osname.equalsIgnoreCase("android")) {
				driver.findElement(By.xpath(
						"//*[@resource-id='com.android.packageinstaller:id/permission_allow_button']|//*[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']"))
						.click();

				if (osname.equalsIgnoreCase("android")) {
					driver.findElementByXPath("//*[@resource-id=\"com.waze:id/authWelcomeTopButtonText\"]").click();
					driver.findElement(By.id("com.waze:id/btnScrollToBottom")).click();
					driver.findElement(By.id("AcceptButtonText")).click();
					driver.findElementByXPath("//*[@text=\"Sign up later\"]").click();
//				} else {
//					driver.findElement(By.id("authWelcomeBottomButton")).click();
//					driver.findElement(By.id("com.waze:id/btnScrollToBottom")).click();
//					driver.findElement(By.id("AcceptButtonText")).click();
//					reportiumClient.stepStart("3. Login with creds: ");
//					//Thread.sleep(5000);
//					driver.findElement(By.id("input")).sendKeys("cloudtestus");
//					driver.findElement(By.id("emailNextButton")).click();
//					driver.findElement(By.id("input")).sendKeys("Cloud1234");
//					driver.findElement(By.id("next")).click();

				}
				System.out.println(driver.getPageSource());
				//Wait for the alert to disappear
				try{ocrTextSelect(driver,"Tap to");}
				catch(Exception e){

				}

				reportiumClient.stepStart("6. Click on Search field");




//				if (device.equals("RFCN90KKCGZ"))
//					Thread.sleep(40000);
//				else
//					Thread.sleep(30000);

//				reportiumClient.stepStart("4. Open main menu  (Click My waze)");
//				if (driver.findElement(By.xpath("//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]")).isDisplayed())
//					reportiumClient.reportiumAssert("Login successful", true);
//				else
//					reportiumClient.reportiumAssert("Login successful", false);

			}
			try {
				driver.findElement(By.xpath("//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]|//XCUIElementTypeButton[@label=\"My Waze\"]")).click();
			}catch(Exception NoSuchElementException){}
				reportiumClient.stepStart("6. Click on Search field");

				driver.findElement(By.xpath(
						"//*[@resource-id=\"com.waze:id/recyclerView\"]//*[@resource-id=\"com.waze:id/searchBox\"]|(//*[@name='mainMenuSearchTextField'])[1]"))
						.click();

			try {
				driver.findElementByXPath("//*[@label=\"main menu icon blue\"]").click();
			}catch(Exception NoSuchElementException){
			}

				reportiumClient.stepStart("7. Type 'cafe Solomon'");
				driver.findElement(By.xpath("//*[@resource-id='com.waze:id/searchBox']|(//*[@name='mainMenuSearchTextField'])[1]")).sendKeys("cafe solomon");

				reportiumClient.stepStart("8. Choose the first search result");
				if(osname.equalsIgnoreCase("android"))
					driver.findElement(By.xpath("(//*[@resource-id='com.waze:id/cellTitle'])[1]|(//*[@resource-id='com.waze:id/imgAutoCompleteIcon'])[1]")).click();
				else
				{//UIObjectsUtilities.selectText(driver,"Lva Tolstoho Street",90);
					driver.findElement(By.xpath("//*[@label=\"Lva Tolstoho Street, Kyiv, Ukraine\"]")).click();
					}

			reportiumClient.stepStart("9. Click 'Go' button");
			try {
				driver.findElement(By.xpath("//*[@resource-id='com.waze:id/addressPreviewGoButtonText']|//*[@value=\"GO\"]")).click();

				reportiumClient.stepStart("10. Click 'Go now' button and be sure navigation was started");

					driver.findElement(By.xpath("//*@resource-id='com.waze:id/lblGo']|//XCUIElementTypeButton[@label=\"Go now\"]")).click();
				} catch (Exception NoSuchElementException) {
				}

			String initialDistance;
			try {
				if (osname.equalsIgnoreCase("android"))
					initialDistance = driver.findElement(By.xpath("//*[@resource-id=\"com.waze:id/lblDistanceToDestination\"]")).getAttribute("text");
				else
					initialDistance = driver.findElement(By.xpath("(//*[@name='eta distance'])[1]")).getAttribute("label");
				if (!initialDistance.isEmpty())
					reportiumClient.reportiumAssert("Map loaded", true);
				else
					reportiumClient.reportiumAssert("Map loaded", false);
			}catch(Exception NoSuchElementException){};

			reportiumClient.testStop(TestResultFactory.createSuccess());
		} catch (Exception e) {
			reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
			e.printStackTrace();
		}
	}

	@AfterTest
	public void tearDown() throws Exception {

		// Close driver
		if(osname.equalsIgnoreCase("android"))
			driver.closeApp();
		else
			ApplicationUtilities.closeApplicationByName(driver,"Waze");

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
		params.put("timeout", "120");
		driver.executeScript("mobile:text:select", params);
	}
}
