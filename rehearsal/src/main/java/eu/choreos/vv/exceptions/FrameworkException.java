package eu.choreos.vv.exceptions;

/**
 * Generic exception used mainly when underlying libraries throw some unclear exception
 * 
 * @author Leonardo Leite, Lucas Piva
 *
 */
public class FrameworkException extends Exception {

	private static final long serialVersionUID = -8372153536725153278L;

	private Exception originalException;
	
	public FrameworkException (Exception originalException) {

		super(originalException);
		this.originalException = originalException;
	}

	public FrameworkException(String message) {
		super(message);
	}

	public Exception getOriginalException() {
		return originalException;
	}

	
}
