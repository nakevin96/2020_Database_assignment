import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SimpleFunction {
	public static String getUserPass(String s) {
		MessageDigest md;
		String pw=null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(s.getBytes());
			pw = String.format("%064x", new BigInteger(1,md.digest()));
			return pw;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return pw;
		
	}
}
