package coreapi.testingjdbc.controller;

import coreapi.testingjdbc.dao.UserDao;
import coreapi.testingjdbc.exceptionerror.DuplicateKeyViolationException;
import coreapi.testingjdbc.model.userdetails;
import coreapi.testingjdbc.model.userinfoBO;
import coreapi.testingjdbc.model.userupdateBO;
import coreapi.testingjdbc.responsehandler.Response;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class usercontroller {
    private final UserDao userDao;

    public usercontroller(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("/user/test/getall")
    public List<userdetails> getUsers(){
        return userDao.getAll();
    }

    @GetMapping("/user/test/get")
    public userdetails getById(@RequestParam int id){
        return userDao.getById(id);
    }

 /*   @GetMapping("/user/test/get")
    public ResponseEntity<Object> getById(@RequestParam int id){
       return Response.responseBuilder("get investor details",
                HttpStatus.OK, String.valueOf(userDao.getById(id)));
     //   return userDao.getById(id);
    }*/
    @PostMapping("/user/test/save")
    public ResponseEntity<Object> saveuser(@Valid @RequestBody userdetails userdetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return a response with field-specific error messages
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            try {
                int result = userDao.save(userdetails);
               return ResponseEntity.ok("User created successfully");
            } catch (DuplicateKeyException e) {
                String emailfieldname = "email"; // Replace with the actual field name causing the duplicate key violation
            throw new DuplicateKeyViolationException(emailfieldname, "Duplicate key violation: " + e.getMessage());
            }
        }
    }
/*
    @PostMapping("/user/test/save")
    public ResponseEntity<Object> saveuser(@Valid @RequestBody userdetails userdetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return a response with field-specific error messages
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            try {
                int result = userDao.save(userdetails);
                return ResponseEntity.ok("User created successfully");
            } catch (DuplicateKeyException e) {
                String emailFieldName = "email";
                String mobileFieldName = "mobile";
                String errorMessage = "Duplicate key violation: " + e.getMessage();
                throw new DuplicateKeyViolationException(emailFieldName, errorMessage, mobileFieldName);
            }
        }
    }*/


    @PutMapping("/user/test/update")
    public String update(@Valid @RequestBody userupdateBO userupdate, @RequestParam int id){
        return userDao.update(userupdate,id)+"No. of Rows Updated to the DB";
    }

    @DeleteMapping("/user/test/delete")
    public String delete(@RequestParam int id){

        return userDao.delete(id)+"Rows Deleted";
    }

    @PostMapping("/user/test/userinfo")
    public String useraddress(@Valid @RequestBody userinfoBO userinfoBO, @RequestParam int userinfoid){
        return userDao.userinfo(userinfoBO,userinfoid)+"No. of Rows Saved to the DB";
    }

}
