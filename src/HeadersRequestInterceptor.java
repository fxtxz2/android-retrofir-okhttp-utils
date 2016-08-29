

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加header头的okhttp2拦截器,不支持gzip
 * Created by zyl on 16/8/17.
 */
public class HeadersRequestInterceptor implements Interceptor {
    private Map<String,String> headerMap;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();
        if (headerMap != null){
            for (Map.Entry<String, String> entry : headerMap.entrySet()){
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request compressedRequest = builder.build();
        return chain.proceed(compressedRequest);
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }
}
