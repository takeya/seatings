package com.takeya.seatingsplanner.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Checks;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.takeya.seatingsplanner.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;


/**
 * Created by Takeya on 26.11.2017.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    /* Instantiate an IntentsTestRule object. */
    @Rule
    public ActivityTestRule<MainActivity> mIntentsRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        mIntentsRule.getActivity();
    }

    @Test
    public void firstItemClickTest() throws Exception {
        onView(withId(R.id.customer_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void AbrahamLincolnLoadedTest() throws Exception {
        onView(allOf(withId(R.id.customer_item_name), withText("Abraham Lincoln")))
                .check(matches(withText(containsString("Abraham Lincoln"))));
    }

    @Test
    public void AbrahamLincolnTableTenFreeTest() throws Exception {
        onView(allOf(withId(R.id.customer_item_name), withText("Abraham Lincoln")))
                .perform(click());

        onView(allOf(withId(R.id.table_item_layout),
                withChild(allOf(withId(R.id.table_number), withText("10")))))
                .check(matches(withBackgroundColor(
                        Color.LTGRAY)));
    }

    @Test
    public void AbrahamLincolnReserveTableTenTest() throws Exception {
        onView(allOf(withId(R.id.customer_item_name), withText("Abraham Lincoln")))
                .perform(click());

        onView(allOf(withId(R.id.table_item_layout),
                withChild(allOf(withId(R.id.table_number), withText("10")))))
                .perform(click())
                .check(matches(withBackgroundColor(Color.GREEN)));
    }

    private static Matcher<View> withBackgroundColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, ConstraintLayout>(ConstraintLayout.class) {
            @Override
            public boolean matchesSafely(ConstraintLayout view) {
                return color == ((ColorDrawable) view.getBackground()).getColor();
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with background color: ");
            }
        };
    }
}