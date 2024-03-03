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

        public String email;

        public String IBAN;

        public String userName;

        public boolean formal;

        public String gender;

        public List<Group> groups;

        public Participant(String firstName, String lastName, String email, String IBAN, String userName) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.IBAN = IBAN;
            this.userName = userName;
            formal = false;
            groups = new ArrayList<>();
        }

        public Participant(String firstName, String lastName, String email, String IBAN, String userName, String gender) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.IBAN = IBAN;
            this.userName = userName;
            this.gender = gender;
            formal = false;
            groups = new ArrayList<>();
        }

        public String getEmail() {
            return email;
        }

        public String getIBAN() {
            return IBAN;
        }

        public Set<ExpensePayed> getExpensesPayed() {
            return expensesPayed;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setIBAN(String IBAN) {
            this.IBAN = IBAN;
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
            return getId() == that.getId() && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getIBAN(), that.getIBAN()) && Objects.equals(getUserName(), that.getUserName()) && Objects.equals(getExpensesPayed(), that.getExpensesPayed());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getFirstName(), getLastName(), getEmail(), getIBAN(), getUserName(), getExpensesPayed());
        }

        @Override
        public String toString() {
            return "Participant{" +
                    "id=" + id +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", email='" + email + '\'' +
                    ", IBAN='" + IBAN + '\'' +
                    ", userName='" + userName + '\'' +
                    ", expensesPayed=" + expensesPayed +
                    '}';
        }

        public boolean notNull(){
            return (userName != null) && (firstName != null) && (lastName != null);
        }

        public void enterGroup(Group group){
		groups.add(group);
	    }

	    public void leaveGroup(Group group){
		groups.remove(group);
	    }
        
        public List<Group> getGroups(){
            return groups;
        }

	    /**
 	    * each person will have the option to display their profile name formally or not
 	    */
	    public void setFormal(boolean value){
		formal = value;
	    }

        /**
        * to be used whenever searching a user or looking through the participants of a group
        */
	    public String getFullName(){
        String name = firstName + " " + lastName + ", known as " + userName;    
		if (formal && gender!=null){
			if (gender.equals("female")) return "Ms. " + name;
            else if (gender.equals("male")) return "Mr. " + name;
			return "Mx. " + name;
		}
		return name;
	    }

    }
