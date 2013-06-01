/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * Thrown when a problem occurs with the name of a Tag.
 * @author Timothy Oltjenbruns
 * @version 0.5, 05/28/2013
 * @see Tag
 */
public class NBTNameException extends Exception {

    /**
     * Creates an exception to represent a problem with the tag's name or
     * lack of one.
     * @param message the detail message. The detail message is saved for
     * later retrieval by the {@link java.lang.Exeption#getMessage()} method.
     */
    public NBTNameException(String message) {
        super(message);
    }
}
