

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson Json单列
 * Created by zyl on 16/8/19.
 */
public class JacksonMapper {
    /**
     * Jackson Mapper
     */
    private ObjectMapper objectMapper;
    private JacksonMapper(){
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    private static class JacksonMapperHolder{
        private static final JacksonMapper INSTANCE = new JacksonMapper();
    }
    public static JacksonMapper getInstance(){
        return JacksonMapperHolder.INSTANCE;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
