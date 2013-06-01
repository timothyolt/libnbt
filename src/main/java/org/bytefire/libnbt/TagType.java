/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * Convenience numeration for each tag type and ID byte.
 * @author Timothy Oltjenbruns
 * @version 0.5, 05/31/2013
 */
public enum TagType {
    /**
     * Unnamed, null-payload tag used to mark the end of a list.
     */
    TAG_End         ((byte)0),

    /**
     * A single signed byte, sometimes used for booleans.
     */
    TAG_Byte        ((byte)1),

    /**
     * A signed, 16 bit, big endian short.
     */
    TAG_Short       ((byte)2),

    /**
     * A signed, 32 bit, big endian integer.
     */
    TAG_Int         ((byte)3),

    /**
     * A signed, 64 bit, big endian long.
     */
    TAG_Long        ((byte)4),

    /**
     * A signed, 32 bit, big endian, IEEE 754-2008 floating point value.
     */
    TAG_Float       ((byte)5),

    /**
     * A signed, 64 bit, big endian, IEEE 754-2008 floating point value.
     */
    TAG_Double      ((byte)6),

    /**
     * An array of bytes of unspecified format, headed by a TAG_Int for length.
     */
    TAG_ByteArray   ((byte)7),

    /**
     * A byte array representing an UTF-8 string,
     * headed by a TAG_Short for length.
     */
    TAG_String      ((byte)8),

    /**
     * A sequential list of unnamed tags, headed by a TAG_Byte for tag type and
     * a TAG_Int for length.
     */
    TAG_List        ((byte)9),

    /**
     * A sequential list of uniquely named tags, terminated with a TAG_End.
     */
    TAG_Compound    ((byte)10),

    /**
     * An array of TAG_Int headed by a TAG_Int for length.
     */
    TAG_IntArray    ((byte)11);

    private byte id;
    TagType(byte id) {
        this.id = id;
    }

    /**
     * Gets the appropriate ID for the tag.
     * @return TagType's id
     */
    public byte getID() {
        return id;
    }

    /**
     * Finds a tag with the specified ID.
     * @param id ID to use while finding a tag
     * @throws NBTTagException when the byte does not refer to a known tag
     * @return TagType assigned to the ID given
     */
    public static TagType getTag(byte id) throws NBTTagException {
        switch(id) {
            case 0: return TagType.TAG_End;
            case 1: return TagType.TAG_Byte;
            case 2: return TagType.TAG_Short;
            case 3: return TagType.TAG_Int;
            case 4: return TagType.TAG_Long;
            case 5: return TagType.TAG_Float;
            case 6: return TagType.TAG_Double;
            case 7: return TagType.TAG_ByteArray;
            case 8: return TagType.TAG_String;
            case 9: return TagType.TAG_List;
            case 10: return TagType.TAG_Compound;
            case 11: return TagType.TAG_IntArray;
            default: throw new NBTTagException(Byte.toString(id)
                        + " does not represent a valid tag.");
        }
    }
}
