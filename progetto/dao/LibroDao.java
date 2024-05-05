package progetto.dao;

import progetto.entity.Libro;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class LibroDao {
    private EntityManagerFactory entityManagerFactory;

    public LibroDao() {
        entityManagerFactory = Persistence.createEntityManagerFactory("ProgettoPU");
    }

    public void aggiungiLibro(Libro libro) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(libro);
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

    public Libro trovaLibroPerISBN(String isbn) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Libro libro = null;

        try {
            libro = entityManager.find(Libro.class, isbn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return libro;
    }
}
