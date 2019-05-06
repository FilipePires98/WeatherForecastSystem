package weatherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import weatherapp.cache.LocalCache;
import weatherapp.services.ExternalService;
import weatherapp.services.WeatherService;

/**
 * Application Main class.
 * @author Filipe Pires (85122)
 */
@SpringBootApplication
public class WeatherApplication {

    /**
     * Main function, executed when starting the Application
     * @param args [unused]
     */
    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
       return new RestTemplate();
    }
    
    @Bean
    public LocalCache getLocalCache() {
        return new LocalCache();
    }
    
    @Bean
    public WeatherService getWeatherService() {
        return new WeatherService();
    }
    
    @Bean
    public ExternalService getExternalService() {
        return new ExternalService();
    }
    
}
