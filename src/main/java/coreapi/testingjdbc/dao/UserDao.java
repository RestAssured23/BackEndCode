package coreapi.testingjdbc.dao;

import coreapi.testingjdbc.model.userdetails;
import coreapi.testingjdbc.model.userinfoBO;
import coreapi.testingjdbc.model.userupdateBO;

import java.util.List;

public interface UserDao {
    int save(userdetails userdetails);
    int update(userupdateBO userupdate, int id);
    int delete(int id);
    List<userdetails> getAll();

    userdetails getById(int id);
    int userinfo(userinfoBO userinfo, int userinfoid);
}
