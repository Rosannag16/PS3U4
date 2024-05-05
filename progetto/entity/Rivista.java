package progetto.entity;

import javax.persistence.Entity;

@Entity
public class Rivista extends ElementoCatalogo {
    private String periodicità;

    public Rivista() {
        super();
    }

    public Rivista(String isbn, String titolo, int annoPubblicazione, int numeroPagine) {
        super(isbn, titolo, annoPubblicazione, numeroPagine);
    }

    public String getPeriodicità() {
        return periodicità;
    }

    public void setPeriodicità(String periodicità) {
        this.periodicità = periodicità;
    }
}
