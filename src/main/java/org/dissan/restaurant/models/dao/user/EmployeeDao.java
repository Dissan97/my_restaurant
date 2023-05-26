package org.dissan.restaurant.models.dao.user;

import org.dissan.restaurant.cli.utils.OutStream;
import org.dissan.restaurant.models.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeDao {

    private EmployeeDao(){}
    private static final String EMPLOYEES = "employees.json";
    private static final List<Employee> EMPLOYEE_LIST = new ArrayList<>();
    public static @Nullable List<Employee> pullEmployees() {
        if (EMPLOYEE_LIST.isEmpty()) {
            UserDao dao = new UserDao();
            List<AbstractUser> users = dao.pullUsers();
            try (InputStream reader = Objects.requireNonNull(UserDaoFs.class.getResourceAsStream(EmployeeDao.EMPLOYEES))) {
                JSONTokener jsonTokener = new JSONTokener(reader);
                JSONArray array = new JSONArray(jsonTokener);
                for (int i = 0; i < array.length(); i++){
                    for (AbstractUser u:
                         users) {
                        JSONObject object = array.getJSONObject(i);
                        if (u.getUsername().equals(object.getString("username"))){
                            EMPLOYEE_LIST.add(fillData(u, object));
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                return null;
            }
        }
        return EMPLOYEE_LIST;
    }

    public static @Nullable Employee getEmployeeByCode(String empCode){
        if (EMPLOYEE_LIST.isEmpty()){
            pullEmployees();
        }
        for (Employee e:
             EMPLOYEE_LIST) {
            if (e.getEmployeeCode().equals(empCode)){
                return e;
            }
        }
        return null;
    }

    private static @NotNull Employee fillData(@NotNull AbstractUser user, JSONObject object) {
        Employee employee = null;
        if (user.getRole() == UserRole.ATTENDANT){
            employee = new Attendant(user);
        } else if (user.getRole() == UserRole.COOKER) {
            employee = new Cooker(user);
        }
        assert employee != null;
        employee.setEmployeeCode(object.getString("employeeCode"));
        return employee;
    }


}
