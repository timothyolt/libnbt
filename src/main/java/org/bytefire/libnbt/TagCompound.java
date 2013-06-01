/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import static org.bytefire.libnbt.TagType.TAG_Compound;
import static org.bytefire.libnbt.TagType.TAG_List;

/**
 * A tag representing a sequential list of uniquely named tags,
 * terminated with a TAG_End.
 * @author Timothy Oltjenbruns
 * @version 0.5, 05/29/2013
 * @see TagType#TAG_Compound
 */
public class TagCompound extends Tag {

    private final Map<String, Tag> payload;

    /**
     * Creates a TAG_Compound.
     * @param name name used to identify the tag within a compound tag
     * @param payload a HashMap of Tags keyed by tag name
     * @see java.util.HashMap
     * @see Tag
     * @see TagType#TAG_Compound
     */
    public TagCompound(String name, Map<String, Tag> payload) {
        super(name);
        this.payload = payload;
    }

    /**
     * Gets the ID of TAG_Compound.
     * @return TAG_Compound ID
     * @see TagType#TAG_Compound
     */
    @Override
    public byte getID() {
        return TagType.TAG_Compound.getID();
    }

    /**
     * Gets the TagType of TAG_Compound.
     * @return TagType TAG_Compound
     * @see TagType#TAG_Compound
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_Compound;
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
     * @return a HashMap of Tags keyed by tag name
     */
    @Override
    public Map<String, Tag> getPayload() {
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
        final String eol = System.getProperty("line.separator");

        List<Tag> subTags = new ArrayList<Tag>(payload.values());

        String typeSuffix = "";
        if (name != null) typeSuffix = "(\"" + name + "\")";

        String subString = "";
        for(int i = 0; i < depth; i++) subString += "    ";
        subString += "{" + eol;
        for (Map.Entry<String, Tag> mapEntry : payload.entrySet()) {
            Tag tag = mapEntry.getValue();
            for(int i = 0; i < depth + 1; i++) subString += "    ";
            if (tag.getTagType().equals(TAG_Compound))
                subString += ((TagCompound) tag).toString(depth + 1) + eol;
            else if (tag.getTagType().equals(TAG_List))
                subString += ((TagList) tag).toString(depth + 1) + eol;
            else subString += tag.toString() + eol;
        }
        for(int i = 0; i < depth; i++) subString += "    ";
        subString += "}";

        return "TAG_Compound" + typeSuffix + ": "
            + Integer.toString(payload.size()) + " entries" + eol + subString;
    }
}
