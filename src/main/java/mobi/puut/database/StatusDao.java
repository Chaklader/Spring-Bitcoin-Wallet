package mobi.puut.database;

import mobi.puut.entities.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chaklader on 6/24/17.
 */
@Repository
public class StatusDao extends HibernateDaoSupport {


    /**
     * @param status takes transaction status save it in the status database table
     * @return return the status object
     */
    public Status saveStatus(final Status status) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(status);
            transaction.commit();
            return status;
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }


    /**
     * @param walletId takes wallet ID as the input paramenter
     * @return return the list of the statuses
     */
    public List<Status> getByWalletId(final Long walletId) {
        try (Session session = getSessionFactory().openSession()) {
            Query query = session.createQuery("from Status where wallet_id = :walletId");
            query.setParameter("walletId", walletId);
            List<Status> userList = query.list();
            return userList;
        }
    }
}
