package com.adnaga;

import org.apache.cordova.*;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

public class AdnagaAdmob implements IPlugin {
    private static final String LOG_TAG = "Adnaga-Admob";

    private com.google.android.gms.ads.InterstitialAd _admobInterstitialAd;
    private Adnaga _adnaga;

    public String getNetworkName() {
        return "admob";
    }

    public void init(String pid, Adnaga adnaga) {
        _adnaga = adnaga;
        Log.i(LOG_TAG, "admaga-admob inited");
    }

    public void loadAds(final String pid) {
        final Activity activity = _adnaga.getActivity();
        activity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  Log.i(LOG_TAG, "Trying to load admob ads, pid=" + pid);
                  _admobInterstitialAd = new com.google.android.gms.ads.InterstitialAd(activity);
                  _admobInterstitialAd.setAdUnitId(pid);
                  AdRequest adRequest = new AdRequest.Builder().build();
                  _admobInterstitialAd.setAdListener(new AdmobAdListener());
                  _admobInterstitialAd.loadAd(adRequest);
              }
          });
    }

    public void showAds(final CallbackContext callbackContext) {
        _adnaga.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(LOG_TAG, "Trying to show admob ads");
                if (_admobInterstitialAd != null) {
                    _admobInterstitialAd.show();
                } else {
                    Log.e(LOG_TAG, "abmob interstitial not ready, cannot show");
                    PluginResult result = new PluginResult(PluginResult.Status.ERROR, "admob interstitial not ready, cannot show");
                    callbackContext.sendPluginResult(result);
                }
            }
        });
    }

    public void onPause() {

    }

    public void onResume() {

    }

    private class AdmobAdListener extends AdListener {
        @Override
        public void onAdClosed() {
            _adnaga.sendAdsEventToJs("admob", "FINISH", "");
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            _adnaga.sendAdsEventToJs("admob", "LOADERROR", String.valueOf(errorCode));
        }

        @Override
        public void onAdLeftApplication() {
            _adnaga.sendAdsEventToJs("admob", "CLICK", "");
        }

        @Override
        public void onAdOpened() {
            _adnaga.sendAdsEventToJs("admob", "START", "");
        }

        @Override
        public void onAdLoaded() {
            _adnaga.sendAdsEventToJs("admob", "READY", "");
        }
    }
}