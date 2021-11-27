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
import com.perfecto.utilities.device.DeviceVitals;
import com.perfecto.utilities.device.Location;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
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

public class WazeTestCase2 {

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
			driver.resetApp();
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		}

		String JOB_NAME = System.getProperty("reportium-job-name", "Waze Demo");
		String JOB_NUMBER = System.getProperty("reportium-job-number", "1");

		// Reporting client. For more details, see
		// http://developers.perfectomobile.com/display/PD/Reporting
		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
				.withProject(new Project("Waze Demo", "1.0")).withJob(new Job(JOB_NAME, Integer.parseInt(JOB_NUMBER)))
				.withContextTags("TC2").withWebDriver(driver).build();
		reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

	}

	@Test
	public void test() throws InterruptedException {
		try {

			reportiumClient.testStart("WazeTestCase2",
					new TestContext.Builder().withTestExecutionTags("SetLocation", "LocationServices", "Functional")
							.withCustomFields(new CustomField("developer", "waze")).build());
			DeviceVitals.startDeviceVitals(driver,5);

			if (osname.equalsIgnoreCase("android")) {
				reportiumClient.stepStart("1. Open app");

				reportiumClient.stepStart("2. Set location to Tel Aviv 32.071557, 34.784212");
				Location.setDeviceLocationByCordinates(driver, "32.07155, 34.78421");
				Thread.sleep(5000);
				System.out.println(driver.getPageSource());
				driver.findElement(By.xpath(
						"//*[@resource-id='com.android.packageinstaller:id/permission_allow_button']|//*[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']"))
						.click();
				Thread.sleep(5000);
				driver.findElement(By.id("authWelcomeBottomButton")).click();
				driver.findElement(By.id("com.waze:id/btnScrollToBottom")).click();
				driver.findElement(By.id("AcceptButtonText")).click();
				reportiumClient.stepStart("3. Login with creds: ");
				//Thread.sleep(5000);
				driver.findElement(By.id("input")).sendKeys("cloudtestus");
				driver.findElement(By.id("emailNextButton")).click();
				driver.findElement(By.id("input")).sendKeys("Cloud1234");
				driver.findElement(By.id("next")).click();

				System.out.println(driver.getPageSource());
				//Wait for the alert to disappear
//				try{ocrTextSelect(driver,"Never");}
//				catch(Exception e){
//
//				}
				try{ocrTextSelect(driver,"Tap to");}
				catch(Exception e){

				}
				//Thread.sleep(15000);
				reportiumClient.stepStart("4. Ensure the map displayed in the right location");

				if (driver.findElement(By.xpath("//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]")).isDisplayed())
					reportiumClient.reportiumAssert("Login successful", true);
				else
					reportiumClient.reportiumAssert("Login successful", false);

				driver.findElement(By.xpath("//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]")).click();

				reportiumClient.stepStart("5. Go to device Settings and turn location off");
				//ApplicationUtilities.startApplicationByName(driver,"Settings");

				//runShell("settings put secure location_providers_allowed -gps");
				//runShell("settings put secure location_providers_mode 0");
				//ApplicationUtilities.startApplicationByName(driver,"Waze");
				//Thread.sleep(3000);
				locationServices("off");
				reportiumClient.stepStart("6. Restart Waze");
				//Thread.sleep(5000);
				ApplicationUtilities.closeApplicationByIdentifier(driver, "com.waze");
				Thread.sleep(5000);
				ApplicationUtilities.startApplicationByIdentifier(driver, "com.waze");
				//Thread.sleep(5000);

				reportiumClient.stepStart("7. Expected result; Map is not displayed - 'Waze requires acces to location' screen is displayed");
				if (driver.findElement(By.xpath("//*[@resource-id='com.waze:id/locationPermissionTitle']")).getAttribute("text").equalsIgnoreCase("Waze requires access to location"))
					reportiumClient.reportiumAssert("Location message displayed", true);
				else
					reportiumClient.reportiumAssert("Location message displayed", false);
			}
			reportiumClient.testStop(TestResultFactory.createSuccess());
		} catch (Exception e) {
			reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
			e.printStackTrace();
		}
	}

	@AfterTest
	public void tearDown() throws Exception {
		locationServices("on");
		DeviceVitals.stopDeviceVitals(driver);
		//ApplicationUtilities.startApplicationByName(driver,"Settings");

		//runShell("settings put secure location_providers_mode 3");
		//runShell("settings put secure location_providers_allowed +gps");
				//"settings put secure location_providers_mode 3");
		// Close driver
		driver.closeApp();

		// Quit Instance
		driver.quit();

	}

	public void runShell(String command) {
		Map<String, Object> params1 = new HashMap<>();
		params1.put("command", command);
		String result1 = (String) driver.executeScript("mobile:handset:shell", params1);
	}

	public void locationServices(String status) throws InterruptedException {
		ApplicationUtilities.closeApplicationByIdentifier(driver,"com.android.settings");
		ApplicationUtilities.startApplicationByIdentifier(driver,"com.android.settings");
		//Thread.sleep(3000);
		driver.findElementByXPath("//*[contains(@content-desc,\"Search\")]").click();
		driver.findElementByXPath("//*[@resource-id=\"com.android.settings.intelligence:id/search_src_text\"]").sendKeys("Location");
		driver.findElementByXPath("//*[@text=\"Biometrics and security\"]|//*[@resource-id=\"com.android.settings.intelligence:id/breadcrumb\" and @text=\"Location\"]").click();
		String status1 = driver.findElementByXPath("//*[contains(@resource-id,'switch_widget')]").getAttribute("text");
		String[] res = status1.split("[,]", 0);
		for(String myStr: res) {
			System.out.println(myStr);}
		try {
			if (!res[1].toLowerCase().equalsIgnoreCase(status))
				driver.findElementByXPath("//*[contains(@resource-id,'switch_widget')]").click();
		}
		catch(Exception ArrayIndexOutOfBoundsException)
		{
			if (!res[0].toLowerCase().equalsIgnoreCase(status))
				driver.findElementByXPath("//*[contains(@resource-id,'switch_widget')]").click();
		}
	}

	public static void ocrTextSelect(AppiumDriver driver, String textToBeFound)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("content", textToBeFound);
		params.put("timeout", "40");
		driver.executeScript("mobile:text:select", params);
	}
}


