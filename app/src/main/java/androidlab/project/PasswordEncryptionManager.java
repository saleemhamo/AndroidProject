package androidlab.project;

import android.util.Base64;

public class PasswordEncryptionManager {

    private static PasswordEncryptionManager passwordEncryptionManager = null;

    private PasswordEncryptionManager() {
//        this.passwordEncryptionManager = new PasswordEncryptionManager();
    }

    public static PasswordEncryptionManager getInstance() {
        if (passwordEncryptionManager != null) {
            return passwordEncryptionManager;
        }
        passwordEncryptionManager = new PasswordEncryptionManager();
        return passwordEncryptionManager;
    }


    public String getEncodedString(String password) {
        return Base64.encodeToString(password.getBytes(), 0);
    }

    public String getDecodedString(String encryptedString) {
        return new String(Base64.decode(encryptedString.getBytes(), 0));
    }
}
