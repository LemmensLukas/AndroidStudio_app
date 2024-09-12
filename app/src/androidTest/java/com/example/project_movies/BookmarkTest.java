package com.example.project_movies;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BookmarkTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mMainActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test   /*don't forget to set the bookmarked property back to false when rerunning tests => movie at position changed*/
    public void bookMarkMovieFromFalseToTrueAndCheckIfMatchesInRecyclerview(){
        onView(ViewMatchers.withId(R.id.rv_movies)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.iv_bookmark_false)).perform(click());
        onView(withId(R.id.menu)).perform(click());
        onView(withId(R.id.bookmarked)).perform(click());
        onView(ViewMatchers.withId(R.id.rv_bookmarked)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.title_input_edit)).check(matches(withText("Movie 1")));
    }

    @Test /*don't forget to set the bookmarked property back to true when rerunning tests => movie at position changed*/
    public void bookMarkMovieFromTrueToFalse(){
        onView(ViewMatchers.withId(R.id.rv_movies)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.iv_bookmark_true)).perform(click());
    }
}
