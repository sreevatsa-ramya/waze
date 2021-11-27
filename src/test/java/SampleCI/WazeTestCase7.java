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
import com.perfecto.utilities.device.Files;
import com.perfecto.utilities.device.Functions;
import com.perfecto.utilities.device.ImageInjection;

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

public class WazeTestCase7 {

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
			//capabilitiesMobile.setCapability("sensorInstrument", "true");
			capabilitiesMobile.setCapability("enableAppiumBehavior", true);
			driver = new AndroidDriver(new URL("https://" + host + "/nexperience/perfectomobile/wd/hub/fast"),
					capabilitiesMobile);
			driver.resetApp();
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}

		String JOB_NAME = System.getProperty("reportium-job-name", "Waze Demo");
		String JOB_NUMBER = System.getProperty("reportium-job-number", "1");

		// Reporting client. For more details, see
		// http://developers.perfectomobile.com/display/PD/Reporting
		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
				.withProject(new Project("Waze Demo", "1.0")).withJob(new Job(JOB_NAME, Integer.parseInt(JOB_NUMBER)))
				.withContextTags("TC7").withWebDriver(driver).build();
		reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

	}

	@Test
	public void test() throws InterruptedException {
		try {

			reportiumClient.testStart("WazeTestCase7",
					new TestContext.Builder().withTestExecutionTags("File Transfer","Device File")
							.withCustomFields(new CustomField("developer", "waze")).build());

			//File transfer to device
			//Public: ProfilePic.jpeg
			Files.putFile(driver,"phone:/sdcard/DCIM/Camera/ProfilePic.jpeg","PUBLIC:ProfilePic.jpeg");

			driver.resetApp();
			ApplicationUtilities.closeApplicationByIdentifier(driver,"com.google.android.apps.photos");
			ApplicationUtilities.startApplicationByIdentifier(driver,"com.google.android.apps.photos");

			if (osname.equalsIgnoreCase("android")) {
				ApplicationUtilities.closeApplicationByName(driver,"Waze");
				ApplicationUtilities.startApplicationByName(driver,"Waze");
				reportiumClient.stepStart("1. Open app");

				driver.findElement(By.xpath(
						"//*[@resource-id='com.android.packageinstaller:id/permission_allow_button']|//*[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']"))
						.click();


				driver.findElement(By.id("authWelcomeBottomButton")).click();
				driver.findElement(By.id("com.waze:id/btnScrollToBottom")).click();
				driver.findElement(By.id("AcceptButtonText")).click();
				reportiumClient.stepStart("3. Login with creds: ");
				//Thread.sleep(5000);
				driver.findElement(By.id("input")).sendKeys("cloudtestus");
				driver.findElement(By.id("emailNextButton")).click();
				driver.findElement(By.id("input")).sendKeys("Cloud1234");
				driver.findElement(By.id("next")).click();
//				driver.findElementByXPath("//*[@resource-id=\"com.waze:id/authWelcomeTopButtonText\"]").click();
//				driver.findElement(By.id("com.waze:id/btnScrollToBottom")).click();
//				driver.findElement(By.id("AcceptButtonText")).click();
//				driver.findElementByXPath("//*[@text=\"Sign up later\"]").click();

				reportiumClient.stepStart("4. Open main menu  (Click My waze)");
				if (driver.findElement(By.xpath("//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]")).isDisplayed())
					reportiumClient.reportiumAssert("Login successful", true);
				else
					reportiumClient.reportiumAssert("Login successful", false);

				try {
					ocrTextSelect(driver, "Tap to");
				} catch (Exception e) {

				}

				reportiumClient.stepStart("5. Change profile pic - Choose existing picture");
				new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]")));

				driver.findElement(By.xpath(
						"//*[@resource-id=\"com.waze:id/leftMenuButtonImage\"]"))
						.click();

				driver.findElementByXPath("//*[@resource-id=\"com.waze:id/myWazeProfileMood\"]").click();
				driver.findElementByXPath("//*[@resource-id=\"com.waze:id/youOnMapImage\"]").click();
				driver.findElementByXPath("//*[@resource-id=\"com.waze:id/action_icon\"]").click();

				driver.findElementByXPath("//*[@text=\"Choose existing picture\"]").click();
								try{
									driver.findElementByXPath("//*[@resource-id=\"com.android.packageinstaller:id/permission_allow_button\"]").click();

									//driver.findElementByXPath("//*[@resource-id=\"com.android.packageinstaller:id/permission_allow_button\"]").click();
				}catch (Exception NoSuchElementException){}

				driver.findElementByXPath("//*[@text=\"Photos\"]").click();

				driver.findElementByXPath("//*[contains(@content-desc,\"Photo taken\")]").click();
				driver.findElementByXPath("//*[@resource-id=\"com.google.android.apps.photos:id/photos_photoeditor_fragments_editor3_save\"]").click();

				Thread.sleep(10000);

				Files.deleteFile(driver,"phone:/sdcard/DCIM/Camera/ProfilePic.jpeg");

			}
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
		params.put("timeout", "120");
		driver.executeScript("mobile:text:select", params);
	}
}
