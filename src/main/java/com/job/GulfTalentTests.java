package com.job;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class GulfTalentTests {

    WebDriver driver;


    // STEP A: Open browser
    public void setUp() {
        System.out.println(">>> setUp START");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        System.out.println(">>> setUp END");
    }

    // STEP B: Close browser (wait 2 seconds only)
    public void tearDown() {
        System.out.println(">>> tearDown START");

        sleep(2000);

        if (driver != null) {
            driver.quit();
        }
        System.out.println(">>> tearDown END");
    }


    /* =========================
       HELPER METHODS (COMMON)
       ========================= */

    private void openHomePage() {
        driver.get("https://www.gulftalent.com");
    }

    // Small sleep wrapper (used everywhere)
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
        }
    }

    // Switch to the newly opened tab
    private void switchToNewTab(String originalWindow) {
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    //search job method
    private void searchJobsFromCandidateHome(String keyword, String countryVisibleText) {
        // Search textbox
        WebElement keywordInput = driver.findElement(By.xpath("//input[@data-cy='job-search-textbox']"));
        keywordInput.clear();
        keywordInput.sendKeys(keyword);

        // Country dropdown
        WebElement countryDropdown = driver.findElement(By.xpath("//select[@data-cy='job-search-dropdown']"));
        Select selectCountry = new Select(countryDropdown);
        selectCountry.selectByVisibleText(countryVisibleText);

        // Click Search
        WebElement findJobsButton = driver.findElement(By.xpath("//button[@data-cy='job-search-btn']"));
        findJobsButton.click();

        sleep(1500);
    }


    /**
     * Common Easy Apply flow:
     * - Click Easy Apply on job details
     * - Scroll and click Proceed (if present)
     * - Handle premium popup → Apply as Standard (if present)
     * - Click Submit Application (if present)
     */
    private void applyEasyApplyFlow(WebDriverWait wait, JavascriptExecutor js) {

        // Easy Apply button (must exist for this flow)
        WebElement easyApplyBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class,'btn-primary') and contains(.,'Easy Apply')]")));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", easyApplyBtn);
        sleep(300);
        js.executeScript("arguments[0].click();", easyApplyBtn);
        sleep(1000);

        // Proceed button (sometimes present)
        try {
            js.executeScript("window.scrollBy(0, 700);");
            sleep(400);

            WebElement proceedBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'Proceed')]")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", proceedBtn);
            sleep(300);
            js.executeScript("arguments[0].click();", proceedBtn);
            sleep(1000);
        } catch (Exception ignored) {
            System.out.println("ℹ 'Proceed' button not present – skipping.");
        }

        // Premium modal → Apply as Standard (sometimes present)
        try {
            WebElement applyStandardBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@data-cy='standard-apply-btn']")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", applyStandardBtn);
            sleep(300);
            js.executeScript("arguments[0].click();", applyStandardBtn);
            sleep(1000);
        } catch (Exception ignored) {
            System.out.println("ℹ Premium popup not shown – skipping.");
        }

        // Submit Application (sometimes present)
        try {
            WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'Submit Application')]")));
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", submitBtn);
            sleep(300);
            js.executeScript("arguments[0].click();", submitBtn);
            sleep(1200);
        } catch (Exception ignored) {
            System.out.println("ℹ 'Submit Application' not found – maybe Apply as Standard already submitted.");
        }
    }

    /* =========================
       LOGIN HELPERS
       ========================= */

    // Helper: login as Mariam (WEAK CV)
    public void loginAsMariam() {
        System.out.println(">>> loginAsMariam START");

        openHomePage();

        WebElement loginButton = driver.findElement(By.xpath("//a[@data-cy='nav-login-btn']"));
        loginButton.click();
        sleep(2000);

        WebElement emailInput = driver.findElement(By.xpath("//input[@data-cy='email-input']"));
        emailInput.clear();
        emailInput.sendKeys("MARIAMUSERNAME");

        WebElement passwordInput = driver.findElement(By.xpath("//input[@data-cy='password-input']"));
        passwordInput.clear();
        passwordInput.sendKeys("MARIAMPASS");

        WebElement submitButton = driver.findElement(By.xpath("//button[@data-cy='login-submit-btn']"));
        submitButton.click();
        sleep(5000);

        System.out.println(">>> loginAsMariam END");
    }

    // Helper: login as Ahmed (STRONG CV)
    public void loginAsAhmed() {
        System.out.println(">>> loginAsAhmed START");

        openHomePage();

        WebElement loginButton = driver.findElement(By.xpath("//a[@data-cy='nav-login-btn']"));
        loginButton.click();
        sleep(2000);

        WebElement emailInput = driver.findElement(By.xpath("//input[@data-cy='email-input']"));
        emailInput.clear();
        emailInput.sendKeys("AHMEDUSERNAME@yahoo.com"); // or your final email

        WebElement passwordInput = driver.findElement(By.xpath("//input[@data-cy='password-input']"));
        passwordInput.clear();
        passwordInput.sendKeys("AHMEDPASS"); // or your final password

        WebElement submitButton = driver.findElement(By.xpath("//button[@data-cy='login-submit-btn']"));
        submitButton.click();
        sleep(5000);

        System.out.println(">>> loginAsAhmed END");
    }

    public void testLoginAhmed_Positive() {
        System.out.println(">>> testLoginAhmed_Positive START");
        boolean testPassed = true;

        loginAsAhmed();

        // CHECK #1: Name is visible
        boolean nameVisible = false;
        try {
            WebElement userName = driver.findElement(By.xpath(
                    "//li[contains(translate(text(),'AHMED','ahmed'),'ahmed')]"
            ));
            if (userName.isDisplayed()) {
                nameVisible = true;
                System.out.println("CHECK #1 PASSED: Ahmed's name is visible → " + userName.getText());
            } else {
                System.out.println("CHECK #1 FAILED: Ahmed's name NOT displayed.");
                testPassed = false;
            }
        } catch (NoSuchElementException e) {
            System.out.println("CHECK #1 FAILED: Ahmed's name element NOT found.");
            testPassed = false;
        }

        // CHECK #2: Redirect to Candidate Home
        boolean correctUrl = false;
        String url = driver.getCurrentUrl();
        if (url.contains("/candidates/home")) {
            correctUrl = true;
            System.out.println("CHECK #2 PASSED: Redirected to Candidate Home page.");
        } else {
            System.out.println("CHECK #2 FAILED: Wrong URL → " + url);
            testPassed = false;
        }

        if (nameVisible && correctUrl && testPassed) {
            System.out.println("✅ TEST PASSED: Ahmed login successful.");
        } else {
            System.out.println("❌ TEST FAILED: Ahmed login failed.");
        }

        System.out.println(">>> testLoginAhmed_Positive END");
    }



    /* =========================
       TEST CASE 1 – APPLY JOB POSITIVE
       ========================= */
    public void test_TC01_ApplyJob_Positive_Draftsman() {

        System.out.println(">>> TC01_ApplyJob_Positive_Draftsman START");
        boolean testPassed = true;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // STEP 1 – Login as Ahmed (strong CV)
        loginAsAhmed();

        // STEP 2 – Search for "Draftsman"
        searchJobsFromCandidateHome("Draftsman", "All Countries");

        // STEP 3 – Filter by Easy Apply
        WebElement easyApplyFilter = driver.findElement(
                By.xpath("//input[@name='filters[has_external_application][0]']"));
        if (!easyApplyFilter.isSelected()) {
            easyApplyFilter.click();
        }
        sleep(1000);

        // STEP 4 – Pick first NON-applied job row
        List<WebElement> rows = driver.findElements(By.xpath("//tr[@data-cy='job-result-row']"));
        WebElement targetJobLink = null;

        for (WebElement row : rows) {
            // skip rows that already have applied icon
            if (row.findElements(By.xpath(".//i[@data-cy='applied-job-icon']")).isEmpty()) {
                targetJobLink = row.findElement(By.xpath(".//a[@data-cy='job-link']"));
                break;
            }
        }

        if (targetJobLink == null) {
            System.out.println("❌ No un-applied Draftsman Easy Apply jobs found.");
            return;
        }

        String originalWindow = driver.getWindowHandle();
        System.out.println("Opening job: " + targetJobLink.getText());
        targetJobLink.click();

        // Switch tab
        switchToNewTab(originalWindow);
        sleep(1500);

        // STEP 5–8 – Run common Easy Apply flow
        applyEasyApplyFlow(wait, js);

        // STEP 9 – Validate success message
        try {
            WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@data-cy='flash-message-text' and contains(.,'successfully delivered')]")
            ));

            if (successMsg.isDisplayed()) {
                System.out.println("✔ Success message: " + successMsg.getText());
            } else {
                System.out.println("❌ Success message element not displayed.");
                testPassed = false;
            }
        } catch (Exception e) {
            System.out.println("❌ Success message NOT found.");
            testPassed = false;
        }

        if (testPassed) {
            System.out.println("✅ TEST PASSED (Draftsman Easy Apply)");
        } else {
            System.out.println("❌ TEST FAILED (Draftsman Easy Apply)");
        }

        System.out.println(">>> TC01_ApplyJob_Positive_Draftsman END");
    }


    /* =========================
    TEST CASE 2 – APPLY JOB NEGATIVE (Draftsman, weak CV)
    ========================= */
    public void test_TC02_ApplyJob_Negative_Draftsman() {

        System.out.println(">>> TC02_ApplyJob_Negative_Draftsman START");
        boolean testPassed = true;

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));

        // STEP 1 – Login as Mariam (weak CV)
        loginAsMariam();

        // STEP 2 – Search for "Draftsman" from candidate home
        searchJobsFromCandidateHome("Draftsman", "All Countries");

        // STEP 3 – Filter by Easy Apply
        WebElement easyApplyFilter = driver.findElement(
                By.xpath("//input[@name='filters[has_external_application][0]']"));
        if (!easyApplyFilter.isSelected()) easyApplyFilter.click();
        sleep(1500);

        // STEP 4 – Select first job
        List<WebElement> jobLinks = driver.findElements(By.xpath("//a[@data-cy='job-link']"));
        if (jobLinks.size() == 0) {
            System.out.println("❌ No Easy Apply Draftsman jobs found.");
            return;
        }

        String originalWindow = driver.getWindowHandle();
        System.out.println("Opening Draftsman job (negative test): " + jobLinks.get(0).getText());
        jobLinks.get(0).click();

        // Switch to new tab
        switchToNewTab(originalWindow);
        sleep(1500);

        // STEP 5 – Try Easy Apply
        WebElement easyApplyBtn = driver.findElement(
                By.xpath("//a[contains(@class,'btn-primary') and contains(.,'Easy Apply')]"));
        easyApplyBtn.click();
        sleep(1500);

        // STEP 6 – Validate weak CV error message
        boolean errorShown = false;
        try {
            WebElement flashMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@data-cy='flash-message-text' and contains(.,'CV')]")
            ));

            if (flashMessage.isDisplayed()) {
                System.out.println("✔ Weak CV error message: " + flashMessage.getText());
                errorShown = true;
            } else {
                System.out.println("❌ Weak CV error not shown (element not visible).");
                testPassed = false;
            }
        } catch (Exception e) {
            System.out.println("❌ Weak CV error message NOT found.");
            testPassed = false;
        }

        // STEP 7 – Validate redirect to Edit CV/Profile
        String url = driver.getCurrentUrl().toLowerCase();
        if (url.contains("profile") || url.contains("cv")) {
            System.out.println("✔ Redirected to Profile/CV page: " + url);
        } else {
            System.out.println("❌ Not redirected to CV/Profile.");
            testPassed = false;
        }

        // FINAL RESULT
        if (testPassed && errorShown) {
            System.out.println("✅ TEST PASSED: Mariam blocked from applying — weak CV (Draftsman).");
        } else {
            System.out.println("❌ TEST FAILED: Weak CV logic incorrect (Draftsman).");
        }

        System.out.println(">>> TC02_ApplyJob_Negative_Draftsman END");
    }


    /* =======================================================
       TEST CASE 3 – SEARCH JOB KEYWORD + COUNTRY (Positive)
       ======================================================= */
    public void test_TC03_SearchJob_ByKeywordAndCountry_Positive() {

        System.out.println(">>> TC_03_SearchJob_ByKeywordAndCountry_Positive START");

        // 1. Navigate to home
        openHomePage();

        // 2. Keyword input (home page search bar)
        WebElement keywordInput = driver.findElement(By.xpath("//input[@name='pos_ref']"));
        keywordInput.clear();
        keywordInput.sendKeys("Software Engineer");

        // 3. Select Country dropdown (home page)
        WebElement countryDropdown = driver.findElement(By.xpath("//select[@name='frmPositionCountry']"));
        Select selectCountry = new Select(countryDropdown);
        selectCountry.selectByVisibleText("Jordan");

        // 4. Click Search button
        WebElement findJobsButton = driver.findElement(By.xpath("//button[@data-cy='job-search-btn']"));
        findJobsButton.click();

        sleep(2000);

        boolean testPassed = true;

        // CHECK #1: At least one job row
        List<WebElement> jobRows = driver.findElements(By.xpath("//tr[@data-cy='job-result-row']"));

        if (jobRows.size() > 0) {
            System.out.println("CHECK #1 PASSED: Job results displayed. Count = " + jobRows.size());
        } else {
            System.out.println("CHECK #1 FAILED: No results found.");
            testPassed = false;
        }

        // CHECK #2: Jordan checkbox selected in filters
        WebElement jordanCheckbox =
                driver.findElement(By.xpath("//input[@name='filters[country][10229117000000]']"));

        if (jordanCheckbox.isSelected()) {
            System.out.println("CHECK #2 PASSED: Jordan is selected.");
        } else {
            System.out.println("CHECK #2 FAILED: Jordan NOT selected.");
            testPassed = false;
        }

        // CHECK #3: First 1–3 job titles contain "Software"
        int checkLimit = Math.min(3, jobRows.size());

        for (int i = 0; i < checkLimit; i++) {
            WebElement titleElement =
                    jobRows.get(i).findElement(By.xpath(".//a[@data-cy='job-link']//strong"));
            String title = titleElement.getText().toLowerCase();

            if (title.contains("software")) {
                System.out.println("CHECK #3 PASSED: Row " + (i + 1) + " title contains 'Software' → " + title);
            } else {
                System.out.println("CHECK #3 FAILED: Row " + (i + 1) + " title does NOT contain 'Software' → " + title);
                testPassed = false;
            }
        }

        // Final result
        if (testPassed) {
            System.out.println("✅ TEST PASSED: Keyword + Country search works correctly.");
        } else {
            System.out.println("❌ TEST FAILED: Issues in one or more validations.");
        }

        System.out.println(">>> TC_03_SearchJob_ByKeywordAndCountry_Positive END");
    }

}
