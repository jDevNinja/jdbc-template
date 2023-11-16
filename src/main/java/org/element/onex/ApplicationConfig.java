package org.element.onex;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class ApplicationConfig {

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource =
        new DriverManagerDataSource("jdbc:postgresql://localhost:5442/postgres", "user", "pass");
    dataSource.setDriverClassName("org.postgresql.Driver");
    return dataSource;
  }

  @Bean
  public JdbcTemplate jdbcTemplate(@Autowired DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
