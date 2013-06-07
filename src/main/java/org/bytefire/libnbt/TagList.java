/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

import java.util.ArrayList;
import java.util.List;
/**
 * A sequential list of unnamed tags, headed by a TAG_Byte for tag type and
 * a TAG_Int for length.
 * @author Timothy Oltjenbruns
 * @version 1.1, 06/03/2013
 * @see TagType#TAG_List
 */
public class TagList extends Tag {

    private final List<Tag> payload;

    private final TagType type;

    /**
     * Creates a TAG_List.
     * @param name name used to identify the tag within a compound tag
     * @param type TagType to be used for the list
     * @param payload an List of Tags identified by numerical order
     * @throws NBTTagException when a tag does not match the given type
     * @throws NBTNameException when a named tag is given
     * @see java.util.List
     * @see TagType
     * @see TagType#TAG_List
     */
    public TagList(String name, TagType type, List<Tag> payload)
        throws NBTTagException, NBTNameException {
        super(name);
        this.type = type;
        for (Tag tag : payload){
            TagType tagType = tag.getTagType();
            if (!tagType.equals(type)) throw new
                NBTTagException(tagType.toString() +
                " tag type does not match list type " + type.toString());
            if (tag.getName() != null) throw new
                NBTNameException(
                "Named tag given where unnamed tag was expected");
        }
        this.payload = payload;
    }

    /**
     * Creates a TAG_List with the TagType of the first tag.
     * @param name name used to identify the tag within a compound tag
     * @param payload an ArrayList of Tags identified by numerical order
     * @throws NBTTagException when a tag does not match the first type
     * @throws NBTNameException when a named tag is given
     * @see java.util.ArrayList
     * @see TagType
     * @see TagType#TAG_List
     */
    public TagList(String name, List<Tag> payload)
        throws NBTTagException, NBTNameException {
        super(name);
        this.type = payload.get(0).getTagType();
        for (Tag tag : payload) {
            TagType tagType = tag.getTagType();
            if (!tagType.equals(type)) throw
                new NBTTagException(tagType.toString()
                + " tag type does not match list type " + type.toString());
            if (tag.getName() != null) throw new NBTNameException(
                "Named tag given where unnamed tag was expected");
        }
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_List.
     * @return TAG_List ID
     * @see TagType#TAG_List
     */
    @Override
    public byte getID() {
        return TagType.TAG_List.getID();
    }

    /**
     * Gets the TagType of TAG_List.
     * @return TagType TAG_List
     * @see TagType#TAG_List
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_List;
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
     * @return an ArrayList of Tags identified by numerical order
     */
    @Override
    public List<Tag> getPayload() {
        return payload;
    }

    /**
     * Gets the TagType the list stores.
     * @return TagType the list stores
     */
    public TagType getType() {
        return type;
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
        final String eol = System.getProperty("line.separator");

        String typeSuffix = "";
        if (name != null) typeSuffix = "(\"" + name + "\")";

        String subString = "";
        for(int i = 0; i < depth; i++) subString += "    ";
        subString += "{" + eol;
        for (Tag tag : payload) {
            for(int ii = 0; ii < depth + 1; ii++) subString += "    ";
            switch (tag.getTagType()){
                case TAG_Compound:
                    subString += ((TagCompound) tag).toString(depth + 1) + eol;
                    break;
                case TAG_List:
                    subString += ((TagList) tag).toString(depth + 1) + eol;
                    break;
                case TAG_ByteArray:
                    subString += ((TagByteArray) tag).toString(depth + 1) + eol;
                    break;
                case TAG_IntArray:
                    subString += ((TagIntArray) tag).toString(depth + 1) + eol;
                    break;
                default:
                    subString += tag.toString() + eol;
            }
        }
        for(int i = 0; i < depth; i++) subString += "    ";
        subString += "}";

        return "TAG_List" + typeSuffix + ": "
            + Integer.toString(payload.size()) + " entries of type "
            + type.toString() + eol + subString;
    }
}
