package progetto.dao;

import progetto.entity.Rivista;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class RivistaDao {
    private EntityManagerFactory entityManagerFactory;

    public RivistaDao() {
        entityManagerFactory = Persistence.createEntityManagerFactory("ProgettoPU");
    }

    public void aggiungiRivista(Rivista rivista) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(rivista);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public Rivista trovaRivistaPerISBN(String isbn) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Rivista rivista = null;

        try {
            rivista = entityManager.find(Rivista.class, isbn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return rivista;
    }
}
