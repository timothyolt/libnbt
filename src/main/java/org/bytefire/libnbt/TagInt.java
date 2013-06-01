/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * A tag representing a signed, 32 bit, big endian integer.
 * @author Timothy Oltjenbruns
 * @version 1.0, 05/29/2013
 * @see TagType#TAG_Int
 */
public class TagInt extends Tag {

    private final int payload;

    /**
     * Creates a TAG_Int.
     * @param name used to identify the tag within a compound tag
     * @param payload a 32 bit integer
     * @see TagType#TAG_Int
     */
    public TagInt(String name, int payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_Int.
     * @return TAG_Int ID
     * @see TagType#TAG_Int
     */
    @Override
    public byte getID() {
        return TagType.TAG_Int.getID();
    }

    /**
     * Gets the TagType of TAG_Int.
     * @return TagType TAG_Int
     * @see TagType#TAG_Int
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_Int;
    }

    /**
     * Gets the name of the tag.
     * @return tag name, null if in a list
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the payload of the tag.
     * @return a 32 bit integer
     */
    @Override
    public Integer getPayload() {
        return payload;
    }

    /**
     * Converts the tag to a string.
     * @return string representation of the specified tag
     */
    @Override
    public String toString() {

        String typeSuffix = "";
        if (name != null) typeSuffix = "(\"" + name + "\")";

        return "TAG_Int" + typeSuffix + ": " + Integer.toString(payload);
    }
}
