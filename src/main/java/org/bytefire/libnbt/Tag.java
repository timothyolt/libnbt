/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * Abstract class that represents a single tag.
 * @author timn
 * @version 1.0, 05/29/2013
 * @see TagType
 */
public abstract class Tag {

    /**
     * The name of the tag, null if unnamed.
     */
    protected final String name;

    /**
     * Creates a tag with the name specified.
     * @param name the name to give the tag, null for unnamed
     * @see TagType
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Abstract method to retrieve the ID of the tag it represents.
     * @return ID of the tag
     * @see TagType
     */
    public abstract byte getID();

    /**
     * Abstract method to retrieve the TagType of the tag it represents.
     * @return TagType of the tag
     * @see TagType
     */
    public abstract TagType getTagType();

    /**
     * Gets the name of the tag.
     * @return tag name, null if in a list
     */
    public abstract String getName();

    /**
     * Abstract method to retrieve the payload of the tag, if any.
     * @return the payload of the tag, null if none
     */
    public abstract Object getPayload();

    /**
     * Converts the tag to a string.
     * @return string representation of the specified tag
     */
    @Override
    public abstract String toString();
}
