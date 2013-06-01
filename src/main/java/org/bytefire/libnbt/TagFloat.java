/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * A tag representing a signed, 32 bit, big endian,
 * IEEE 754-2008 floating point value.
 * @author Timothy Oltjenbruns
 * @version 0.5, 05/29/2013
 * @see TagType#TAG_Float
 */
public class TagFloat extends Tag {

    private final float payload;

    /**
     * Creates a TAG_Float.
     * @param name used to identify the tag within a compound tag
     * @param payload a 32 bit floating point
     * @see TagType#TAG_Float
     */
    public TagFloat(String name, float payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_Float.
     * @return TAG_Float ID
     * @see TagType#TAG_Float
     */
    @Override
    public byte getID() {
        return TagType.TAG_Float.getID();
    }

    /**
     * Gets the TagType of TAG_Float.
     * @return TagType TAG_Float
     * @see TagType#TAG_Float
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_Float;
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
     * @return a 32 bit floating point
     */
    @Override
    public Float getPayload() {
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

        return "TAG_Float" + typeSuffix + ": " + Float.toString(payload);
    }
}
