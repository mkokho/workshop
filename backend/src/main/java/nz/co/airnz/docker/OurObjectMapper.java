package nz.co.airnz.docker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import io.dropwizard.jackson.Jackson;

public class OurObjectMapper {

    public static final ObjectMapper INSTANCE = makeObjectMapper();

    private static ObjectMapper makeObjectMapper() {
        ObjectMapper mapper = Jackson.newMinimalObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new KotlinModule());
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }
}
