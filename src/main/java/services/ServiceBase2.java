package services;

import javax.persistence.EntityManager;

import utils.DBUtil2;

public class ServiceBase2 {
    protected EntityManager em = DBUtil2.createEntityManager();

    /**
     * EntityManagerのクローズ
     */
    public void close() {
        if (em.isOpen()) {
            em.close();
        }
    }

}