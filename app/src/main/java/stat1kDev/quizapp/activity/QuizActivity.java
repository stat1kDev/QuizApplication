package stat1kDev.quizapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import stat1kDev.quizapp.R;
import stat1kDev.quizapp.adapters.QuizAdapter;
import stat1kDev.quizapp.constants.AppConstants;
import stat1kDev.quizapp.data.preference.AppPreference;
import stat1kDev.quizapp.listeners.ListitemClickListener;
import stat1kDev.quizapp.models.quiz.quiz.QuizModel;
import stat1kDev.quizapp.models.quiz.quiz.ResultModel;
import stat1kDev.quizapp.utilities.ActivityUtilities;
import stat1kDev.quizapp.utilities.AdsUtilities;
import stat1kDev.quizapp.utilities.BeatBox;
import stat1kDev.quizapp.utilities.DialogUtilities;
import stat1kDev.quizapp.utilities.SoundUtilities;

public class QuizActivity extends BaseActivity implements RewardedVideoAdListener, DialogUtilities.OnCompleteListener {

    private Activity mActivity;
    private Context mContext;
    private ImageButton btnSpeaker;
    private Button btnNext;
    private RecyclerView mRecyclerQuiz;
    private TextView tvQuestionText;
    private TextView tvQuestionTitle;
    private ImageView imgFirstLife, imgSecondLife, imgThirdLife, imgFourthLife, imgFifthLife;

    private QuizAdapter mAdapter = null;
    private List<QuizModel> mItemList;
    ArrayList<String> mOptionList;
    ArrayList<String> mBackgroundColorList;

    private int mQuestionPosition = 0;
    private int mQuestionsCount = 0;
    private int mScore = 0, mWrongAns = 0, mSkip = 0;
    private int mLifeCounter = 5;
    private boolean mUserHasPressed = false;
    private boolean mIsSkipped = false, mIsCorrect = false;
    private String mQuestionText, mGivenAnsText, mCorrectAnsText, mCategoryId;
    private ArrayList<ResultModel> mResultList;
    private RewardedVideoAd mRewardedVideoAd;

    private BeatBox mBeatBox;
    private List<SoundUtilities> mSounds;
    private boolean isSoundOn;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeRewardedAds();
        loadRewardedVideoAds();

