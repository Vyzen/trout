package com.eigenvektor.graph;

/**
 * An exception to throw when a graph algorithm fails.
 */
public class GraphAlgorithmException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1446136388665889655L;

	/**
	 * Create a new GraphAlgorithmException.
	 */
	public GraphAlgorithmException() {
		super();
	}

	/**
	 * Create a new GraphAlgorithmException with a specific message.
	 * 
	 * @param msg
	 *            The message.
	 */
	public GraphAlgorithmException(final String msg) {
		super(msg);
	}

}
