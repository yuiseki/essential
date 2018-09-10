package net.yuiseki.essential.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import net.yuiseki.essential.EssentialService;
import net.yuiseki.essential.R;

import retrofit2.Call;

public class EssentialDialog extends EssentialView {
    String TAG = "EssentialDialog";

    public EssentialDialog(final EssentialService essentialService) {
        super(essentialService);
        // Necessary for keyboard input
        essentialWindowLayoutParams.flags = 0;

        LayoutInflater inflater = LayoutInflater.from(essentialService);
        essentialView = inflater.inflate(R.layout.dialog_essential, null);
        essentialView.setFitsSystemWindows(true);
        essentialView.setPadding(0, 0, 0, 0);

        final EditText edittext = essentialView.findViewById(R.id.edittext);
        edittext.requestFocus();
        InputMethodManager im = (InputMethodManager)essentialService.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(edittext, InputMethodManager.SHOW_IMPLICIT);

        Button tweet = essentialView.findViewById(R.id.button_tweet);
        tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = edittext.getText().toString();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(essentialService);
                String token = sharedPreferences.getString("twitter_token", "");
                String secret = sharedPreferences.getString("twitter_secret", "");
                Long userId = sharedPreferences.getLong("twitter_user_id", 0);
                String userName = sharedPreferences.getString("twitter_user_name", "");

                TwitterSession session = new TwitterSession(new TwitterAuthToken(token, secret), userId, userName);
                TwitterApiClient client = TwitterCore.getInstance().getApiClient(session);
                StatusesService service = client.getStatusesService();

                Call<Tweet> call = service.update(status, null, null, null, null, null, null, null, null);
                call.enqueue(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        essentialService.hideEssentialDialog();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        exception.printStackTrace();
                        essentialService.hideEssentialDialog();
                    }
                });
            }
        });

        Button cancel = essentialView.findViewById(R.id.button_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                essentialService.hideEssentialDialog();
            }
        });

    }


}
