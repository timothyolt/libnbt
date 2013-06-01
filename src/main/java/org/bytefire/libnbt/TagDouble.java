/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * A tag representing a signed, 64 bit, big endian,
 * IEEE 754-2008 floating point value.
 * @author Timothy Oltjenbruns
 * @version 0.5, 05/29/2013
 * @see TagType#TAG_Double
 */
public class TagDouble extends Tag {

    private final double payload;

    /**
     * Creates a TAG_Double.
     * @param name used to identify the tag within a compound tag
     * @param payload a 64 bit floating point
     * @see TagType#TAG_Double
     */
    public TagDouble(String name, double payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_Double.
     * @return TAG_Double ID
     * @see TagType#TAG_Double
     */
    @Override
    public byte getID() {
        return TagType.TAG_Double.getID();
    }

    /**
     * Gets the TagType of TAG_Double.
     * @return TagType TAG_Double
     * @see TagType#TAG_Double
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_Double;
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
     * @return a 64 bit floating point
     */
    @Override
    public Double getPayload() {
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

        return "TAG_Double" + typeSuffix + ": " + Double.toString(payload);
    }
}
