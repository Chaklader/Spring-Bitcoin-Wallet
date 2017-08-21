package mobi.puut.database;

import mobi.puut.entities.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created by Chaklader on 6/13/17.
 */
@Repository
public class UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * @param user takes the wallet user as argument
     * @return true if the creation of the user is successful, otherwise, return false.
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean create(User user) {
        sessionFactory.getCurrentSession().save(user);
        return true;
    }


    /**
     * @param user takes wallet user as an argument and save it in the users table
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(User user) {
        sessionFactory.getCurrentSession().save(user);
    }

    /**
     * @param users takes list of users as arguments
     * @return return true if the creation of all the users is successful
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean create(List<User> users) {

        users.forEach(user -> sessionFactory.getCurrentSession().save(user));
        return true;
    }

    /**
     * @return get the list of all the wallet users
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public List<User> getAllUsers() {
        return sessionFactory.getCurrentSession()
                .createQuery("from User").getResultList();
    }

    /**
     * @param id Id of the bitcoin wallet user
     * @return the bitcoin wallet user
     */
    @Transactional(rollbackFor = Exception.class)
    public User getById(int id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }
}

