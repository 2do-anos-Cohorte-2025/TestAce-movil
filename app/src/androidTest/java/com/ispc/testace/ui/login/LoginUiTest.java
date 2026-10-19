package com.ispc.testace.ui.login;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.espresso.accessibility.AccessibilityChecks;
import com.ispc.testace.ui.login.LoginActivity;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginUiTest {

    @BeforeClass
    public static void enableAccessibility() {
        AccessibilityChecks.enable();
    }

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testFlujoInteraccionLogin() {
        LoginScreen loginScreen = new LoginScreen();

        loginScreen.verificarCamposVisibles()
                .escribirEmail("pepe@gmail.com")
                .escribirPassword("pepe1234")
                .hacerClickIngresar();

    }
}