package com.tissini.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;
    Activity activity;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void shareApi( String title,String body) {

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);

        // (Optional) Here we're setting the title of the content
        sendIntent.putExtra(Intent.EXTRA_TITLE, title);

        // (Optional) Here we're passing a content URI to an image to be displayed
        //sendIntent.setData(appLinkData);

        sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Show the Sharesheet
        activity.startActivity(Intent.createChooser(sendIntent, null));
    }
}
