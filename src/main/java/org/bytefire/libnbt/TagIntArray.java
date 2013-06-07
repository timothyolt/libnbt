/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * An array of integers, headed by a TAG_Int for length.
 * @author Timothy Oltjenbruns
 * @version 1.1, 06/03/2013
 * @see TagType#TAG_Int
 * @see TagType#TAG_IntArray
 */
public class TagIntArray extends Tag {

    private final int[] payload;

    /**
     * Creates a TAG_IntArray.
     * @param name used to identify the tag within a compound tag
     * @param payload an array of integers
     * @see TagType#TAG_IntArray
     */
    public TagIntArray(String name, int[] payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_IntArray.
     * @return TAG_IntArray ID
     * @see TagType#TAG_IntArray
     */
    @Override
    public byte getID() {
        return TagType.TAG_IntArray.getID();
    }

    /**
     * Gets the TagType of TAG_IntArray.
     * @return TagType TAG_IntArray
     * @see TagType#TAG_IntArray
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_IntArray;
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
    public int[] getPayload() {
        return payload;
    }

    /**
     * Converts the tag to a string.
     * @return string representation of the specified tag
     */
    @Override
    public String toString() {
        return toString(0);
    }

    /**
     * Converts the tag to a string (for internal use only).
     * @param depth indentation key
     * @return string representation of the specified tag
     */
    protected String toString(int depth) {
        String eol = System.getProperty("line.separator");

        String typeSuffix = "";
        if (name != null) typeSuffix = "(\"" + name + "\")";

        String integers = "";
        for(int i = 0; i < depth; i++) integers += "    ";
        integers += "{";
        for (int i = 0; i < payload.length; i++) {
            if (i % 16 == 0){
                integers += eol;
                for(int ii = 0; ii < depth + 1; ii++) integers += "    ";
            }
            else integers += ", ";
            integers += Integer.toString(payload[i]);
        }
        integers += eol;
        for(int i = 0; i < depth; i++) integers += "    ";
        integers += "}";

        return "TAG_ByteArray" + typeSuffix + ": ["
            + Integer.toString(payload.length) + " bytes]"
            + eol + integers;
    }
}
