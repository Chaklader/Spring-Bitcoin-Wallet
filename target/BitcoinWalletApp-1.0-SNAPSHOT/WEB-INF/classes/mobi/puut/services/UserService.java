package mobi.puut.services;

import java.util.List;
import java.util.Objects;

import mobi.puut.database.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mobi.puut.entities.User;


/**
 * Created by Chaklader on 6/19/17.
 */
@Service("userService")
public class UserService {

    @Autowired
    public UserDao userDAO;

    @Transactional(readOnly = true)
    public List<User> getCurrentStatuses() {
        return userDAO.getAllUsers();
    }

    public void create(User user) {
        userDAO.saveOrUpdate(user);
    }

    public List<User> getAllUsers() {
        List<User> users = userDAO.getAllUsers();

        if (Objects.isNull(users)) {
            return null;
        }
        return users;
    }
}

