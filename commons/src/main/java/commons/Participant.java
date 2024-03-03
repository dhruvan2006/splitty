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
        public String userName;

        public Participant(String firstName, String lastName, String userName) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.userName = userName;
        }

        @OneToMany(mappedBy = "participant")
        Set<ExpensePayed> expensesPayed;

        public Participant() {

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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Participant that)) return false;
            return getId() == that.getId() && Objects.equals(getFirstName(), that.getFirstName())
                    && Objects.equals(getLastName(), that.getLastName())
                    && Objects.equals(getUserName(), that.getUserName());
        }

        public boolean notNull(){
            return (userName != null) && (firstName != null) && (lastName != null);
        }
        @Override
        public int hashCode() {
            return Objects.hash(getId(), getFirstName(), getLastName(), getUserName());
        }

        @Override
        public String toString() {
            return "Participent{" +
                    "id=" + id +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", userName='" + userName + '\'' +
                    '}';
        }
    }
