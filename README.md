# android-retrofir-okhttp-utils
Retrofit，Okhttp，RxAndroid之间的初始化，这是我在使用Retrofit结合RxJava过程中遇到的一些问题和基础使用，把原来volley网络框架换掉了，因为使用Retrofit注解方式，有点贴近原来J2EE中的service层，具体参考[Retrofit官网](http://square.github.io/retrofit/)

# Gradle配置
```Gradle
compile 'io.reactivex:rxandroid:1.2.1'
compile 'io.reactivex:rxjava:1.1.8'
compile 'com.squareup.okhttp3:okhttp:3.4.1'
compile 'com.squareup.okhttp3:okhttp-android-support:3.4.1'
compile 'com.squareup.okhttp3:okhttp-urlconnection:3.4.1'
compile 'com.squareup.okio:okio:1.9.0'
compile 'com.squareup.retrofit2:retrofit:2.1.0'
// addCallAdapterFactory(RxJavaCallAdapterFactory.create())需要
compile 'com.squareup.retrofit2:adapter-rxjava:2.1.0'
// json 解析需要
compile 'com.squareup.retrofit2:converter-jackson:2.1.0'
```
这里涉及到[RxAndroid](https://github.com/ReactiveX/RxAndroid)，[okhttp3](http://square.github.io/okhttp/)和[retrofit2](http://square.github.io/retrofit/)的配置。

# 初始化代码
```Java
// 请求头设置
HeadersRequestInterceptor headersInterceptor = new HeadersRequestInterceptor();
headersInterceptor.setHeaderMap(headerMap);

// 保存响应头中的Set-Cookie
ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor();
receivedCookiesInterceptor.setContext(getApplicationContext());
receivedCookiesInterceptor.setKeyName(BaseString.SET_COOKIE_KEY);


// okhttp的Cookie管理
CookieHandler cookieHandler = new CookieManager(
        new PersistentCookieStore(getApplicationContext()), CookiePolicy.ACCEPT_ALL);

// Retrofit的json解析器
ObjectMapper jacksonMapper = JacksonMapper.getInstance().getObjectMapper();

OkHttpClient client = new OkHttpClient.Builder()
        // 添加请求头追加拦截器
        .addInterceptor(headersInterceptor)
        // 添加保存Set－Cookie响应头拦截器
        .addInterceptor(receivedCookiesInterceptor)
        // 设置cookie管理（可选）
        .cookieJar(new JavaNetCookieJar(cookieHandler))
        .build();

Retrofit.Builder builder = new Retrofit.Builder()
        // 配置远程服务
        .baseUrl(BaseString.HTTP)
        // 配置Retrofit回调适配器
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        // 配置JSON解析器
        .addConverterFactory(JacksonConverterFactory.create(jacksonMapper))
        .client(client);

// 构建builder
RetrofitUtils.getInstance().build(builder);
```
这里主要涉及Okhttp的2个拦截器配置和cookie管理（可选），Retrofit配置远程服务地址，配置回调适配器，json解析器和Retrofit构建。
## OkHttp两个拦截器
其中OkHttp多提一下：OKhttp的拦截器是一种很重要的思想，也就是原来在J2EE中经常要了解的面向切面编程，要多多了解一下，主要参考[Okhttp官网](https://github.com/square/okhttp/wiki/Interceptors)
### HeadersRequestInterceptor
该类作用这主要向所有请求中添加请求头。
### ReceivedCookiesInterceptor
该类主要响应头中的Set-Cookie的cookie保存到本地sp文件中，该类依赖SharedPreferUtils。使用如下方法取出cookie值：
```Java
SharedPreferUtils.get(context,BaseString.SET_COOKIE_KEY, String.class)
```
## Okhttp的Cookie管理
主要涉及PersistentCookieStore这个类，这个类依赖SerializableHttpCookie。Okhttp3主要是通过cookieJar(new JavaNetCookieJar(cookieHandler))方法配置cookie。
## 关闭Jackson的JSON解析器的严格校验
```Java
new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
```
## 全局获取retrofit并创造服务
```Java
GitHubService service = RetrofitUtils.getInstance().getRetrofit().create(GitHubService.class);
```
具体Retrofit使用参考[官网教程](http://square.github.io/retrofit/)，类似J2EE的服务层。
