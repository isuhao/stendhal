/*
 * @(#) src/games/stendhal/common/ConfigurableFactory.java
 *
 * $Id$
 */

package games.stendhal.common;

/**
 * A general object factory that accepts confguration attributes.
 */
public interface ConfigurableFactory {

	/**
	 * Create an object.
	 *
	 * @param	ctx		Configuration context.
	 *
	 * @return	A new object, or <code>null</code> if allowed by
	 *		the factory type.
	 *
	 * @throws	IllegalArgumentException
	 *				If there is a problem with the
	 *				attributes. The exception message
	 *				should be a value sutable for
	 *				meaningful user interpretation.
	 */
	public Object create(ConfigurableFactoryContext ctx) throws IllegalArgumentException;
}
