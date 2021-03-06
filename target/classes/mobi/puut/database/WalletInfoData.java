package mobi.puut.database;

import mobi.puut.entities.WalletInfo;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chaklader on 6/24/17.
 */
@Repository
public class WalletInfoData {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional(rollbackFor = Exception.class)
    public List<WalletInfo> getAllWallets() {

        return sessionFactory.getCurrentSession()
                .createQuery("from WalletInfo").getResultList();
    }

    @Transactional(rollbackFor = Exception.class)
    public WalletInfo getById(final Long id) {
        return sessionFactory.getCurrentSession().get(WalletInfo.class, id);
    }

    /**
     * @param address address of the wallet
     * @return return the created WalletInfo object with provided name and address
     */
    @Transactional(rollbackFor = Exception.class)
    public WalletInfo create(final String address, final String currency, final String code) {

        WalletInfo walletInfo = new WalletInfo();

        walletInfo.setAddress(address);
        walletInfo.setCurrency(currency);
        walletInfo.setCode(code);

        sessionFactory.getCurrentSession().persist(walletInfo);
        return walletInfo;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteWalletInfoById(Long theId) {
        sessionFactory.getCurrentSession().createQuery("delete WalletInfo where id = :id")
                .setParameter("id", theId).executeUpdate();
    }
}
