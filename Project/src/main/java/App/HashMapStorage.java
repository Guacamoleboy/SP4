package App;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HashMapStorage {

    // Attributes
    private static final long exp_time = TimeUnit.MINUTES.toMillis(5); // 5 min
    private static final HashMap<String, CodeEntry> storage = new HashMap<>();

    // ________________________________________________________

    private static class CodeEntry {

        // Inner attributes
        String code;
        long timestamp;

        // ________________________________________________________

        CodeEntry(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        } // Inner Constructor end

    } // Inner end

    // ________________________________________________________

    public static void saveCode(String email, String code) {
        storage.put(email, new CodeEntry(code, System.currentTimeMillis()));
    }

    // ________________________________________________________

    public static boolean validateCode(String email, String code) {
        CodeEntry entry = storage.get(email);
        if (entry == null) {
            System.out.println("Code not found for email.");
            return false;
        }

        long now = System.currentTimeMillis();
        if (!entry.code.equals(code)) {
            System.out.println("Code does not match.");
            return false;
        }
        if (now - entry.timestamp > exp_time) {
            System.out.println("Code expired.");
            storage.remove(email);
            return false;
        }

        return true;
    }

} // Class end
