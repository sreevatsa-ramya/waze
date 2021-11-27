package SampleCI;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.CustomField;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResult;
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

public class WazeTestCase3 {

	public static final String REPORT_TIMER_RESULT = "result";
	public static final String REPORT_TIMER_THRESHOLD = "threshold";
	public static final String REPORT_TIMER_DESCRIPTION = "description";
	public static final String REPORT_TIMER_NAME = "name";
	public static final String MOBILE_STATUS_TIMER_COMMAND = "mobile:status:timer";
	AppiumDriver driver;
	ReportiumClient reportiumClient;

	String device;
	String osname;
	String network;

	@Parameters({ "deviceName", "OS","Network_Condition" })
	@BeforeTest
	public void setUp(String deviceName, String OS, String Network_Condition) throws Exception {
		String browserName = null;
		String host = "demo.perfectomobile.com";
		String securityToken = "";
		osname = OS;
		device = deviceName;
		network = Network_Condition;
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
				.withContextTags("TC3").withWebDriver(driver).build();
		reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);

	}

	@Test
	public void test() throws InterruptedException {
		try {

			reportiumClient.testStart("WazeTestCase3",
					new TestContext.Builder().withTestExecutionTags("Performance", "UITest", network)
							.withCustomFields(new CustomField("developer", "waze")).build());
			DeviceVitals.startDeviceVitals(driver,5);
			if (osname.equalsIgnoreCase("android")) {
				reportiumClient.stepStart("1. Open app");

				reportiumClient.stepStart("2. Set location to Tel Aviv 32.071557, 34.784212");
				Location.setDeviceLocationByCordinates(driver, "32.07155, 34.78421");
				startNetworkVirtualization(driver,network);

				reportiumClient.stepStart("4. Measure time taken to launch get started screen");
				driver.findElement(By.xpath(
						"//*[@resource-id='com.android.packageinstaller:id/permission_allow_button']|//*[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']"))
						.click();

				ocrTextCheck(driver, "Log in", 98, 120);
				//Start the uxtimer
				//Maximum threshold is 10 seconds - Under which we need the results
				long uxTimer = getUXTimer(driver);
				System.out.println("Time took to Getstarted screen (in milliseconds) - " +uxTimer);
				reportTimer(driver, uxTimer, 10000, "Time taken to launch getstarted screen - ", "GetStarted");
				if(uxTimer>10000){
				try {
					throw new Exception("Network Test Failure");
				} catch (Exception ex) {
					TestResult testResult = TestResultFactory.createFailure("Network Test Failure", ex,
							"uvrqN5h04m");
					reportiumClient.testStop(testResult);
				}}
//				if(device.equals("RFCN90KKCGZ"))
//					Thread.sleep(15000);
//				else
//					Thread.sleep(40000);
				driver.findElement(By.id("authWelcomeBottomButton")).click();
				driver.findElement(By.id("com.waze:id/btnScrollToBottom")).click();
				driver.findElement(By.id("AcceptButtonText")).click();
				reportiumClient.stepStart("3. Login with creds: ");
				//Thread.sleep(5000);
				driver.findElement(By.id("input")).sendKeys("cloudtestus");
				driver.findElement(By.id("emailNextButton")).click();
				driver.findElement(By.id("input")).sendKeys("Cloud1234");
				driver.findElement(By.id("next")).click();

				reportiumClient.stepStart("4. Measure time taken to login");
				ocrTextCheck(driver, "Send Location", 98, 120);
				//Start the uxtimer
				//Maximum threshold is 10 seconds - Under which we need the results
				uxTimer = getUXTimer(driver);
				System.out.println("Time took to Login (in milliseconds) - " +uxTimer );
				reportTimer(driver, uxTimer, 10000, "Time taken to Login - ", "LoginTime");
				if(uxTimer>10000){
					try {
						throw new Exception("Network Test Failure");
					} catch (Exception ex) {
						TestResult testResult = TestResultFactory.createFailure("Network Test Failure", ex,
								"uvrqN5h04m");
						reportiumClient.testStop(testResult);
					}}

			}
			reportiumClient.testStop(TestResultFactory.createSuccess());
		} catch (Exception e) {
			reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
			e.printStackTrace();
		}
	}

	@AfterTest
	public void tearDown() throws Exception {
		//locationServices("on");
		//DeviceVitals.stopDeviceVitals(driver);
		//runShell("settings put secure location_providers_allowed +gps");
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


	// To start network virtualization
	public static void startNetworkVirtualization(AppiumDriver driver, String networkProfile) {
		Map<String, Object> params = new HashMap<>();
		params.put("profile", networkProfile);
		driver.executeScript("mobile:vnetwork:start", params);
	}

	// To stop network virtualization
	public static void stopNetworkVirtualization(AppiumDriver driver) {
		Map<String, Object> params = new HashMap<>();
		driver.executeScript("mobile:vnetwork:stop", params);
	}

	//To perform OCR Find
	public static void ocrTextFind(AppiumDriver driver, String textToBeFound)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("content", textToBeFound);
		params.put("timeout", "120");
		driver.executeScript("mobile:text:find", params);
	}

	// Perform text check ocr function
	public static String ocrTextCheck(AppiumDriver driver, String text, int threshold, int timeout) {
		// Verify that arrived at the correct page, look for the Header Text
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("content", text);
		params.put("timeout", Integer.toString(timeout));
		params.put("measurement", "accurate");
		params.put("source", "camera");
		params.put("analysis", "automatic");
		if (threshold > 0) {
			params.put("threshold", Integer.toString(threshold));
		}
		String result = (String) driver.executeScript("mobile:checkpoint:text", params);
		return result;
	}

	// Returns ux timer
	// Wind Tunnel: Gets the requested timer
	public static long timerGet(AppiumDriver driver, String timerType) {
		String command = "mobile:timer:info";
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", timerType);
		long result = (Long) driver.executeScript(command, params);
		return result;
	}
	public static long getUXTimer(AppiumDriver driver) {
		long timer = timerGet(driver, "ux");
		return timer;
	}

	public static String reportTimer(AppiumDriver driver, long result, long threshold, String description,
									 String name) {
		Map<String, Object> params = new HashMap<String, Object>(7);
		params.put(REPORT_TIMER_RESULT, result);
		params.put(REPORT_TIMER_THRESHOLD, threshold);
		params.put(REPORT_TIMER_DESCRIPTION, description);
		params.put(REPORT_TIMER_NAME, name);
		String status = (String) driver.executeScript(MOBILE_STATUS_TIMER_COMMAND, params);
		return status;
	}
}
