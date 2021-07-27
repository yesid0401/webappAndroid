package com.tissini.webview.webViewHelpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.webkit.JavascriptInterface;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import static com.tissini.webview.helpers.Functions.NotificationUpdate;
import static com.tissini.webview.helpers.Functions.downloadImage;
import static com.tissini.webview.helpers.Functions.getBitmapFromURL;
import static com.tissini.webview.helpers.Functions.goToThePlayStore;
import static com.tissini.webview.helpers.Functions.shareImage;

public class  WebAppInterface {

    Context mContext;

    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void shareApi( String title,String body) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        mContext.startActivity(Intent.createChooser(intent, title));
    }

    @JavascriptInterface
    public void shareWhatsApp(String number,String body) throws UnsupportedEncodingException {
        number = "+"+number;
        String url = "https://api.whatsapp.com/send?phone="+ number +"&text=" + URLEncoder.encode(body, "UTF-8");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setPackage("com.whatsapp");
        i.setData(Uri.parse(url));
        mContext.startActivity(i);
    }

    @JavascriptInterface
    public void optionImage(String productName,String productURL,String imageName, String imageURL,String action) throws IOException {

        if(action.equals("share")){
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Bitmap img = getBitmapFromURL(imageURL);
            shareImage(img,imageName,productName,productURL,mContext);
        }

        if(action.equals("download")){
            downloadImage(imageURL,imageName,mContext);
        }

    }

    @JavascriptInterface
    public void  updateApp(){
        NotificationUpdate(mContext);
        goToThePlayStore(mContext);
    }






}
