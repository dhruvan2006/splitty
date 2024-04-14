/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import java.util.Random;

@SpringBootApplication
@EntityScan(basePackages = { "commons", "server" })
public class Main {

    private static String password;

    public static void main(String[] args) {
        password = passwordGenerator();
        System.out.println("ADMIN PASSWORD: " + password);
        SpringApplication.run(Main.class, args);
    }

    public static String passwordGenerator(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        Random random = new Random();

        StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 15; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String newPassword) {
        password = newPassword;
    }

    // strong password means at least one uppercase letter, one lowercase letter, one digit
    // and one special character
    public static boolean isPasswordStrong(String candidatePassword) {
        String upper = ".*[A-Z].*";
        String lower = ".*[a-z].*";
        String digit = ".*\\d.*";
        String special = ".*[^A-Za-z0-9].*";

        // Check if the password matches all the criteria
        return candidatePassword.matches(upper) &&
                candidatePassword.matches(lower) &&
                candidatePassword.matches(digit) &&
                candidatePassword.matches(special);
    }
}