package cl.architeq.zkpush.impl.config;

import cl.architeq.zkpush.impl.command.Command;
import cl.architeq.zkpush.model.Device;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, Command> commandCache() {
        return CacheBuilder.newBuilder().maximumSize(1000).build();
    }

    @Bean
    public Cache<String, Long> attendanceLogCache() {
        return CacheBuilder.newBuilder().maximumSize(50).build();
    }

    @Bean
    public Cache<LocalDate, Integer> codeGen() {
        return CacheBuilder.newBuilder().maximumSize(50).build();
    }

    @Bean
    public Cache<String, DeviceOption> deviceOption() {
        return CacheBuilder.newBuilder().maximumSize(200).build();
    }

    @Bean
    public Cache<String, Device> deviceCache() {
        return CacheBuilder.newBuilder().maximumSize(200).build();
    }

}
