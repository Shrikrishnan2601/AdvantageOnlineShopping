package testcases;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {

	public static WebDriver driver;
	public WebDriverWait wait;
	public Logger logger;
	public Properties p = new Properties();
	public static PDDocument document = new PDDocument();
	public static final String pdfFilePath = "";

	@BeforeMethod
	@Parameters("browser")
	public void startup(String browser, Method method) throws IOException {
		FileReader file = new FileReader("./src//test//resources//config.properties");
		p.load(file);
		logger = LogManager.getLogger(this.getClass());
		initializeBrowser(browser);
		logger.info("Initialized browser : " + browser);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.manage().window().maximize();
		loadURL();
	}
	
	

	public void initializeBrowser(String browser) {
		String selectedBrowser = System.getProperty("browser", browser);

		switch (selectedBrowser.toLowerCase()) {
		case "chrome":
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			break;
		default:
			throw new IllegalArgumentException("Invalid browser: " + selectedBrowser);
		}

	}

	
	public void loadURL() {
		int attempt = 0;
		int maxAttemps = 3;
		boolean urlLoaded = false;

		while (attempt < maxAttemps && !urlLoaded) {
			driver.get(p.getProperty("aosurl"));
			if (driver.getTitle().equals("Advantage Shopping")) {
				urlLoaded = true;
				logger.info("URL loaded successfully on attempt " + (attempt + 1));
			} else {
				logger.warn("Attempt " + (attempt + 1) + " failed to load URL, Retrying...");
				attempt++;
				if (attempt == maxAttemps) {
					logger.error("Failed to load the URL even after " + maxAttemps + " attempts.");
					throw new RuntimeException("Could not load the URL after multiple attempts.");
				}
			}

		}
	}

	public String captureScreen(String testName) throws IOException {
		String currentTimestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File sourceFile = ts.getScreenshotAs(OutputType.FILE);
		String targetFilePath = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + currentTimestamp
				+ ".png";
		File targetFile = new File(targetFilePath);
		sourceFile.renameTo(targetFile);
		return targetFilePath;
	}

}
