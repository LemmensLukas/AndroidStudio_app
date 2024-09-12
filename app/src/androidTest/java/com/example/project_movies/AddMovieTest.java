package com.example.project_movies;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddMovieTest {

    @Rule
    public ActivityScenarioRule<AddMovieActivity> mAddActivityScenarioRule = new ActivityScenarioRule<>(AddMovieActivity.class);

    @Before
    public void addMovieTitle(){
        onView(withId(R.id.add_movie_title)).perform(typeText("UI test"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.add_movie)).perform(click());
    }

    @Test
    public void checkMovieTitle(){
        onView(ViewMatchers.withId(R.id.rv_movies)).perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));
        onView(withId(R.id.title_input_edit)).check(matches(withText("UI test")));
    }
}
