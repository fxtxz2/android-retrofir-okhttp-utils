

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 设置cookie
 * Created by zyl on 16/8/17.
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;

    private String keyName;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            // 设置WebView中的cookie
            final StringBuffer cookies3 = new StringBuffer();
            Observable.just(originalResponse.headers("Set-Cookie"))
                    .flatMap(new Func1<List<String>, Observable<String>>() {
                        @Override
                        public Observable<String> call(List<String> strings) {
                            return Observable.from(strings);
                        }
                    })
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            cookies3.deleteCharAt(cookies3.length() - 2);
                            String oldCookies = SharedPreferUtils.get(context, keyName, String.class);
                            HashMap<String, String> map = new HashMap<>();
                            if (!TextUtils.isEmpty(oldCookies)){
                                String[] pairs = oldCookies.split(";");
                                for (String pair : pairs) {
                                    String[] keyValue = pair.split("=");
                                    map.put(keyValue[0], keyValue[1]);
                                }
                            }
                            String newCookies = cookies3.toString().trim();
                            String[] pairs = newCookies.split(";");
                            for (String pair : pairs) {
                                String[] keyValue = pair.split("=");
                                String key = keyValue[0];
                                String oldValue = map.get(key);
                                String newValue = keyValue[1];
                                if (!newValue.equals(oldValue)){
                                    map.put(key, newValue);
                                }
                            }
                            Iterator it = map.entrySet().iterator();
                            cookies3.setLength(0);
                            while (it.hasNext()){
                                HashMap.Entry pair = (HashMap.Entry) it.next();
                                cookies3.append(pair.getKey()).append("=").append(pair.getValue()).append("; ");
                            }
                            cookies3.deleteCharAt(cookies3.length() - 2);
                            SharedPreferUtils.save(context, keyName, cookies3.toString().trim());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String s) {
                            cookies3.append(s).append("; ");
                        }
                    });

        }
        return originalResponse;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
