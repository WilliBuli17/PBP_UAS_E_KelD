package com.example.tubes_kelompok_d;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.tubes_kelompok_d.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UpdateProfilTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void updateProfilTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.Email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.loggin),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.Email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.loggin),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("buliwilliams17@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.loggin),
                                        2),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.Login), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.loggin),
                                        3),
                                0),
                        isDisplayed()));
        materialButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.nav_search), withContentDescription("Profil"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.btnEditProfile), withText("Edit Profil"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        10),
                                0),
                        isDisplayed()));
        materialButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.updateNamaView), withText("willi Buli"),
                        childAtPosition(
                                allOf(withId(R.id.nameLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                2),
                        isDisplayed()));
        textInputEditText.perform(replaceText("EditBaruTes"));

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.updateNamaView), withText("EditBaruTes"),
                        childAtPosition(
                                allOf(withId(R.id.nameLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                4)),
                                2),
                        isDisplayed()));
        textInputEditText2.perform(closeSoftKeyboard());


        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.updatePhoneView), withText("08123456789"),
                        childAtPosition(
                                allOf(withId(R.id.phoneLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                6)),
                                2),
                        isDisplayed()));
        textInputEditText3.perform(replaceText("08987654321"));

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.updatePhoneView), withText("08987654321"),
                        childAtPosition(
                                allOf(withId(R.id.phoneLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                6)),
                                2),
                        isDisplayed()));
        textInputEditText4.perform(closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnUpdateProfile), withText("Update Profil"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        8),
                                0),
                        isDisplayed()));
        materialButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.nav_search), withContentDescription("Profil"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottom_navigation),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
