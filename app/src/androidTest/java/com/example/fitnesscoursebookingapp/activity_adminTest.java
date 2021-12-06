
package com.example.fitnesscoursebookingapp;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.*;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class activity_adminTest {

    @Rule
    public ActivityScenarioRule<activity_admin> mActivityRule = new ActivityScenarioRule<>(activity_admin.class);


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testLaunch(){

    }

    @After
    public void tearDown() throws Exception {
    }
}