package stat1kDev.quizapp.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import stat1kDev.quizapp.R;

public class AdsUtilities {

    private static AdsUtilities mAdsUtilities;

    private InterstitialAd mInterstitialAd;

    private boolean mDisableBannerAd = false, mDisableInterstitialAd = false;

    private AdsUtilities(Context context) {
        MobileAds.initialize(context, context.getResources().getString(R.string.app_ad_id));
    }

    public static AdsUtilities getInstance(Context context) {
        if (mAdsUtilities == null) {
            mAdsUtilities = new AdsUtilities(context);
        }
        return mAdsUtilities;
    }

    public void showBannerAd(final AdView mAdView) {
        if (mDisableBannerAd) {
            mAdView.setVisibility(View.GONE);
        } else {
            AdRequest adRequest = new AdRequest.Builder()
                    .tagForChildDirectedTreatment(true)
                    .build();
            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    mAdView.setVisibility(View.GONE);
                }
            });
        }
    }

    public void loadFullScreenAd(Activity activity) {
        if (!mDisableInterstitialAd) {
            mInterstitialAd = new InterstitialAd(activity);
            mInterstitialAd.setAdUnitId(activity.getResources().getString(R.string.interstitial_ad_unit_id));

            AdRequest adRequest = new AdRequest.Builder()
                    .tagForChildDirectedTreatment(true)
                    .build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    public boolean showFullScreenAd() {
        if (!mDisableInterstitialAd) {
            if (mInterstitialAd != null) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    return true;
                }
            }
        }
        return false;
    }

    public void disableBannerAd() {
        this.mDisableBannerAd = true;
    }

    public void disableInterstitialAd() {
        this.mDisableInterstitialAd = true;
    }


}