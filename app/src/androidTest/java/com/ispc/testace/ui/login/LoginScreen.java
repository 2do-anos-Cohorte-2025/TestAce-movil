package com.ispc.testace.ui.login;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.ispc.testace.R;

public class LoginScreen {
    // Recursos privados
    private final int etUsuario = R.id.etUsuario;
    private final int etPassword = R.id.etPassword;
    private final int btnLogin = R.id.btnLogin;

    // Métodos para el test
    public LoginScreen escribirEmail(String email) {
        onView(withId(etUsuario)).perform(typeText(email), closeSoftKeyboard());
        return this;
    }

    public LoginScreen escribirPassword(String password) {
        onView(withId(etPassword)).perform(typeText(password), closeSoftKeyboard());
        return this;
    }

    public LoginScreen hacerClickIngresar() {
        onView(withId(btnLogin)).perform(click());
        return this;
    }

    public LoginScreen verificarCamposVisibles() {
        onView(withId(etUsuario)).check(matches(isDisplayed()));
        onView(withId(etPassword)).check(matches(isDisplayed()));
        onView(withId(btnLogin)).check(matches(isDisplayed()));
        return this;
    }
}