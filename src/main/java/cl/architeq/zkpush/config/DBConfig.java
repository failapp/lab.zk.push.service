package cl.architeq.zkpush.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName( env.getProperty("datasource.driver") );
        dataSourceBuilder.url( env.getProperty("datasource.url") );
        dataSourceBuilder.username( env.getProperty("datasource.username") );
        dataSourceBuilder.password( env.getProperty("datasource.password") );
        return dataSourceBuilder.build();

    }


}
