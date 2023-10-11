package coreapi.testingjdbc.dao;

import coreapi.testingjdbc.exceptionerror.DuplicateKeyViolationException;
import coreapi.testingjdbc.model.userdetails;
import coreapi.testingjdbc.model.userinfoBO;
import coreapi.testingjdbc.model.userupdateBO;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@Repository
public class UserDaoIMPL implements UserDao{

    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String isoFormat = sdf.format(new Date(currentTimestamp.getTime()));
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public int save(userdetails userdetails) {
        try {
            String hashedPassword = BCrypt.hashpw(userdetails.getPassword(), BCrypt.gensalt());
            String base64Password = Base64.getEncoder().encodeToString(hashedPassword.getBytes());

            // Step 1: Insert into 'testuser' table and retrieve the generated keys
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO testuser (" +
                                "name, gender, mobile, email, password, active, createddate, modifieddate" +
                                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, userdetails.getName());
                ps.setString(2, String.valueOf(userdetails.getGender()));
                ps.setString(3, userdetails.getMobile());
                ps.setString(4, userdetails.getEmail());
                ps.setString(5, base64Password);
                ps.setString(6, "A");
                ps.setString(7, isoFormat);
                ps.setString(8, isoFormat);
                return ps;
            }, keyHolder);

            // Step 2: Retrieve the generated keys as a map
            Map<String, Object> keys = keyHolder.getKeys();

            // Step 3: Retrieve the user ID from the generated keys
            assert keys != null;
            int userId = (int) keys.get("id");

            // Step 4: Insert user ID into another table
            int rowsUpdated = jdbcTemplate.update(
                    "INSERT INTO userinfo (userid) VALUES (?)",
                    userId);

            // Step 5: Update 'createddate' in the 'userinfo' table
            int rowsUpdatedCreatedDate = jdbcTemplate.update(
                    "UPDATE userinfo SET createddate = ? WHERE userid = ?",
                    isoFormat, userId);

            return rowsUpdated;
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyViolationException("Duplicate key violation: " + e.getMessage(), "Duplicate key violation: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(userupdateBO userupdateBO, int id) {
        StringBuilder queryBuilder = new StringBuilder("UPDATE testuser SET ");
        List<Object> params = new ArrayList<>();
        try {
            if (userupdateBO.getName() != null) {
                queryBuilder.append("name=?, ");
                params.add(userupdateBO.getName());
            }

            if (userupdateBO.getGender() != 0) {        // Assuming '0' is a default value for 'gender'
                queryBuilder.append("gender=?, ");
                params.add(userupdateBO.getGender());
            }

            if (userupdateBO.getMobile() != null) {
                queryBuilder.append("mobile=?, ");
                params.add(userupdateBO.getMobile());
            }

            if (userupdateBO.getEmail() != null) {
                queryBuilder.append("email=?, ");
                params.add(userupdateBO.getEmail());
            }
            queryBuilder.append("modifieddate=NOW(), ");
            // Remove the trailing comma and space from the query string
            queryBuilder.setLength(queryBuilder.length() - 2);

            // Add the WHERE clause to specify the user by ID
            queryBuilder.append(" WHERE id = ?");
            params.add(id);

            return jdbcTemplate.update(queryBuilder.toString(), params.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

            @Override
    public int delete(int id) {
                try {
                    return jdbcTemplate.update("UPDATE testuser set active='D' where id=?", id);
                } catch (Exception e) {
                    e.printStackTrace();
                    return -1;
                }
            }

    @Override
    public List<userdetails> getAll() {
     return jdbcTemplate.query("select * from testuser",new BeanPropertyRowMapper<userdetails>(userdetails.class));
    }

    @Override
    public userdetails getById(int id) {
        return jdbcTemplate.queryForObject("select * from testuser where id=?",new BeanPropertyRowMapper<userdetails>(userdetails.class),id);
    }

    @Override
    public int userinfo(userinfoBO userinfo, int userinfoid) {
        try{
            return jdbcTemplate.update("UPDATE userinfo " +
                            "SET address = ?, city = ?, state = ?, pincode = ?, modifieddate=?" +
                            "WHERE userid = ?",
                    userinfo.getAddress(), userinfo.getCity(),
                    userinfo.getState(), userinfo.getPincode(), isoFormat,userinfoid);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }

    }
}
