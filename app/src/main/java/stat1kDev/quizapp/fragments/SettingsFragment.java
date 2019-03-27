package stat1kDev.quizapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import stat1kDev.quizapp.R;
import stat1kDev.quizapp.constants.AppConstants;

public class SettingsFragment extends PreferenceFragment implements BillingProcessor.IBillingHandler {

    BillingProcessor bp;
    Preference preferencepurchase;
    Preference preferencesubscribe;

    AlertDialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_preference);

        String license = getResources().getString(R.string.google_play_license);
        bp = new BillingProcessor(getActivity(), license, this);
        bp.loadOwnedPurchasesFromGoogle();



        preferencesubscribe = findPreference("subscribe");

        if (null != license && !license.equals("")){

            preferencesubscribe
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            bp.subscribe(getActivity(), SUBSCRIPTION_ID());
                            return true;
                        }
                    });

            if (getIsSubscribe(getActivity())){
                preferencesubscribe.setIcon(R.drawable.ic_action_action_done);
            }
        }

        preferencepurchase = findPreference("purchase");

        if (null != license && !license.equals("")){


            preferencepurchase
                    .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            bp.purchase(getActivity(), PRODUCT_ID());
                            return true;
                        }
                    });

            if (getIsPurchased(getActivity())){
                preferencepurchase.setIcon(R.drawable.ic_action_action_done);
                preferencesubscribe.setEnabled(false);
                preferencesubscribe.setSelectable(false);
            }
        }



    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        if (productId.equals(PRODUCT_ID())){
            setIsPurchased(true, getActivity());
            preferencepurchase.setIcon(R.drawable.ic_action_action_done);
            Toast.makeText(getActivity(), getResources().getString(R.string.settings_purchase_success), Toast.LENGTH_LONG).show();
            Log.v("TAG", "Purchase purchased");
        }

        if (productId.equals(SUBSCRIPTION_ID())){
            setIsSubscribe(true, getActivity());
            preferencesubscribe.setIcon(R.drawable.ic_action_action_done);
            Toast.makeText(getActivity(), getResources().getString(R.string.settings_subscribe_success), Toast.LENGTH_LONG).show();
            Log.v("TAG", "Subscribe purchased");
        } else {
            setIsSubscribe(false, getActivity());
            preferencesubscribe.getIcon().setVisible(false, true);
        }
        Log.v("TAG", "Purchase or subscribe purchased");
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(getActivity(), getResources().getString(R.string.settings_purchase_fail), Toast.LENGTH_LONG).show();
        Log.v("TAG", "Error");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        if (bp.isPurchased(PRODUCT_ID())) {
            setIsPurchased(true, getActivity());
            Log.v("TAG", "Purchase actually restored");
            preferencepurchase.setIcon(R.drawable.ic_action_action_done);
            if (dialog != null) dialog.cancel();
            Toast.makeText(getActivity(), getResources().getString(R.string.settings_restore_purchase_success), Toast.LENGTH_LONG).show();
        }

        if (bp.isSubscribed(SUBSCRIPTION_ID())) {
            setIsSubscribe(true, getActivity());
            Log.v("TAG", "Subscribe actually restored");
            preferencesubscribe.setIcon(R.drawable.ic_action_action_done);
            if (dialog != null) dialog.cancel();
        } else {
            setIsSubscribe(false, getActivity());
            preferencesubscribe.getIcon().setVisible(false, true);
        }
        Log.v("TAG", "Purchase and subscription restored called");
    }



    public void setIsPurchased(boolean purchased, Context c){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(c);

        SharedPreferences.Editor editor= prefs.edit();

        editor.putBoolean(AppConstants.PRODUCT_ID_BOUGHT, purchased);
        editor.apply();
    }

    public void setIsSubscribe(boolean purchased, Context c){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(c);

        SharedPreferences.Editor editor= prefs.edit();

        editor.putBoolean(AppConstants.PRODUCT_ID_SUBSCRIBE, purchased);
        editor.apply();
    }

    public static boolean getIsPurchased(Context c){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(c);

        return prefs.getBoolean(AppConstants.PRODUCT_ID_BOUGHT, false);
    }

    public static boolean getIsSubscribe(Context c){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(c);

        return prefs.getBoolean(AppConstants.PRODUCT_ID_SUBSCRIBE, false);
    }

    private String PRODUCT_ID(){
        return getResources().getString(R.string.product_id);
    }

    private String SUBSCRIPTION_ID(){
        return getResources().getString(R.string.subscription_id);
    }


    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        bp.handleActivityResult(requestCode, resultCode, intent);
    }


    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }
}