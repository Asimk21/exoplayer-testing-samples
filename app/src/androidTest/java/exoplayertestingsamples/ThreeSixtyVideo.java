package exoplayertestingsamples;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ui.spherical.SphericalSurfaceView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import player.CapturingPlayerStateListener;
import player.Player;
import player.PlayerFactory;
import wiremockextensions.FileSourceAndroidAssetFolder;
import wiremockextensions.WireMockStaticFileFromRequestPathTransformer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ThreeSixtyVideo {
    @Rule
    public ActivityTestRule<Activity> activityTestRule = new ActivityTestRule<>(Activity.class);
    private Player player;


    @Test
    public void
    manualAssertDoes360() {
        final Context context = InstrumentationRegistry.getContext();

        String threeSixtyURLString = "urlHere";
        player = new PlayerFactory(context).playerForUrl(threeSixtyURLString);

        CapturingPlayerStateListener playerStateListener = new CapturingPlayerStateListener();

        player.addStateListener(playerStateListener);

        playerStateListener.awaitReady();

        activityTestRule.getActivity().runOnUiThread((Runnable) () -> {
            FrameLayout view = createTextureViewInActivity();
            threeSixtyBaby(view);
        });

        player.play();

        SystemClock.sleep(30_000); //wait for playback to advance into the green section

    }

    private void threeSixtyBaby(FrameLayout view) {
        SphericalSurfaceView sphericalSurfaceView = new SphericalSurfaceView(view.getContext());
        sphericalSurfaceView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        view.addView(sphericalSurfaceView);
        sphericalSurfaceView.setDefaultStereoMode(C.STEREO_MODE_MONO);
        sphericalSurfaceView.setVideoComponent(player.videoComponent());
        sphericalSurfaceView.onResume();
    }

    private FrameLayout createTextureViewInActivity() {
        final Activity activity = activityTestRule.getActivity();
        final FrameLayout view = new FrameLayout(activity);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        activity.setContentView(view);

        return view;
    }


}
