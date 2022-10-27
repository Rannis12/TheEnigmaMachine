package utils;

import jakarta.servlet.ServletContext;
import logic.enigma.Engine;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engine";

//	private static String toDecode;
//	private static String toEncode;

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object engineManagerLock = new Object();
//	private static final Object decodeStringLock = new Object();
//	private static final Object encodeStringLock = new Object();


	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static Engine getEngine(ServletContext servletContext, String userName){ //we need to make sure not to do get before we made an engine.

		UserManager userManager = getUserManager(servletContext);
		synchronized (engineManagerLock) {
			if (userManager.getUBoat(userName) == null) {
				userManager.addUBoat(userName);
				return null;
			}
		}
		return userManager.getUBoat(userName).getEngine();
	}

	public static void setEngine(ServletContext servletContext, Engine engine) {
		synchronized (engineManagerLock) {
			if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, engine);
			}
		}
	}

	/*public static void setToEncodeString(String toEncode) {
		synchronized (encodeStringLock) {
			ServletUtils.toEncode = toEncode;
		}
	}*/

	/*public static String getToEncodeString() {
		return toEncode;
	}*/

	/*public static void setToDecodeString(String toDecode) {
		synchronized (decodeStringLock) {
			ServletUtils.toDecode = toDecode;
		}
	}*/

	/*public static String getToDecodeString() {
		return toDecode;
	}*/




}
