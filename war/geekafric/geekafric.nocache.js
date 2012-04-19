function geekafric(){
  var $intern_0 = '', $intern_35 = '" for "gwt:onLoadErrorFn"', $intern_33 = '" for "gwt:onPropertyErrorFn"', $intern_21 = '"><\/script>', $intern_10 = '#', $intern_43 = '&', $intern_73 = '.cache.html', $intern_12 = '/', $intern_65 = '0EE504B8EED7703DC653D4626716D80D', $intern_67 = '17B360508D050EB532526B1F8AA95A72', $intern_68 = '315CD0B395315F7B0908874B5A4A33C1', $intern_69 = '57BD13CA68C7AE03D39AC9CEFD643E94', $intern_70 = '98E9D38963CB81E5AAB53CB9932551E9', $intern_72 = ':', $intern_27 = '::', $intern_81 = '<script defer="defer">geekafric.onInjectionDone(\'geekafric\')<\/script>', $intern_20 = '<script id="', $intern_30 = '=', $intern_11 = '?', $intern_51 = 'ActiveXObject', $intern_32 = 'Bad handler "', $intern_52 = 'ChromeTab.ChromeFrame', $intern_71 = 'D11495BC26E1C99E443B107577B99417', $intern_80 = 'DOMContentLoaded', $intern_22 = 'SCRIPT', $intern_46 = 'Unexpected exception in locale detection, using default: ', $intern_45 = '_', $intern_44 = '__gwt_Locale', $intern_19 = '__gwt_marker_geekafric', $intern_23 = 'base', $intern_15 = 'baseUrl', $intern_4 = 'begin', $intern_3 = 'bootstrap', $intern_50 = 'chromeframe', $intern_14 = 'clear.cache.gif', $intern_29 = 'content', $intern_41 = 'default', $intern_66 = 'en', $intern_9 = 'end', $intern_64 = 'fr', $intern_58 = 'gecko', $intern_59 = 'gecko1_8', $intern_1 = 'geekafric', $intern_17 = 'geekafric.nocache.js', $intern_26 = 'geekafric::', $intern_5 = 'gwt.codesvr=', $intern_6 = 'gwt.hosted=', $intern_7 = 'gwt.hybrid', $intern_74 = 'gwt/standard/standard.css', $intern_34 = 'gwt:onLoadErrorFn', $intern_31 = 'gwt:onPropertyErrorFn', $intern_28 = 'gwt:property', $intern_79 = 'head', $intern_62 = 'hosted.html?geekafric', $intern_78 = 'href', $intern_57 = 'ie6', $intern_56 = 'ie8', $intern_55 = 'ie9', $intern_36 = 'iframe', $intern_13 = 'img', $intern_37 = "javascript:''", $intern_75 = 'link', $intern_61 = 'loadExternalRefs', $intern_40 = 'locale', $intern_42 = 'locale=', $intern_24 = 'meta', $intern_39 = 'moduleRequested', $intern_8 = 'moduleStartup', $intern_54 = 'msie', $intern_25 = 'name', $intern_48 = 'opera', $intern_38 = 'position:absolute;width:0;height:0;border:none', $intern_76 = 'rel', $intern_53 = 'safari', $intern_16 = 'script', $intern_63 = 'selectingPermutation', $intern_2 = 'startup', $intern_77 = 'stylesheet', $intern_18 = 'undefined', $intern_60 = 'unknown', $intern_47 = 'user.agent', $intern_49 = 'webkit';
  var $wnd = window, $doc = document, $stats = $wnd.__gwtStatsEvent?function(a){
    return $wnd.__gwtStatsEvent(a);
  }
  :null, $sessionId = $wnd.__gwtStatsSessionId?$wnd.__gwtStatsSessionId:null, scriptsDone, loadDone, bodyDone, base = $intern_0, metaProps = {}, values = [], providers = [], answers = [], softPermutationId = 0, onLoadErrorFunc, propertyErrorFunc;
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_4});
  if (!$wnd.__gwt_stylesLoaded) {
    $wnd.__gwt_stylesLoaded = {};
  }
  if (!$wnd.__gwt_scriptsLoaded) {
    $wnd.__gwt_scriptsLoaded = {};
  }
  function isHostedMode(){
    var result = false;
    try {
      var query = $wnd.location.search;
      return (query.indexOf($intern_5) != -1 || (query.indexOf($intern_6) != -1 || $wnd.external && $wnd.external.gwtOnLoad)) && query.indexOf($intern_7) == -1;
    }
     catch (e) {
    }
    isHostedMode = function(){
      return result;
    }
    ;
    return result;
  }

  function maybeStartModule(){
    if (scriptsDone && loadDone) {
      var iframe = $doc.getElementById($intern_1);
      var frameWnd = iframe.contentWindow;
      if (isHostedMode()) {
        frameWnd.__gwt_getProperty = function(name){
          return computePropValue(name);
        }
        ;
      }
      geekafric = null;
      frameWnd.gwtOnLoad(onLoadErrorFunc, $intern_1, base, softPermutationId);
      $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_8, millis:(new Date).getTime(), type:$intern_9});
    }
  }

  function computeScriptBase(){
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf($intern_10);
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf($intern_11);
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf($intern_12, Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):$intern_0;
    }

    function ensureAbsoluteUrl(url){
      if (url.match(/^\w+:\/\//)) {
      }
       else {
        var img = $doc.createElement($intern_13);
        img.src = url + $intern_14;
        url = getDirectoryOfFile(img.src);
      }
      return url;
    }

    function tryMetaTag(){
      var metaVal = __gwt_getMetaProperty($intern_15);
      if (metaVal != null) {
        return metaVal;
      }
      return $intern_0;
    }

    function tryNocacheJsTag(){
      var scriptTags = $doc.getElementsByTagName($intern_16);
      for (var i = 0; i < scriptTags.length; ++i) {
        if (scriptTags[i].src.indexOf($intern_17) != -1) {
          return getDirectoryOfFile(scriptTags[i].src);
        }
      }
      return $intern_0;
    }

    function tryMarkerScript(){
      var thisScript;
      if (typeof isBodyLoaded == $intern_18 || !isBodyLoaded()) {
        var markerId = $intern_19;
        var markerScript;
        $doc.write($intern_20 + markerId + $intern_21);
        markerScript = $doc.getElementById(markerId);
        thisScript = markerScript && markerScript.previousSibling;
        while (thisScript && thisScript.tagName != $intern_22) {
          thisScript = thisScript.previousSibling;
        }
        if (markerScript) {
          markerScript.parentNode.removeChild(markerScript);
        }
        if (thisScript && thisScript.src) {
          return getDirectoryOfFile(thisScript.src);
        }
      }
      return $intern_0;
    }

    function tryBaseTag(){
      var baseElements = $doc.getElementsByTagName($intern_23);
      if (baseElements.length > 0) {
        return baseElements[baseElements.length - 1].href;
      }
      return $intern_0;
    }

    var tempBase = tryMetaTag();
    if (tempBase == $intern_0) {
      tempBase = tryNocacheJsTag();
    }
    if (tempBase == $intern_0) {
      tempBase = tryMarkerScript();
    }
    if (tempBase == $intern_0) {
      tempBase = tryBaseTag();
    }
    if (tempBase == $intern_0) {
      tempBase = getDirectoryOfFile($doc.location.href);
    }
    tempBase = ensureAbsoluteUrl(tempBase);
    base = tempBase;
    return tempBase;
  }

  function processMetas(){
    var metas = document.getElementsByTagName($intern_24);
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name = meta.getAttribute($intern_25), content;
      if (name) {
        name = name.replace($intern_26, $intern_0);
        if (name.indexOf($intern_27) >= 0) {
          continue;
        }
        if (name == $intern_28) {
          content = meta.getAttribute($intern_29);
          if (content) {
            var value, eq = content.indexOf($intern_30);
            if (eq >= 0) {
              name = content.substring(0, eq);
              value = content.substring(eq + 1);
            }
             else {
              name = content;
              value = $intern_0;
            }
            metaProps[name] = value;
          }
        }
         else if (name == $intern_31) {
          content = meta.getAttribute($intern_29);
          if (content) {
            try {
              propertyErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_32 + content + $intern_33);
            }
          }
        }
         else if (name == $intern_34) {
          content = meta.getAttribute($intern_29);
          if (content) {
            try {
              onLoadErrorFunc = eval(content);
            }
             catch (e) {
              alert($intern_32 + content + $intern_35);
            }
          }
        }
      }
    }
  }

  function __gwt_isKnownPropertyValue(propName, propValue){
    return propValue in values[propName];
  }

  function __gwt_getMetaProperty(name){
    var value = metaProps[name];
    return value == null?null:value;
  }

  function unflattenKeylistIntoAnswers(propValArray, value){
    var answer = answers;
    for (var i = 0, n = propValArray.length - 1; i < n; ++i) {
      answer = answer[propValArray[i]] || (answer[propValArray[i]] = []);
    }
    answer[propValArray[n]] = value;
  }

  function computePropValue(propName){
    var value = providers[propName](), allowedValuesMap = values[propName];
    if (value in allowedValuesMap) {
      return value;
    }
    var allowedValuesList = [];
    for (var k in allowedValuesMap) {
      allowedValuesList[allowedValuesMap[k]] = k;
    }
    if (propertyErrorFunc) {
      propertyErrorFunc(propName, allowedValuesList, value);
    }
    throw null;
  }

  var frameInjected;
  function maybeInjectFrame(){
    if (!frameInjected) {
      frameInjected = true;
      var iframe = $doc.createElement($intern_36);
      iframe.src = $intern_37;
      iframe.id = $intern_1;
      iframe.style.cssText = $intern_38;
      iframe.tabIndex = -1;
      $doc.body.appendChild(iframe);
      $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_8, millis:(new Date).getTime(), type:$intern_39});
      iframe.contentWindow.location.replace(base + initialHtml);
    }
  }

  providers[$intern_40] = function(){
    var locale = null;
    var rtlocale = $intern_41;
    try {
      if (!locale) {
        var queryParam = location.search;
        var qpStart = queryParam.indexOf($intern_42);
        if (qpStart >= 0) {
          var value = queryParam.substring(qpStart + 7);
          var end = queryParam.indexOf($intern_43, qpStart);
          if (end < 0) {
            end = queryParam.length;
          }
          locale = queryParam.substring(qpStart + 7, end);
        }
      }
      if (!locale) {
        locale = __gwt_getMetaProperty($intern_40);
      }
      if (!locale) {
        locale = $wnd[$intern_44];
      }
      if (locale) {
        rtlocale = locale;
      }
      while (locale && !__gwt_isKnownPropertyValue($intern_40, locale)) {
        var lastIndex = locale.lastIndexOf($intern_45);
        if (lastIndex < 0) {
          locale = null;
          break;
        }
        locale = locale.substring(0, lastIndex);
      }
    }
     catch (e) {
      alert($intern_46 + e);
    }
    $wnd[$intern_44] = rtlocale;
    return locale || $intern_41;
  }
  ;
  values[$intern_40] = {'default':0, en:1, fr:2};
  providers[$intern_47] = function(){
    var ua = navigator.userAgent.toLowerCase();
    var makeVersion = function(result){
      return parseInt(result[1]) * 1000 + parseInt(result[2]);
    }
    ;
    if (function(){
      return ua.indexOf($intern_48) != -1;
    }
    ())
      return $intern_48;
    if (function(){
      return ua.indexOf($intern_49) != -1 || function(){
        if (ua.indexOf($intern_50) != -1) {
          return true;
        }
        if (typeof window[$intern_51] != $intern_18) {
          try {
            var obj = new ActiveXObject($intern_52);
            if (obj) {
              obj.registerBhoIfNeeded();
              return true;
            }
          }
           catch (e) {
          }
        }
        return false;
      }
      ();
    }
    ())
      return $intern_53;
    if (function(){
      return ua.indexOf($intern_54) != -1 && $doc.documentMode >= 9;
    }
    ())
      return $intern_55;
    if (function(){
      return ua.indexOf($intern_54) != -1 && $doc.documentMode >= 8;
    }
    ())
      return $intern_56;
    if (function(){
      var result = /msie ([0-9]+)\.([0-9]+)/.exec(ua);
      if (result && result.length == 3)
        return makeVersion(result) >= 6000;
    }
    ())
      return $intern_57;
    if (function(){
      return ua.indexOf($intern_58) != -1;
    }
    ())
      return $intern_59;
    return $intern_60;
  }
  ;
  values[$intern_47] = {gecko1_8:0, ie6:1, ie8:2, ie9:3, opera:4, safari:5};
  geekafric.onScriptLoad = function(){
    if (frameInjected) {
      loadDone = true;
      maybeStartModule();
    }
  }
  ;
  geekafric.onInjectionDone = function(){
    scriptsDone = true;
    $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_61, millis:(new Date).getTime(), type:$intern_9});
    maybeStartModule();
  }
  ;
  processMetas();
  computeScriptBase();
  var strongName;
  var initialHtml;
  if (isHostedMode()) {
    if ($wnd.external && ($wnd.external.initModule && $wnd.external.initModule($intern_1))) {
      $wnd.location.reload();
      return;
    }
    initialHtml = $intern_62;
    strongName = $intern_0;
  }
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_63});
  if (!isHostedMode()) {
    try {
      unflattenKeylistIntoAnswers([$intern_64, $intern_53], $intern_65);
      unflattenKeylistIntoAnswers([$intern_66, $intern_53], $intern_67);
      unflattenKeylistIntoAnswers([$intern_41, $intern_53], $intern_68);
      unflattenKeylistIntoAnswers([$intern_64, $intern_59], $intern_69);
      unflattenKeylistIntoAnswers([$intern_41, $intern_59], $intern_70);
      unflattenKeylistIntoAnswers([$intern_66, $intern_59], $intern_71);
      strongName = answers[computePropValue($intern_40)][computePropValue($intern_47)];
      var idx = strongName.indexOf($intern_72);
      if (idx != -1) {
        softPermutationId = Number(strongName.substring(idx + 1));
        strongName = strongName.substring(0, idx);
      }
      initialHtml = strongName + $intern_73;
    }
     catch (e) {
      return;
    }
  }
  var onBodyDoneTimerId;
  function onBodyDone(){
    if (!bodyDone) {
      bodyDone = true;
      if (!__gwt_stylesLoaded[$intern_74]) {
        var l = $doc.createElement($intern_75);
        __gwt_stylesLoaded[$intern_74] = l;
        l.setAttribute($intern_76, $intern_77);
        l.setAttribute($intern_78, base + $intern_74);
        $doc.getElementsByTagName($intern_79)[0].appendChild(l);
      }
      maybeStartModule();
      if ($doc.removeEventListener) {
        $doc.removeEventListener($intern_80, onBodyDone, false);
      }
      if (onBodyDoneTimerId) {
        clearInterval(onBodyDoneTimerId);
      }
    }
  }

  if ($doc.addEventListener) {
    $doc.addEventListener($intern_80, function(){
      maybeInjectFrame();
      onBodyDone();
    }
    , false);
  }
  var onBodyDoneTimerId = setInterval(function(){
    if (/loaded|complete/.test($doc.readyState)) {
      maybeInjectFrame();
      onBodyDone();
    }
  }
  , 50);
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_3, millis:(new Date).getTime(), type:$intern_9});
  $stats && $stats({moduleName:$intern_1, sessionId:$sessionId, subSystem:$intern_2, evtGroup:$intern_61, millis:(new Date).getTime(), type:$intern_4});
  $doc.write($intern_81);
}

geekafric();
