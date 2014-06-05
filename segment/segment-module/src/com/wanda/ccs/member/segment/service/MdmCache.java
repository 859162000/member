package com.wanda.ccs.member.segment.service;

import java.io.Serializable;


/**
 *
 * Interface for a Cache where data mappings are string keys <BR>
 */
public interface MdmCache<T> extends Serializable {

	/**
	 *
	 * Associates the specified value with the specified key for this cache. <BR/>
	 *
	 * @param key
	 *            Key with which the specified value is to be associated.
	 * @param value
	 *            Value to be associated with the specified key.
	 * @return previous value associated with specified key, or null if there
	 *         was no mapping for key.
	 * @since
	 */
	public T put(String key, T value);

	/**
	 *
	 * Get the value which be associated with specified key.<br/>
	 *
	 * @param key
	 * @return The value which be associated with specified key.
	 * @since
	 */
	public T get(String key);

	/**
	 *
	 * Removes the mapping for this key from this cache <BR/>
	 *
	 * @return previous value associated with specified key.
	 * @since
	 */
	public T remove(String key);


	/**
	 *
	 * Remove all mappings from this cache. The cache will be empty after this call returns.

	 */
	public void clear();

	/**
	 *
	 * Return Returns the number of cached value in this cache.
	 *
	 * @return
	 * @since
	 */
	public int size();
}
