package EventDriven;

import org.mindrot.jbcrypt.BCrypt;

public class hashCreate {

    public static void main(String[] args) {
        String password = "admin123";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println("Generated hash:");
        System.out.println(hash);
    }
}
