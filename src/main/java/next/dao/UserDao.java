package next.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import next.dao.template.JdbcTemplate;
import next.model.User;

public class UserDao {

    public void insert(User user){

        String sql = "INSERT INTO USERS VALUES (?,?,?,?)";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        });


        jdbcTemplate.update(sql);
    }

    public void update(User user){

        String sql = "UPDATE USERS SET name=?, password=?, email=? WHERE userId=?";

        JdbcTemplate jdbcTemplate = new JdbcTemplate((pstmt)->{
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());

        });

        jdbcTemplate.update(sql);
    }


    public List<User> findAll() throws SQLException {

        ArrayList<User> users = new ArrayList<>();

        String sql = "SELECT userId, password, name, email FROM USERS";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(new JdbcTemplate.RowMapper() {
            @Override
            public Object mapRow(ResultSet resultSet) throws SQLException {
                User user = new User(resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email"));
                return user;
            }
        });

        jdbcTemplate.select(sql).forEach(x->users.add((User)x));
        return users;
    }

    public User findByUserId(String userId) throws SQLException {
        String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

        final JdbcTemplate jdbcTemplate = new JdbcTemplate(
                new JdbcTemplate.PreParedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement pstmt) throws SQLException {
                        pstmt.setString(1, userId);
                    }
                },
                new JdbcTemplate.RowMapper() {
                    @Override
                    public Object mapRow(ResultSet resultSet) throws SQLException {
                        return new User(resultSet.getString("userId"),
                                resultSet.getString("password"),
                                resultSet.getString("name"),
                                resultSet.getString("email"));
                    }
                });

        User user = (User) jdbcTemplate.selectForObject(sql);
        return user;
    }
}



