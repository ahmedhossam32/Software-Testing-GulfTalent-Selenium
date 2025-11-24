# Software Testing – GulfTalent Selenium Automation

This repository contains Selenium WebDriver automation for the GulfTalent website, developed as part of a Software Quality Assurance (SQA) course project.

The focus of this project is to practice functional GUI testing, basic test design, and automation of positive and negative scenarios using Java + Selenium (following the same implementation style used in the SQA labs).

---

## 📌 Objectives

- Automate real job application flows on **GulfTalent**
- Implement **positive & negative** test cases for the “Easy Apply” feature
- Automate a **search functionality** (keyword + country)
- Follow the **basic Selenium WebDriver structure** taught in lab:
  - ChromeDriver
  - Implicit waits
  - Explicit waits
  - XPaths / IDs
  - Simple `main` method to run tests

---

## 🛠 Tech Stack

- **Language:** Java  
- **Automation:** Selenium WebDriver  
- **Build Tool:** Maven  
- **Browser:** Google Chrome  
- **IDE:** IntelliJ IDEA  

Selenium features used:
- Element locators (`By.id`, `By.xpath`, `By.cssSelector`)
- Implicit wait
- Explicit wait (`WebDriverWait`)
- Dropdown handling (`Select`)
- JavaScript scrolling (`JavascriptExecutor`)
- Handling multiple tabs (`switchTo().window(...)`)
- Basic assert-style logging using conditional checks

---

## 📁 Project Structure

Ahmed_GulfTalent_Selenium/
├── pom.xml
└── src
└── main
└── java
└── com.job
├── GulfTalentTests.java # Core test automation
└── Main.java # Test runner


---

# ✅ Implemented Test Cases

## 1. **TC01 – Apply Job (Positive) – Draftsman**
**Method:** `test_TC01_ApplyJob_Positive_Draftsman()`

**Flow:**
- Login as **Ahmed** (strong CV)
- Search for “Draftsman”
- Filter with **Easy Apply**
- Select first **non-applied** job
- Open job in new tab
- Perform full **Easy Apply flow**
  - Easy Apply click  
  - Proceed (if exists)  
  - Apply as Standard (if exists)  
  - Submit Application  
- Verify success message (“successfully delivered”)

**Assertion Style:**  
- SUCCESS flash message must appear  
- Final log: `✅ TEST PASSED (Draftsman Easy Apply)`  

---

## 2. **TC02 – Apply Job (Negative) – Weak CV – Draftsman**
**Method:** `test_TC02_ApplyJob_Negative_Draftsman()`

**Flow:**
- Login as **Mariam** (weak CV)
- Search for “Draftsman”
- Filter with **Easy Apply**
- Open first job
- Click **Easy Apply**
- Expect blocking message about CV
- Verify redirect to **Profile/CV** page

**Assertion Style:**  
- Error message containing “CV” must appear  
- URL must contain `profile` or `cv`  
- Final log:  
  `✔ Weak CV error message found`  
  `✔ Redirected to profile edit page`  
  `✅ TEST PASSED: Mariam blocked from applying — weak CV (Draftsman).`

---

## 3. **TC03 – Search Job by Keyword + Country**
**Method:** `test_TC03_SearchJob_ByKeywordAndCountry_Positive()`

**Flow:**
- Open home page
- Keyword: `"Software Engineer"`
- Country: `"Jordan"`
- Search
- Validate:
  - Job results appear
  - Country filter shows Jordan selected
  - First 1–3 job titles contain `"Software"`

**Assertion Style:**  
- Check result count  
- Check checkbox  
- Check title text  
- Final log:  
  `✅ TEST PASSED: Keyword + Country search works correctly.`

---

## 4. **Login Test – Ahmed**
**Method:** `testLoginAhmed_Positive()`

**Flow:**
- Login using login helper
- Validate:
  - Ahmed’s name visible in the header
  - URL contains `/candidates/home`

**Assertion Style:**  
- Name element exists  
- URL check  
- Final log:  
  `✅ TEST PASSED: Ahmed login successful.`

---

# 🔧 Helper Methods (Summary)

Helpers located in **GulfTalentTests.java**:
- `setUp()` – Open browser, maximize, implicit wait  
- `tearDown()` – Close browser  
- `loginAsAhmed()` / `loginAsMariam()`  
- `searchJobsFromCandidateHome()`  
- `switchToNewTab()`  
- `applyEasyApplyFlow()`  
  - Easy Apply  
  - Proceed (optional)  
  - Apply as Standard (optional)  
  - Submit Application (optional)

All helpers follow the learning style of the lab examples.

---

# ▶️ How to Run the Tests

### **Run From IntelliJ (recommended)**

Open `Main.java`:

```java
public class Main {
    public static void main(String[] args) {

        GulfTalentTests tests = new GulfTalentTests();

        // Run a single test
        tests.setUp();
        tests.test_TC01_ApplyJob_Positive_Draftsman();
        tests.tearDown();

        // Or run all tests:

        /*
        tests.setUp();
        tests.test_TC01_ApplyJob_Positive_Draftsman();
        tests.tearDown();

        tests.setUp();
        tests.test_TC02_ApplyJob_Negative_Draftsman();
        tests.tearDown();

        tests.setUp();
        tests.test_TC03_SearchJob_ByKeywordAndCountry_Positive();
        tests.tearDown();

        tests.setUp();
        tests.testLoginAhmed_Positive();
        tests.tearDown();
        */
    }
}





