/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * A tag representing a signed, 16 bit, big endian short.
 * @author Timothy Oltjenbruns
 * @version 1.0, 05/29/2013
 * @see TagType#TAG_Short
 */
public class TagShort extends Tag {

    private final short payload;

    /**
     * Creates a TAG_Short.
     * @param name used to identify the tag within a compound tag
     * @param payload a 16 bit short
     * @see TagType#TAG_Short
     */
    public TagShort(String name, short payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_Short.
     * @return TAG_Short ID
     * @see TagType#TAG_Short
     */
    @Override
    public byte getID() {
        return TagType.TAG_Short.getID();
    }

    /**
     * Gets the TagType of TAG_Short.
     * @return TagType TAG_Short
     * @see TagType#TAG_Short
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_Short;
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
     * @return a 16 bit short
     */
    @Override
    public Short getPayload() {
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

        return "TAG_Short" + typeSuffix + ": " + Short.toString(payload);
    }
}
