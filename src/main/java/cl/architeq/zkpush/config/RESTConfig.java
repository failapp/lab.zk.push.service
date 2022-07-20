package cl.architeq.zkpush.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class RESTConfig {

    public static String WEBSERVICE_URL;

    @Value("${webservice.url.endpoint}")
    public void setWebserviceUrl(String url) {
        WEBSERVICE_URL = url;
    }

    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        //headers.set("tenantId", RESTConfig.CUSTOMER_ID);
        //headers.set("tenantKey", RESTConfig.CUSTOMER_KEY);
        return headers;
    }

}
