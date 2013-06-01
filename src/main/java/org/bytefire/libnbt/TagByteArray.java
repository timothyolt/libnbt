/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * An array of bytes of unspecified format, headed by a TAG_Int for length.
 * @author Timothy Oltjenbruns
 * @version 0.5, 05/29/2013
 * @see TagType#TAG_ByteArray
 * @see TagType#TAG_Byte
 * @see TagType#TAG_Int
 */
public class TagByteArray extends Tag {

    private final byte[] payload;

    /**
     * Creates a TAG_ByteArray.
     * @param name used to identify the tag within a compound tag
     * @param payload an array of bytes
     * @see TagType#TAG_ByteArray
     */
    public TagByteArray(String name, byte[] payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_ByteArray.
     * @return TAG_ByteArray ID
     * @see TagType#TAG_ByteArray
     */
    @Override
    public byte getID() {
        return TagType.TAG_ByteArray.getID();
    }

    /**
     * Gets the TagType of TAG_ByteArray.
     * @return TagType TAG_ByteArray
     * @see TagType#TAG_ByteArray
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_ByteArray;
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
     * @return an array of bytes
     */
    @Override
    public byte[] getPayload() {
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

        String bytes = "";
        for (int i = 0; i < payload.length; i++) {
            if (i != 0) bytes += ", ";
            bytes += Byte.toString(payload[i]);
        }

        return "TAG_ByteArray" + typeSuffix + ": ["
            + Integer.toString(payload.length) + " bytes]"
            + System.getProperty("line.separator") + bytes;
    }
}
