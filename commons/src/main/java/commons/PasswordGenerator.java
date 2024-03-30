package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Random;

@Entity
public class PasswordGenerator {

    @Id
    private String password;

    private int saltHash;


    public PasswordGenerator(){
        this.password = passwordGenerator();
        this.saltHash = salt(password).hashCode();
    }

    private static String passwordGenerator(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        Random random = new Random();

        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        String x = sb.toString();
        return x;
    }

    private static String salt(String s){
        return s + "55005";
    }

    public String getPassword() {
        return password;
    }

    public int getSaltHash() {
        return saltHash;
    }
}
