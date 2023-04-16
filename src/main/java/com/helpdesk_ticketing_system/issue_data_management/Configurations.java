package com.helpdesk_ticketing_system.issue_data_management;

import com.helpdesk_ticketing_system.issue_data_management.persistence.DatabaseFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

@Configuration
public class Configurations {

    @Bean
    public ApplicationContext getAnnotationApplicationContext(){
        return new AnnotationConfigApplicationContext(DatabaseConfig.class);
    }

    @Bean
    public DatabaseFactory getDatabaseFactory(){
        return new DatabaseFactory(getAnnotationApplicationContext());
    }

    @Bean
    public Random getRandom(){
        return new Random();
    }

    @Bean
    public Calendar getCalenderInstance(){
        return Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
    }

}
