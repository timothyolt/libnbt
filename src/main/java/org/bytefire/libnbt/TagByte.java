/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * A tag representing single signed byte, sometimes used for booleans.
 * @author Timothy Oltjenbruns
 * @version 1.0, 05/22/2013
 * @see TagType#TAG_Byte
 */
public class TagByte extends Tag {

    private final byte payload;

    /**
     * Creates a TAG_Byte.
     * @param name name used to identify the tag within a compound tag
     * @param payload a single byte
     * @see TagType#TAG_Byte
     */
    public TagByte(String name, byte payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_Byte.
     * @return TAG_Byte ID
     * @see TagType#TAG_Byte
     */
    @Override
    public byte getID() {
        return TagType.TAG_Byte.getID();
    }

    /**
     * Gets the TagType of TAG_Byte.
     * @return TagType TAG_Byte
     * @see TagType#TAG_Byte
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_Byte;
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
     * @return a single signed byte
     */
    @Override
    public Byte getPayload() {
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

        return "TAG_Byte" + typeSuffix + ": " + Byte.toString(payload);
    }
}
