package commons;
import jakarta.persistence.*;

import java.util.*;


    @Entity
    public class Participant{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        public long id;
        public String firstName;
        public String lastName;

        public Participant(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public long getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Participant that)) return false;
            return getId() == that.getId() && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getFirstName(), getLastName());
        }

        @Override
        public String toString() {
            return "Participent{" +
                    "id=" + id +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }
