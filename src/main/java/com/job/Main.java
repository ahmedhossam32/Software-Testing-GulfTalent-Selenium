package com.job;

public class Main {
    public static void main(String[] args) {

        GulfTalentTests tests = new GulfTalentTests();

        // TEST 1 – Apply Job Positive
        tests.setUp();
        tests.test_TC01_ApplyJob_Positive_Draftsman();
        tests.tearDown();

        // TEST 2 – Apply Job Negative
        tests.setUp();
        tests.test_TC02_ApplyJob_Negative_Draftsman();
        tests.tearDown();

        // TEST 3 – Search Job by Keyword + Country
        tests.setUp();
        tests.test_TC03_SearchJob_ByKeywordAndCountry_Positive();
        tests.tearDown();

        // (Optional) Login test
        tests.setUp();
        tests.testLoginAhmed_Positive();
        tests.tearDown();
    }
}
