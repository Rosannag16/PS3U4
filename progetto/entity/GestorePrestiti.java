package progetto.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Date;

public class GestorePrestiti {
    private EntityManager entityManager;

    public GestorePrestiti(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void aggiungiPrestito(Utente utente, ElementoCatalogo elemento, Date dataInizioPrestito) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Prestito prestito = new Prestito(utente, elemento, dataInizioPrestito);
        entityManager.persist(prestito);

        transaction.commit();
    }

    // Altre operazioni per la gestione dei prestiti possono essere aggiunte qui
}
