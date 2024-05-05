package progetto;

import progetto.entity.ElementoCatalogo;
import progetto.entity.Libro;
import progetto.entity.Rivista;
import progetto.entity.Utente;
import progetto.entity.GestorePrestiti;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class Main {
    private static int prossimoNumeroTessera = 1;

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ProgettoPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            // Creazione e salvataggio di un utente
            Utente utente1 = new Utente(prossimoNumeroTessera++, "Mario", "Rossi", "1990-01-01");
            persistEntityIfNotExists(entityManager, utente1);

            // Creazione e salvataggio di un altro utente
            Utente utente2 = new Utente(prossimoNumeroTessera++, "Luigi", "Verdi", "1995-05-15");
            persistEntityIfNotExists(entityManager, utente2);

            // Creazione e inizializzazione di un oggetto Libro
            Libro libro = new Libro("978-3-16-148410-0", "Il signore degli anelli", 1954, 1170);
            libro.setAutore("J.R.R. Tolkien");
            libro.setGenere("Fantasy");

            // Creazione e inizializzazione di un altro oggetto Libro
            Libro libro2 = new Libro("978-3-16-148411-0", "La compagnia dell'anello", 1954, 1170);
            libro2.setAutore("J.R.R. Tolkien");
            libro2.setGenere("Fantasy");

            // Creazione e inizializzazione di un oggetto Rivista
            Rivista rivista = new Rivista("978-1-23-456789-0", "National Geographic", 2024, 100);
            rivista.setPeriodicità("Mensile");

            // Inserimento delle entità nel database
            persistEntityIfNotExists(entityManager, libro);
            persistEntityIfNotExists(entityManager, libro2);
            persistEntityIfNotExists(entityManager, rivista);

            // Inizializzazione del gestore dei prestiti
            GestorePrestiti gestorePrestiti = new GestorePrestiti(entityManager);

            // Aggiunta di un prestito per l'utente
            gestorePrestiti.aggiungiPrestito(utente1, libro, new Date()); // Aggiunta della data

            // Esempio di ricerca per ISBN
            ElementoCatalogo elementoByIsbn = entityManager.find(ElementoCatalogo.class, "978-3-16-148410-0");
            if (elementoByIsbn != null) {
                System.out.println("Elemento trovato: " + elementoByIsbn.getTitolo());
            } else {
                System.out.println("Nessun elemento trovato per l'ISBN specificato.");
            }

            // Esempio di ricerca per titolo
            List<ElementoCatalogo> elementiByTitolo = entityManager.createQuery(
                            "SELECT e FROM ElementoCatalogo e WHERE e.titolo LIKE :titolo", ElementoCatalogo.class)
                    .setParameter("titolo", "%signore%")
                    .getResultList();
            System.out.println("Elementi trovati per titolo:");
            for (ElementoCatalogo elemento : elementiByTitolo) {
                System.out.println("- " + elemento.getTitolo());
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
        }
    }

    private static void persistEntity(EntityManager entityManager, Object entity) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entity);
        transaction.commit();
    }

    private static void persistEntityIfNotExists(EntityManager entityManager, Object entity) {
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

    private static Object getId(Object entity) {
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
