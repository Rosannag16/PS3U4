package progetto.dao;

import progetto.entity.ElementoCatalogo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ElementoCatalogoDao {
    private EntityManagerFactory entityManagerFactory;

    public ElementoCatalogoDao() {
        entityManagerFactory = Persistence.createEntityManagerFactory("ProgettoPU");
    }

    public void aggiungiElementoCatalogo(ElementoCatalogo elementoCatalogo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(elementoCatalogo); // Assicurati che l'oggetto sia correttamente importato e istanziato
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

    public ElementoCatalogo trovaElementoPerISBN(String isbn) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ElementoCatalogo elementoCatalogo = null;

        try {
            elementoCatalogo = entityManager.find(ElementoCatalogo.class, isbn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return elementoCatalogo;
    }
}
