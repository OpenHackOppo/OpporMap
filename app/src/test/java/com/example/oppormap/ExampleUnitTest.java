package com.example.oppormap;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        OpporApi api = new DefaultOpporApi();
        System.out.println(api.getJobsNearby(59.3338483, 18.0735157, 30));
    }
}