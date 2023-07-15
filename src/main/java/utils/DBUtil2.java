package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import constants.JpaConst2;

public class DBUtil2 {
    private static EntityManagerFactory emf;

    //EntityManagerインスタンスを生成
    public static EntityManager createEntityManager() {
        return _getEntityManagerFactory().createEntityManager();
    }

    //EntityManagerFactoryインスタンスを生成
    private static EntityManagerFactory _getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(JpaConst2.PERSISTENCE_UNIT_NAME);
        }

        return emf;
    }

}