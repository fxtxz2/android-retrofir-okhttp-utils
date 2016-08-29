

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


/**
 * SharedPreferences 持久化工具类
 * Created by zyl on 16/8/1.
 */
public class SharedPreferUtils {
    private final static String NAME = "my_zyl_shared";
    public static void save(Context context, String key, Object object){
        SharedPreferences sharedPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        ObjectMapper mapper = JacksonMapper.getInstance().getObjectMapper();
        try {
            editor.putString(key, mapper.writeValueAsString(object));
            editor.apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static  <T> T get(Context context, String key, Class<T> c){
        SharedPreferences sharedPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String text = sharedPref.getString(key, "");
        ObjectMapper mapper = JacksonMapper.getInstance().getObjectMapper();
        try {
            return mapper.readValue(text, c);
        } catch (IOException e) {
//            e.printStackTrace();
            return null;
        }
    }
}
