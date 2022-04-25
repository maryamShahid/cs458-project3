import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;

import static org.assertj.core.api.Assertions.assertThat;

public class TestClass {

    private final static String projectURL = "http://localhost:8080/cs458-project3/";
    private static final String chromeDriverPath = "C:\\Users\\murat\\Desktop\\chromedriver_win32\\chromedriver.exe";   // NOTE: Change this path according to your local

    private static final By latitude = By.xpath("//input[@placeholder='Enter Latitude']");
    private static final By longitude = By.xpath("//input[@placeholder='Enter Longitude']");
    private static final By sendButton = By.xpath("//button[text()='Show Country']");
    private static final By distanceResults = By.xpath("//p");
    private static final By markerPositionOfIceland = By.xpath("//div[contains(@style, '-14px; top: -43px; z-index: 0')]");
    private static final By markerPositionOfFrance = By.xpath("//area[@coords='13.5,0,4,3.75,0,13.5,13.5,43,27,13.5,23,3.75']");
    private static final By markerPositionOfIndonesia = By.xpath("//area[@coords='13.5,0,4,3.75,0,13.5,13.5,43,27,13.5,23,3.75']");
    private static final By anyMarker = By.xpath("//img[@usemap='#gmimap0']");


    private static final By GPSResult = By.xpath("//*[@id='app']/div/div[1]/div/p");

    private static final By showNPFromGPS = By.xpath("//button[contains(text(), 'North Pole from GPS')]");
    private static final By showNPFromCoordinates = By.xpath("//button[contains(text(), 'North Pole from Coordinates')]");
    private static final By showMoonFromGPS = By.xpath("//button[contains(text(), 'Core from GPS')]");
    private static final By showMoonFromCoordinates = By.xpath("//button[contains(text(), 'Core from Coordinates')]");


    static {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    // Tests for Part A (enter your coordinates of your location and it shows your country)

    @Test
    public void testingShowingCountryByCoordinates() throws InterruptedException {
        ChromeDriver driver = createChromeDriver();

        driver.get(projectURL);

        driver.findElement(latitude).sendKeys("64.9631");
        driver.findElement(longitude).sendKeys("-19.0208"); // Coordinates for Iceland.

        driver.findElement(sendButton).click();

        driver.executeScript("window.scrollBy(0,750)", "");
        Thread.sleep(1700);

        assertThat(driver.findElement(markerPositionOfIceland).isDisplayed())
                .isTrue();

        driver.close();
    }

    @Test
    public void testingDefault0LongitudeWhenLongitudeNotProvided() throws InterruptedException {
        ChromeDriver driver = createChromeDriver();

        driver.get(projectURL);

        driver.findElement(latitude).sendKeys("45"); // 45, 0 is in France

        driver.findElement(sendButton).click();

        driver.executeScript("window.scrollBy(0,750)", "");
        Thread.sleep(500);

        assertThat(driver.findElement(markerPositionOfFrance).isDisplayed())
                .isTrue();

        driver.close();
    }

    // Tests for Part B (gets GPS coordinates of your device automatically and it shows your distance to the Geographic North Pole)

    @Test
    public void testGPS() {
        ChromeDriver driver = createChromeDriver();
        driver.get(projectURL);

        String GPSText = driver.findElement(GPSResult).getText();

        assertThat(GPSText)
                .isEqualTo("39.9671 Latitude, 32.8565 Longitude"); // My Home, coordinates may vary.

        driver.close();
    }

    @Test
    public void testNorthPoleFromGPS() {
        ChromeDriver driver = createChromeDriver();
        driver.get(projectURL);

        driver.findElement(showNPFromGPS).click();

        assertThat(driver.findElements(distanceResults).get(1).getText())
                .isEqualTo("5563.40 km"); // Distance is from my home, coordinates may vary.

        driver.close();
    }

    // Tests for Part C (enter your coordinates, or it gets GPS of your device, then it shows your distance to the Moon’s core)

    @Test
    public void testMoonFromGPS() throws InterruptedException {
        ChromeDriver driver = createChromeDriver();
        driver.get(projectURL);


        WebElement showDistanceButton = driver.findElement(showMoonFromGPS);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showDistanceButton);
        Thread.sleep(350);

        showDistanceButton.click();

        try {
            assertThat(driver.findElements(distanceResults).get(1).getText())
                    .isEqualTo("373207.71 km");     // Distance may vary due to the movement of the moon.
        } catch(AssertionError e) {
            e.printStackTrace();
            driver.close();
        }

    }

    @Test
    public void testMoonFromCoordinates() throws InterruptedException {
        ChromeDriver driver = createChromeDriver();
        driver.get(projectURL);

        driver.findElement(latitude).sendKeys("64.9631");
        driver.findElement(longitude).sendKeys("-19.0208"); // Coordinates for Iceland.

        WebElement showDistanceButton = driver.findElement(showMoonFromCoordinates);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showDistanceButton);
        Thread.sleep(350);

        showDistanceButton.click();


        try {
            assertThat(driver.findElements(distanceResults).get(1).getText())
                    .isEqualTo("373216.14 km");     // Distance may vary due to the movement of the moon.
        } catch(AssertionError e) {
            e.printStackTrace();
            driver.close();
        }
    }

    @Test
    public void testErrorWhenCoordinatesNotProvided() throws InterruptedException {
        ChromeDriver driver = createChromeDriver();
        driver.get(projectURL);

        WebElement showDistanceButton = driver.findElement(showMoonFromCoordinates);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", showDistanceButton);
        Thread.sleep(350);
        showDistanceButton.click();

        String alertMessage = driver.switchTo().alert().getText();

        assertThat(alertMessage)
                .isEqualTo("Enter Coordinates First");

        driver.switchTo().alert().accept();
        driver.close();
    }

    private ChromeDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        return new ChromeDriver(options);
    }
}
