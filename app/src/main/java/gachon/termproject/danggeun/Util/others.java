package gachon.termproject.danggeun.Util;

import android.util.Patterns;

public class others {
    public static boolean isStorageUrl(String url){
        return Patterns.WEB_URL.matcher(url).matches();
    }
}
