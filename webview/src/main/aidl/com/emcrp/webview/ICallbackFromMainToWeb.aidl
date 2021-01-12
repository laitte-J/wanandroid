package com.emcrp.webview;

interface ICallbackFromMainToWeb {
    void onResult(int responseCode, String actionName, String response);
}
