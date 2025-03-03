package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testcases.BaseClass;

public class MyExtentReportManager implements ITestListener {

	public ExtentSparkReporter sparkReporter;// Deals with UI of the report
	public ExtentReports extentReport;// deals with the commons info on the report
	public ExtentTest extentTest;// creating the test case entries and update status of the test methods

	String reportName;
	
	public void onStart(ITestContext context) {
		/*
		 * SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		 * Date date = new Date(); String currentTimestamp = dateFormatter.format(date);
		 */
		String currentTimestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		reportName = "FIT_COM_OM_Sanity_Report_" + currentTimestamp + ".html";
		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/reports/" + reportName);
		sparkReporter.config().setDocumentTitle("COM OM Sanity Automation");
		sparkReporter.config().setReportName("COM OM Sanity Automation");
		sparkReporter.config().setTheme(Theme.DARK);

		extentReport = new ExtentReports();
		extentReport.attachReporter(sparkReporter);

		extentReport.setSystemInfo("Computer Name", "localhost");
		extentReport.setSystemInfo("Environment", "QA");
		extentReport.setSystemInfo("Tester Name", System.getProperty("user.name"));
		// String os = context.getCurrentXmlTest().getParameter("os");
		extentReport.setSystemInfo("OS", "Windows");
		extentReport.setSystemInfo("Browser Name", "Chrome");
		System.out.println("Validation Starts.....");
	}

	public void onTestStart(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println("Starting the test on the method =====> " + methodName);
	}

	public void onTestSuccess(ITestResult result) {
		extentTest = extentReport.createTest(result.getName());// To create a new entry in the report
		extentTest.log(Status.PASS, "Test case Passed is : " + result.getName());

		String methodName = result.getMethod().getMethodName();
		System.out.println(methodName + " This test method got passed successfully ");
	}

	public void onTestFailure(ITestResult result) {
		extentTest = extentReport.createTest(result.getName());// To create a new entry in the report
		extentTest.log(Status.FAIL, "Test case Failed is : " + result.getName());
		extentTest.log(Status.FAIL, "Cause of Test case Failed is : " + result.getThrowable().getMessage());
		String methodName = result.getMethod().getMethodName();
		System.out.println(methodName + " This test method got Failed ");
		try {
			String imgPath = new BaseClass().captureScreen(result.getName());
			extentTest.addScreenCaptureFromPath(imgPath);
		}catch (IOException ex){
			ex.printStackTrace();			
		}
	}

	public void onTestSkipped(ITestResult result) {
		extentTest = extentReport.createTest(result.getName());// To create a new entry in the report
		extentTest.log(Status.SKIP, "Test case Skipped is : " + result.getName());
		extentTest.log(Status.INFO, "Cause of Test case Skipped is : " + result.getThrowable().getMessage());
		String methodName = result.getMethod().getMethodName();
		System.out.println(methodName + " This test method got Skipped ");
	}

	public void onFinish(ITestContext context) {
		extentReport.flush();
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+ reportName;
		File extentReportFile = new File(pathOfExtentReport);
		try {
			Desktop.getDesktop().browse(extentReportFile.toURI());
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Validation Ends.....");
	}
}
