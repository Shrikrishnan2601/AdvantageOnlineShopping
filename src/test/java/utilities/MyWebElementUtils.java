package utilities;

import java.time.Duration;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyWebElementUtils {

	private WebDriver driver;
	private WebDriverWait wait;

	public MyWebElementUtils(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	}

	public void click(By locator) {
		for (int i = 0; i < 3; i++) {
			try {
				scrollToElement(locator);
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
				element.click();
				return;
			} catch (ElementNotInteractableException e) {
				if (i == 2) {
					fallBackClick(locator);
				}
			}
		}
	}

	public void sendKeys(By locator, String text) {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		element.clear();
		element.sendKeys(text);
	}

	public void sendKeysWithAction(By locator, String text, Keys actionKey) {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		element.clear();
		element.sendKeys(text, actionKey);
	}

	public String getAttribute(By locator, String attribute) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		return element.getDomAttribute(attribute);
	}

	public void setAttribute(By locator, String attribute, String value) {
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute(arguments[1],arguments[2]);", element,
				attribute, value);
	}

	public void selectValueByVisibletext(By locator, String value) {
		WebElement element = driver.findElement(locator);
		Select s = new Select(element);
		s.selectByVisibleText(value);
	}

	public void selectValueRandomly(By locator) {
		WebElement element = driver.findElement(locator);
		Select s = new Select(element);
		List<WebElement> options = s.getOptions();
		Random random = new Random();
		int randomIndex = random.nextInt(0, options.size());
		s.selectByIndex(randomIndex);
	}

	public void clickRadioButton(By locator) {
		WebElement radiobutton = wait.until(ExpectedConditions.elementToBeClickable(locator));
		if (!radiobutton.isSelected()) {
			radiobutton.click();
		}
	}

	public void scrollToElement(By locator) {
		WebElement element = driver.findElement(locator);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void fallBackClick(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			scrollToElement(locator);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
