// File: progetto/entity/EntitaPersistente.java
package progetto.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class EntitaPersistente {
    protected static void persistEntity(EntityManager entityManager, Object entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entity);
        transaction.commit();
    }

    protected static void persistEntityIfNotExists(EntityManager entityManager, Object entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            Object existingEntity = entityManager.find(entity.getClass(), getId(entity));
            if (existingEntity == null) {
                entityManager.persist(entity);
                // Stampiamo l'entità in base al suo tipo
                if (entity instanceof Utente) {
                    System.out.println("Nuovo utente aggiunto: " + ((Utente) entity).getNome() + " " + ((Utente) entity).getCognome());
                } else if (entity instanceof Libro) {
                    System.out.println("Nuovo libro aggiunto: " + ((Libro) entity).getTitolo());
                } else if (entity instanceof Rivista) {
                    System.out.println("Nuova rivista aggiunta: " + ((Rivista) entity).getTitolo());
                }
            } else {
                // Entità già presente nel database, non fare nulla
                if (entity instanceof Utente) {
                    System.out.println("L'utente con ID " + getId(entity) + " esiste già nel database.");
                } else if (entity instanceof Libro) {
                    System.out.println("Il libro con ISBN " + ((Libro) entity).getIsbn() + " esiste già nel database.");
                } else if (entity instanceof Rivista) {
                    System.out.println("La rivista con ISBN " + ((Rivista) entity).getIsbn() + " esiste già nel database.");
                }
            }
            transaction.commit();
        } catch (Exception ex) {
            transaction.rollback();
            throw ex;
        }
    }

    protected static Object getId(Object entity) {
        if (entity instanceof Utente) {
            // Restituisce l'ID del numero di tessera per l'utente
            return ((Utente) entity).getNumeroTessera();
        } else if (entity instanceof Libro) {
            return ((Libro) entity).getIsbn();
        } else if (entity instanceof Rivista) {
            return ((Rivista) entity).getIsbn();
        } else {
            // Gestire altri tipi di entità, se necessario
            throw new IllegalArgumentException("Tipo di entità non gestito: " + entity.getClass());
        }
    }
}
