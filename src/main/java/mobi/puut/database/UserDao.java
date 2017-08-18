package mobi.puut.database;

import mobi.puut.entities.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * Created by Chaklader on 6/13/17.
 */
@Repository
public class UserDao extends HibernateDaoSupport{

    /**
     * @param user takes the wallet user as argument
     * @return true if the creation of the user is successful, otherwise, return false.
     */
    public boolean create(User user) {

        Transaction transaction = null;

        try {
            Session session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return false;
    }

    /**
     * @param user takes wallet user as an argument and save it in the users table
     */
    public void saveOrUpdate(User user) {

        Transaction transaction = null;

        try {
            Session session = getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
        } catch (HibernateException e) {

            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * @param users takes list of users as arguments
     * @return return true if the creation of all the users is successful
     */
    public boolean create(List<User> users) {

        Transaction transaction = null;

        try {

            Session session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            for (User user : users) {
                session.save(user);
            }

            transaction.commit();
            session.close();

            return true;
        } catch (HibernateException e) {
            if (Objects.nonNull(transaction)) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return false;
    }

    /**
     * @return get the list of all the wallet users
     */
    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {

        try {
            Session session = getSessionFactory().openSession();
            List<User> userList = session.createQuery("from User").list();
            session.close();
            return userList;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param id Id of the bitcoin wallet user
     * @return the bitcoin wallet user
     */
    public User getById(int id) {
        try (Session session = getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return user;
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return null;
    }
}

