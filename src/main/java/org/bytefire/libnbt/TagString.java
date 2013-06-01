/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

import java.nio.charset.Charset;

/**
 * A tag representing a byte array representing an UTF-8 string,
 * headed by a TAG_Short for length.
 * @author Timothy Oltjenbruns
 * @version 1.0, 05/29/2013
 * @see TagType#TAG_String
 */
public class TagString extends Tag {

    private final String payload;

    /**
     * The character set used by the NBT format (UTF-8).
     */
    public static final Charset charset = Charset.forName("UTF-8");

    /**
     * Creates a TAG_String.
     * @param name used to identify the tag within a compound tag
     * @param payload a UTF-8 string
     * @see TagType#TAG_String
     */
    public TagString(String name, String payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_String.
     * @return TAG_String ID
     * @see TagType#TAG_String
     */
    @Override
    public byte getID() {
        return TagType.TAG_String.getID();
    }

    /**
     * Gets the TagType of TAG_String.
     * @return TagType TAG_String
     * @see TagType#TAG_String
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_String;
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
     * @return a UTF-8 string
     */
    @Override
    public String getPayload() {
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

        return "TAG_String" + typeSuffix + ": " + payload;
    }
}
