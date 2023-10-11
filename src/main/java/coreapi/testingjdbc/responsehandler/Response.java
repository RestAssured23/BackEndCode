package coreapi.testingjdbc.responsehandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response {

    private static JdbcTemplate jdbcTemplate = null;

    public Response(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public static ResponseEntity<Object> responseBuilder(String message, HttpStatus httpStatus, String sqlQuery) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("httpstatus", httpStatus);

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sqlQuery);
        response.put("data", data);

        return new ResponseEntity<>(response, httpStatus);
    }



 /*   public static ResponseEntity<Object> responseBuilder(
            String message, HttpStatus httpStatus, Object responseObject
            )
    {
        Map<String,Object> response=new HashMap<>();
        response.put("message", message);
        response.put("httpstatus", httpStatus);
        response.put("data", responseObject);
        return new ResponseEntity<>(response,httpStatus);

    }*/
}
