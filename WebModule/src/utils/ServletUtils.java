package utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import logic.enigma.Engine;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engine";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object engineManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static Engine getEngine(ServletContext servletContext){ //we need to make sure not to do get before we made an engine.
		synchronized (engineManagerLock) {
			if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME) == null) {
//				servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, new Engine());
				return null;
			}
		}
		return (Engine) servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME);
	}

	public static void setEngine(ServletContext servletContext, Engine engine) {
		synchronized (engineManagerLock) {
			if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, engine);
			}
		}
	}
}
