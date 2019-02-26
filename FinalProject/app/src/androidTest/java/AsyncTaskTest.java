import android.content.Context;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.util.Pair;

import com.udacity.gradle.builditbigger.AsyncTaskCompleteListener;
import com.udacity.gradle.builditbigger.MainActivity;
import com.udacity.gradle.builditbigger.MyEndpointsAsyncTask;
import com.udacity.gradle.builditbigger.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.os.AsyncTask.execute;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Suja Manu on 8/10/2018.
 */
@RunWith(AndroidJUnit4.class)
public class AsyncTaskTest {

    Context context;

    @Test
    public void testJoke() throws InterruptedException {
        assertTrue(true);
        final CountDownLatch latch = new CountDownLatch(1);
        context = InstrumentationRegistry.getContext();
        MyEndpointsAsyncTask testTask = new MyEndpointsAsyncTask() {
            @Override
            protected void onPostExecute(String result) {
                assertNotNull(result);
                if (result != null) {
                    assertTrue(result.length() > 0);
                    assertEquals("This is totally a funny joke",result);
                    latch.countDown();
                }
            }
        };
        testTask.execute(new Pair<Context, String>(context, "Suja"));
        latch.await();
    }
}