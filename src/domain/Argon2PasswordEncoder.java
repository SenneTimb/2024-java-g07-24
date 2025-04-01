package domain;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon2PasswordEncoder {

	private final static Argon2 ARGON2 = Argon2Factory.create(
	        Argon2Factory.Argon2Types.ARGON2id,
	        16,
	        32);

    public static String encode(String rawPassword) {
        return ARGON2.hash(6, 131072, 4, rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return ARGON2.verify(encodedPassword, rawPassword);
    }
	
}
