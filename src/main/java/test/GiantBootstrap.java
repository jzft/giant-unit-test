package test;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;



@EnableAspectJAutoProxy(exposeProxy=true)
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class,XADataSourceAutoConfiguration.class,TransactionAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@ComponentScan(value="test")
@Configuration
@PropertySource("application.properties")
public class GiantBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(GiantBootstrap.class, args);
    }
    

}