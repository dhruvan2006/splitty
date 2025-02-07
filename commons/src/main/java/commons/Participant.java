package commons;
import jakarta.persistence.*;

import java.util.*;


@Entity
public class Participant{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public String email;

    public String IBAN;

    public String userName;


    public Participant(String email, String IBAN, String userName) {
        this.email = email;
        this.IBAN = IBAN;
        this.userName = userName;
    }

    public Participant() {
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getIBAN() {
        return IBAN;
    }

    public String getUserName() {
        return userName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean notNull(){
        return getUserName() != null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        return getId() == ((Participant)o).getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getIBAN(), getUserName());
    }


    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", email='" + email +
                ", IBAN='" + IBAN +
                ", userName='" + userName +
                '}';
    }
}
