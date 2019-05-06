package weatherapp;

import java.util.concurrent.TimeUnit;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class used to ensure the correct integration of the System with the WebApplication.
 * 
 * @author Filipe Pires
 */
@SpringBootTest(classes = {WeatherApplication.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
public class WeatherApplicationIT {
    
    /**
     * WebDriver used to test the WebApp in Chrome.
     */
    private WebDriver driver;
    /**
     * URL of the WeatherApplication.
     */
    private String baseURL;
    /**
     * Buffer used to verify the occurrence of errors.
     */
    private StringBuffer verifErrors = new StringBuffer();
    
    /**
     * Port where the system runs.
     */
    @LocalServerPort
    private int serverPort = 8080;
    
    /**
     * Method called every time a method annotated with @Test is executed, before its execution.
     */
    @BeforeEach
    public void setUp() throws Exception {
        baseURL = "http://localhost:"+ serverPort + "/";
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    
    /**
     * Method called every time a method annotated with @Test is executed, after its execution.
     */
    @AfterEach
    public void tearDown() throws Exception {
        driver.quit();
        String verifErrorString = verifErrors.toString();
        if (!verifErrorString.equals("")) {
            fail(verifErrorString);
        }
    }
    
    /**
     * Test of the basic features and elements of the WebApp.
     */
    @Test
    public void testWebPage() throws Exception {
        System.out.println("web page");
        // arrange
        driver.get(baseURL);
        this.waitForPageLoad(driver);
        // act and assert
        assertThat(driver.getTitle()).isEqualTo("Weather Forecast Web Application");
        assertThat(driver.findElement(By.id("input-days"))).isNotNull();
        assertThat(driver.findElement(By.id("weather-forecast"))).isNotNull();
        assertThat(driver.findElement(By.id("show_and_hide"))).isNotNull();
        driver.findElement(By.id("show_and_hide")).click();
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.id("author")), 250, 250).click().build().perform();
        Thread.sleep(5000);
    }
    
    /**
     * Test of getWeatherNow through the WebAPP.
     */
    @Test
    public void testWeatherNowHere() throws Exception {
        System.out.println("weather now here");
        // arrange
        driver.get(baseURL);
        this.waitForPageLoad(driver);
        int timeout = 10;
        // act and assert
        driver.findElement(By.id("weather-forecast")).click();
        assertTrue(this.contentLoaded("table"));
        assertTrue(this.loadHttpContent(timeout,"tr-1"));
        assertThat(driver.findElement(By.id("refresh"))).isNotNull();
        driver.findElement(By.id("refresh")).click();
    }
    
    /**
     * Test of getWeatherNow through the WebAPP, given an input of coordinates.
     */
    @Test
    public void testWeatherNowThere() throws Exception {
        System.out.println("weather now there");
        // arrange
        driver.get(baseURL);
        this.waitForPageLoad(driver);
        int timeout = 10;
        String input_lat = "38.889931";
        String input_long = "-77.009003";
        // act and assert
        driver.findElement(By.id("input-lat")).sendKeys(input_lat);
        driver.findElement(By.id("input-long")).sendKeys(input_long);
        driver.findElement(By.id("weather-forecast")).click();
        assertTrue(this.contentLoaded("table"));
        assertTrue(this.loadHttpContent(timeout,"tr-1"));
    }
    
    /**
     * Test of getWeatherRecent through the WebAPP.
     */
    @Test
    public void testWeatherRecent() throws Exception {
        System.out.println("weather recent");
        // arrange
        driver.get(baseURL);
        this.waitForPageLoad(driver);
        int timeout = 10;
        String input_days = "3";
        // act and assert
        driver.findElement(By.id("input-days")).sendKeys(input_days);
        driver.findElement(By.id("weather-forecast")).click();
        assertTrue(this.contentLoaded("table"));
        assertTrue(this.loadHttpContent(timeout, "tr-"+input_days));
        assertThat(driver.findElement(By.id("refresh"))).isNotNull();
    }
    
    /**
     * Test of getWeatherPeriod through the WebAPP.
     */
    @Test
    public void testWeatherPeriod() throws Exception {
        System.out.println("weather period");
        // arrange
        driver.get(baseURL);
        this.waitForPageLoad(driver);
        int timeout = 1000;
        String input_start = "2019-05-01";
        String input_end = "2019-05-07";
        // act and assert
        driver.findElement(By.id("datepicker-start")).sendKeys(input_start);
        driver.findElement(By.id("datepicker-end")).sendKeys(input_end);
        driver.findElement(By.id("weather-forecast")).click();
        assertTrue(this.contentLoaded("table"));
        assertTrue(this.loadHttpContent(timeout, "tr-6"));
        assertThat(driver.findElement(By.id("refresh"))).isNotNull();
    }
    
    /**
     * Private internal method used to ensure that the tests only start once the page is fully loaded.
     * @param driver WebDriver used during the tests
     */
    private void waitForPageLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
            ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                }
            };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }
    
    /**
     * Private internal method used to make the test wait while the server processes a request.
     * @param timeout maximum waiting time in seconds
     * @param verif_id html element id used to verify when has the server responded
     * @return true if the server responds within proper time, false if timedout
     */
    private boolean loadHttpContent(int timeout, String verif_id) {
        boolean retval = false;
        int time = 0;
        while(time<timeout) {
            try {
                if(this.contentLoaded(verif_id)) {
                    retval = true;
                    break;
                }
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                fail("Test did not execute correctly.");
            }
            time++;
        }
        return retval;
    }
    
    /**
     * Verifies if an html element is loaded.
     * @param verif_id id of the html element
     * @return true if the element is displayed, false if not
     */
    private boolean contentLoaded(String verif_id) {
        return driver.findElement(By.id(verif_id)).isDisplayed();
    }
    
}
