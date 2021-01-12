package com.emcrp.webview;

import com.emcrp.webview.ICallbackFromMainToWeb;

interface IWebToMain {
      void handleWebAction(String actionName, String jsonParams, in ICallbackFromMainToWeb callback);
}
