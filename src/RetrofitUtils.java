

import retrofit2.Retrofit;

/**
 * Retrofit工具类单例
 * Created by zyl on 16/8/17.
 */
public class RetrofitUtils {
    /**
     * retrofit对象
     */
    private Retrofit retrofit;

    private RetrofitUtils(){}
    private static class RetrofitUtilsHolder{
        private static final RetrofitUtils INSTANCE = new RetrofitUtils();
    }
    public static RetrofitUtils getInstance(){
        return RetrofitUtilsHolder.INSTANCE;
    }
    public void build(Retrofit.Builder builder){
        retrofit = builder.build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
