import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {
    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance with SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Generate the hash
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
