package org.element.onex;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;

public class Main {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
    JdbcTemplate jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);

    Integer accountsNum =
        jdbcTemplate.queryForObject("SELECT COUNT(*) FROM account;", Integer.class);
    System.out.println("Количество аккаунтов: " + accountsNum);

    List<Integer> accounts =
        jdbcTemplate.queryForList("SELECT balance FROM account;", Integer.class);
    System.out.println("Найденные балансы аккаунтов: " + accounts);

    RowMapper<Account> accountRowMapper =
        new RowMapper<>() {
          @Override
          public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Account(rs.getInt("id"), rs.getString("full_name"), rs.getInt("balance"));
          }
        };
    Account account =
        jdbcTemplate.queryForObject("SELECT * FROM account WHERE id = 1", accountRowMapper);
    System.out.println("Аккаунт с id = 1: " + account);

    Integer updatedCount =
        jdbcTemplate.execute(
            "UPDATE account SET balance = 600 WHERE id = 1;",
            new PreparedStatementCallback<Integer>() {
              @Override
              public Integer doInPreparedStatement(PreparedStatement ps)
                  throws SQLException, DataAccessException {
                return ps.executeUpdate();
              }
            });
    System.out.println("Количество обновленных записей: " + updatedCount);

    int insertedCount =
        jdbcTemplate.update(
            "INSERT INTO account (balance, full_name) VALUES (?, ?)", 100, "Nick White");
    System.out.println("Количество добавленных записей: " + insertedCount);
  }
}

class Account {
  private int id;
  private String fullName;
  private int balance;

  public Account(int id, String fullName, int balance) {
    this.id = id;
    this.fullName = fullName;
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "Account{"
        + "id="
        + id
        + ", fullName='"
        + fullName
        + '\''
        + ", balance="
        + balance
        + '}';
  }
}
