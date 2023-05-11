package org.cordova.plugin;

import android.net.Uri;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaActivity;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amplitude.api.Amplitude;
import com.amplitude.api.AmplitudeClient;

public class AmplitudePlugin extends CordovaPlugin {

    private CallbackContext mConversionListener = null;
    private CallbackContext mAttributionDataListener = null;
    private Map<String, String> mAttributionData = null;
    private CallbackContext mInviteListener = null;
    private Uri intentURI = null;
    private Uri newIntentURI = null;
    private Activity c;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    /**
     * Called when the activity receives a new intent.
     */
    @Override
    public void onNewIntent(Intent intent) {
        cordova.getActivity().setIntent(intent);
    }

    @Override
    public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.d("Amplitude", "Executing...");
        if("initialize".equals(action))
        {
            return initSdk(args, callbackContext);
        }

        return false;
    }

    /**
     *
     * @param args
     * @param callbackContext
     */
    private boolean initSdk(final JSONArray args, final CallbackContext callbackContext) {

        try {
            CordovaActivity activity = (CordovaActivity)this.cordova.getActivity();
            String apiKey = null;
            AmplitudeClient client = Amplitude.getInstance();
            apiKey = args.getString(0);

            if(apiKey == null || apiKey.trim().equals("")){
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, "No API KEY found!"));
            } else {
                client.initialize(cordova.getActivity(), apiKey);
                client.enableForegroundTracking(cordova.getActivity().getApplication());
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
                client.logEvent("Application Started");
            }
        }
        catch (JSONException e){
            e.printStackTrace();
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, e.toString()));
        }

        return true;
    }

    private void sendPluginNoResult(CallbackContext callbackContext) {
        PluginResult pluginResult = new PluginResult(
                PluginResult.Status.NO_RESULT);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
    }
}
