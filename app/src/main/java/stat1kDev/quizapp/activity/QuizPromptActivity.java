package stat1kDev.quizapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import stat1kDev.quizapp.R;
import stat1kDev.quizapp.constants.AppConstants;
import stat1kDev.quizapp.data.preference.AppPreference;
import stat1kDev.quizapp.utilities.ActivityUtilities;
import stat1kDev.quizapp.utilities.AdsUtilities;

public class QuizPromptActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;
    private Button mBtnYes, mBtnNo;
    private TextView firstText, thirdtext;
    private String categoryId, score, questionsCount;
    //private String questionsCount = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initListener();
    }

    private void initVar() {
        mActivity = QuizPromptActivity.this;
        mContext = mActivity.getApplicationContext();

        Intent intent = getIntent();
        if (intent != null) {
            categoryId = intent.getStringExtra(AppConstants.BUNDLE_KEY_INDEX);
            score = AppPreference.getInstance(mContext).getString(categoryId);
            questionsCount = AppPreference.getInstance(mContext).getString(categoryId + AppConstants.QUESTIONS_IN_TEST);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_quiz_prompt);

        mBtnYes = (Button) findViewById(R.id.btn_yes);
        mBtnNo = (Button) findViewById(R.id.btn_no);

        firstText = (TextView) findViewById(R.id.first_text);
        thirdtext = (TextView) findViewById(R.id.third_text);

        if (score != null && questionsCount != null) {
            firstText.setText(getString(R.string.quiz_promt_first_text, score, questionsCount));
            thirdtext.setText(R.string.quiz_promt_third_text);
        }

        ininToolbar(true);
        setToolbarTitle(getString(R.string.quiz_prompt));
        enableUpButton();

        // show full-screen ads
        AdsUtilities.getInstance(mContext).showFullScreenAd();
        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));

    }


    private void initListener() {
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtilities.getInstance().invokeCommonQuizActivity(mActivity, QuizActivity.class, categoryId, true);
            }
        });
        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
    }



}