        initVar();
        initView();
        loadData();
        initListener();
    }

    public void initializeRewardedAds() {
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.app_ad_id));

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
    }

    private void loadRewardedVideoAds() {
        mRewardedVideoAd.loadAd(getResources().getString(R.string.rewarded_ad_unit_id),
                new AdRequest.Builder().build());
    }


    private void initVar() {
        mActivity = QuizActivity.this;
        mContext = mActivity.getApplicationContext();

        Intent intent = getIntent();
        if (intent != null) {
            mCategoryId = intent.getStringExtra(AppConstants.BUNDLE_KEY_INDEX);
        }

        mItemList = new ArrayList<>();
        mOptionList = new ArrayList<>();
        mBackgroundColorList = new ArrayList<>();
        mResultList = new ArrayList<>();

        mBeatBox = new BeatBox(mActivity);
        mSounds = mBeatBox.getSounds();
    }

    private void initView() {
        setContentView(R.layout.activity_quiz);

        imgFirstLife = (ImageView) findViewById(R.id.firstLife);
        imgSecondLife = (ImageView) findViewById(R.id.secondLife);
        imgThirdLife = (ImageView) findViewById(R.id.thirdLife);
        imgFourthLife = (ImageView) findViewById(R.id.fourthLife);
        imgFifthLife = (ImageView) findViewById(R.id.fifthLife);
        btnSpeaker = (ImageButton) findViewById(R.id.btnSpeaker);
        btnNext = (Button) findViewById(R.id.btnNext);

        tvQuestionText = (TextView) findViewById(R.id.tvQuestionText);
        tvQuestionTitle = (TextView) findViewById(R.id.tvQuestionTitle);

        mRecyclerQuiz = (RecyclerView) findViewById(R.id.rvQuiz);
        mRecyclerQuiz.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new QuizAdapter(mContext, mActivity, mOptionList, mBackgroundColorList);
        mRecyclerQuiz.setAdapter(mAdapter);

        ininToolbar(true);
        setToolbarTitle(getString(R.string.quiz));
        enableUpButton();
        initLoader();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void loadData() {
        showLoader();

        isSoundOn = AppPreference.getInstance(mActivity).getBoolean(AppConstants.KEY_SOUND, true);
        setSpeakerImage();

        loadJson();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setSpeakerImage() {
        if (isSoundOn) {
            btnSpeaker.setImageResource(R.drawable.ic_speaker);
        } else {
            btnSpeaker.setImageResource(R.drawable.ic_speaker_not);
        }
    }


    public void initListener() {
        btnSpeaker.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                isSoundOn = !isSoundOn;
                AppPreference.getInstance(mActivity).setBoolean(AppConstants.KEY_SOUND, isSoundOn);
                setSpeakerImage();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mUserHasPressed) {
                    FragmentManager manager = getSupportFragmentManager();
                    DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.skip_text), getString(R.string.skip_prompt), getString(R.string.yes), getString(R.string.no), AppConstants.BUNDLE_KEY_SKIP_OPTION);
                    dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
                } else {
                    updateResultSet();
                    setNextQuestion();
                }
            }
        });

        mAdapter.setItemClickListener(new ListitemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (!mUserHasPressed) {
                    int clickedAnswerIndex = position;
                    if (mItemList.get(mQuestionPosition).getCorrectAnswer() != -1) {
                        for (int currentItemIndex = 0; currentItemIndex < mOptionList.size(); currentItemIndex++) {
                            if (currentItemIndex == clickedAnswerIndex && currentItemIndex == mItemList.get(mQuestionPosition).getCorrectAnswer()) {
                                mBackgroundColorList.set(currentItemIndex, AppConstants.COLOR_GREEN);
                                mScore++;
                                mIsCorrect = true;
                                if (isSoundOn) {
                                    mBeatBox.play(mSounds.get(AppConstants.BUNDLE_KEY_ZERO_INDEX));
                                }
                            } else if (currentItemIndex == clickedAnswerIndex && !(currentItemIndex == mItemList.get(mQuestionPosition).getCorrectAnswer())) {
                                mBackgroundColorList.set(currentItemIndex, AppConstants.COLOR_RED);
                                mWrongAns++;
                                if (isSoundOn) {
                                    mBeatBox.play(mSounds.get(AppConstants.BUNDLE_KEY_SECOND_INDEX));
                                }
                                decreaseLifeAndStatus();
                            } else if (currentItemIndex == mItemList.get(mQuestionPosition).getCorrectAnswer()) {
                                mBackgroundColorList.set(currentItemIndex, AppConstants.COLOR_GREEN);
                                ((LinearLayoutManager) mRecyclerQuiz.getLayoutManager()).scrollToPosition(currentItemIndex);
                            }
                        }
                    } else {
                        mBackgroundColorList.set(clickedAnswerIndex, AppConstants.COLOR_GREEN);
                        mScore++;
                        mIsCorrect = true;
                        mBeatBox.play(mSounds.get(AppConstants.BUNDLE_KEY_ZERO_INDEX));
                    }

                    mGivenAnsText = mItemList.get(mQuestionPosition).getAnswers().get(clickedAnswerIndex);
                    mCorrectAnsText = mItemList.get(mQuestionPosition).getAnswers().get(mItemList.get(mQuestionPosition).getCorrectAnswer());

                    mUserHasPressed = true;
                    mAdapter.notifyDataSetChanged();
                }
            }

        });

    }


    public void decreaseLifeAndStatus() {
        mLifeCounter--;
        setLifeStatus();
    }

    void increaseLifeAndStatus() {
        if (mLifeCounter < AppConstants.BUNDLE_KEY_MAX_LIFE) {
            mLifeCounter++;
            setLifeStatus();
        }
    }

    public void setLifeStatus() {
        switch (mLifeCounter) {
            case 1:
                imgFirstLife.setVisibility(View.VISIBLE);
                imgSecondLife.setVisibility(View.GONE);
                imgThirdLife.setVisibility(View.GONE);
                imgFourthLife.setVisibility(View.GONE);
                imgFifthLife.setVisibility(View.GONE);
                break;
            case 2:
                imgFirstLife.setVisibility(View.VISIBLE);
                imgSecondLife.setVisibility(View.VISIBLE);
                imgThirdLife.setVisibility(View.GONE);
                imgFourthLife.setVisibility(View.GONE);
                imgFifthLife.setVisibility(View.GONE);
                break;
            case 3:
                imgFirstLife.setVisibility(View.VISIBLE);
                imgSecondLife.setVisibility(View.VISIBLE);
                imgThirdLife.setVisibility(View.VISIBLE);
                imgFourthLife.setVisibility(View.GONE);
                imgFifthLife.setVisibility(View.GONE);
                break;
            case 4:
                imgFirstLife.setVisibility(View.VISIBLE);
                imgSecondLife.setVisibility(View.VISIBLE);
                imgThirdLife.setVisibility(View.VISIBLE);
                imgFourthLife.setVisibility(View.VISIBLE);
                imgFifthLife.setVisibility(View.GONE);
                break;
            case 5:
                imgFirstLife.setVisibility(View.VISIBLE);
                imgSecondLife.setVisibility(View.VISIBLE);
                imgThirdLife.setVisibility(View.VISIBLE);
                imgFourthLife.setVisibility(View.VISIBLE);
                imgFifthLife.setVisibility(View.VISIBLE);
                break;
            default:
                imgFirstLife.setVisibility(View.GONE);
                imgSecondLife.setVisibility(View.GONE);
                imgThirdLife.setVisibility(View.GONE);
                imgFourthLife.setVisibility(View.GONE);
                imgFifthLife.setVisibility(View.GONE);
                break;
        }
    }



    public void setNextQuestion() {
        if (isSoundOn) {
            mBeatBox.play(mSounds.get(AppConstants.BUNDLE_KEY_FIRST_INDEX));
        }
        mUserHasPressed = false;
        if (mQuestionPosition < mItemList.size() - 1 && mLifeCounter > 0) {
            mQuestionPosition++;
            updateQuestionsAndAnswers();
        } else if (mQuestionPosition < mItemList.size() - 1 && mLifeCounter == 0) {
            FragmentManager manager = getSupportFragmentManager();
            DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.reward_dialog_title), getString(R.string.reward_dialog_message), getString(R.string.yes), getString(R.string.no), AppConstants.BUNDLE_KEY_REWARD_OPTION);
            dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
        } else {
            ActivityUtilities.getInstance().invokeScoreCardActivity(mActivity, ScoreCardActivity.class, mQuestionsCount, mScore, mWrongAns, mSkip, mCategoryId, mResultList, true);
            AppPreference.getInstance(mActivity).setQuizResult(mCategoryId, mScore);
        }
    }

    public void updateQuestionsAndAnswers() {
        mOptionList.clear();
        mBackgroundColorList.clear();
        ((LinearLayoutManager) mRecyclerQuiz.getLayoutManager()).scrollToPosition(AppConstants.BUNDLE_KEY_ZERO_INDEX);

        mOptionList.addAll(mItemList.get(mQuestionPosition).getAnswers());
        mBackgroundColorList.addAll(mItemList.get(mQuestionPosition).getBackgroundColors());
        mAdapter.notifyDataSetChanged();

        mQuestionText = mItemList.get(mQuestionPosition).getQuestion();

        tvQuestionText.setText(Html.fromHtml(mQuestionText));
        tvQuestionTitle.setText(getString(R.string.quiz_question_title, mQuestionPosition + 1, mQuestionsCount));
    }

    public void quizActivityClosePrompt() {
        FragmentManager manager = getSupportFragmentManager();
        DialogUtilities dialog = DialogUtilities.newInstance(getString(R.string.exit), getString(R.string.cancel_prompt), getString(R.string.yes), getString(R.string.no), AppConstants.BUNDLE_KEY_CLOSE_OPTION);
        dialog.show(manager, AppConstants.BUNDLE_KEY_DIALOG_FRAGMENT);
    }

    private void loadJson() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(AppConstants.QUESTION_FILE)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parseJson(sb.toString());
    }

    public void parseJson(String jsonData) {
        try {

            JSONObject jsonObjMain = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObjMain.getJSONArray(AppConstants.JSON_KEY_QUESTIONNAIRY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                String question = jsonObj.getString(AppConstants.JSON_KEY_QUESTION);
                int correctAnswer = Integer.parseInt(jsonObj.getString(AppConstants.JSON_KEY_CORRECT_ANS));
                String categoryId = jsonObj.getString(AppConstants.JSON_KEY_CATEGORY_ID);

                Log.d("TAG", categoryId.toString());

                JSONArray jsonArray2 = jsonObj.getJSONArray(AppConstants.JSON_KEY_ANSWERS);
                ArrayList<String> contents = new ArrayList<>();
                ArrayList<String> backgroundColors = new ArrayList<>();
                for (int j = 0; j < jsonArray2.length(); j++) {
                    String item_title = jsonArray2.get(j).toString();
                    contents.add(item_title);
                    backgroundColors.add(AppConstants.COLOR_WHITE);
                }
                if (mCategoryId.equals(categoryId)) {
                    mItemList.add(new QuizModel(question, contents, correctAnswer, categoryId, backgroundColors));
                }
            }

            mQuestionsCount = mItemList.size();
            Collections.shuffle(mItemList);

            hideLoader();
            updateQuestionsAndAnswers();

        } catch (JSONException e) {
            e.printStackTrace();
            showEmptyView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                quizActivityClosePrompt();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        quizActivityClosePrompt();
    }

    @Override
    public void onComplete(Boolean isOkPressed, String viewIdText) {
        if (isOkPressed) {
            if (viewIdText.equals(AppConstants.BUNDLE_KEY_CLOSE_OPTION)) {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
            } else if (viewIdText.equals(AppConstants.BUNDLE_KEY_SKIP_OPTION)) {
                mSkip++;
                mIsSkipped = true;
                mGivenAnsText = getResources().getString(R.string.skipped_text);
                mCorrectAnsText = mItemList.get(mQuestionPosition).getAnswers().get(mItemList.get(mQuestionPosition).getCorrectAnswer());
                updateResultSet();
                setNextQuestion();
            } else if (viewIdText.equals(AppConstants.BUNDLE_KEY_REWARD_OPTION)) {
                mRewardedVideoAd.show();
            }
        } else if (!isOkPressed && viewIdText.equals(AppConstants.BUNDLE_KEY_REWARD_OPTION)) {
            ActivityUtilities.getInstance().invokeScoreCardActivity(mActivity, ScoreCardActivity.class, mQuestionsCount, mScore, mWrongAns, mSkip, mCategoryId, mResultList, true);
            AppPreference.getInstance(mContext).setQuizResult(mCategoryId, mScore);
            AppPreference.getInstance(mContext).setQuizQuestionsCount(mCategoryId, mQuestionsCount);
        }
    }

    public void updateResultSet() {
        mResultList.add(new ResultModel(mQuestionText, mGivenAnsText, mCorrectAnsText, mIsCorrect, mIsSkipped));
        mIsCorrect = false;
        mIsSkipped = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);

        // load full screen ad
        AdsUtilities.getInstance(mContext).loadFullScreenAd(mActivity);

        super.onResume();
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAds();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        increaseLifeAndStatus();
        updateResultSet();
        setNextQuestion();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}