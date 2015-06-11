package fi.ni.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

/*
 * Adapted from: http://www.asjava.com/core-java/java-md5-example/
 */

public class StringChecksum {

	char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
			'b', 'c', 'd', 'e', 'f' };
	MessageDigest md;
	MessageDigest old;
	boolean value_read = false;

	boolean useHash = true;
	StringBuffer noHashSave = new StringBuffer();

	/*public StringChecksum() {
		this.useHash = true;

		try {
			md = MessageDigest.getInstance("MD5"); // SHA-512 MD5
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		try {
			old = (MessageDigest) md.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

	}*/

	public StringChecksum(boolean useHash) {
		this.useHash = useHash;
		this.useHash = true;
		if (useHash) {
			try {
				md = MessageDigest.getInstance("MD5"); // SHA-512 MD5
			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			}
			try {
				old = (MessageDigest) md.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	public StringChecksum(MessageDigest in_md) {
		this.useHash = false;

		try {
			md = (MessageDigest) in_md.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		try {
			old = (MessageDigest) md.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public StringChecksum(boolean useHash, MessageDigest in_md) {
		this.useHash = useHash;
		if (useHash) {
			try {
				md = (MessageDigest) in_md.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			try {
				old = (MessageDigest) md.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	public StringChecksum copy() {
		StringChecksum md5 = new StringChecksum(this.useHash, this.old);
		return md5;
	}

	/*
	 * public void update(byte[] bytes) { md.update(bytes); }
	 */

	public void update(String txt) {
		if (!useHash)
			noHashSave.append(" "+txt);
		else {
			if (value_read) {
				System.err
						.println("String Md5: Path checksum out of sequence!:"
								+ txt);
				for (StackTraceElement ste : Thread.currentThread()
						.getStackTrace()) {
					System.out.println(ste);
				}
			}

			md.update(txt.getBytes());
		}
	}

	String result = "-";

	public String getChecksumValue() {
		if (!useHash)
			return noHashSave.toString();
		if (value_read) {
			return result;
		}
		try {
			old = (MessageDigest) md.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		try {
			byte temp[] = md.digest();
			char str[] = new char[temp.length * 2];
			int k = 0;
			for (int i = 0; i < temp.length; i++) {
				byte byte0 = temp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			result = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		value_read = true;
		return result;
	}

	// testing
	static Set<String> values = new HashSet<String>();

	public static void main(String[] args) {

		for (int n = 0; n < 10000000; n++) {
			StringChecksum md5_1 = new StringChecksum(true);
			md5_1.update(n + "");
			values.add(md5_1.getChecksumValue());
		}
		System.out.println("suhde:" + values.size() + "/1000 0000");
		/*
		 * StringChecksum md5_1 = new StringChecksum(); md5_1.update("A");
		 * md5_1.update("B"); System.out.println(md5_1.getChecksumValue());
		 * 
		 * 
		 * StringChecksum md5_11 = md5_1.copy(); md5_11.update("B");
		 * md5_11.update("A"); System.out.println(md5_11.getChecksumValue());
		 * 
		 * StringChecksum md5_12 = md5_1.copy(); md5_12.update("B");
		 * md5_12.update("A"); System.out.println(md5_12.getChecksumValue());
		 * System.out.println(md5_12.getChecksumValue());
		 */

	}

}
