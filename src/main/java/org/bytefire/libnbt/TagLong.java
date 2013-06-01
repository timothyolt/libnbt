/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * A tag representing a signed, 64 bit, big endian long.
 * @author Timothy Oltjenbruns
 * @version 1.0, 05/29/2013
 * @see TagType#TAG_Long
 */
public class TagLong extends Tag {

    private final long payload;

    /**
     * Creates a TAG_Long.
     * @param name used to identify the tag within a compound tag
     * @param payload a 64 bit long
     * @see TagType#TAG_Long
     */
    public TagLong(String name, long payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_Long.
     * @return TAG_Long ID
     * @see TagType#TAG_Long
     */
    @Override
    public byte getID() {
        return TagType.TAG_Long.getID();
    }

    /**
     * Gets the TagType of TAG_Long.
     * @return TagType TAG_Long
     * @see TagType#TAG_Long
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_Long;
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
     * @return a 64 bit long
     */
    @Override
    public Long getPayload() {
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

        return "TAG_Long" + typeSuffix + ": " + Long.toString(payload);
    }
}
