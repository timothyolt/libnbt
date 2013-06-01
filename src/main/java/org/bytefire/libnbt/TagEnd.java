/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

/**
 * Unnamed, null-payload tag used to mark the end of a list.
 * @author Timothy Oltjenbruns
 * @version 1.0, 05/22/2013
 * @see TagType#TAG_End
 */
public class TagEnd extends Tag {

    /**
     * Creates a TAG_End.
     * @see TagType#TAG_End
     */
    public TagEnd() {
        super(null);
    }

    /**
     * Gets the ID of TAG_End.
     * @return TAG_End ID
     * @see TagType#TAG_End
     */
    @Override
    public byte getID() {
        return TagType.TAG_End.getID();
    }

    /**
     * Gets the TagType of TAG_End.
     * @return TagType TAG_End
     * @see TagType#TAG_End
     */
    @Override
    public TagType getTagType() {
        return TagType.TAG_End;
    }

    /**
     * Gets the name of the tag (always null for TAG_End).
     * @return null
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Gets the payload of the tag (always null for TAG_End).
     * @return null
     */
    @Override
    public Object getPayload() {
        return null;
    }

    /**
     * Converts the tag to a string.
     * @return string representation of the specified tag
     */
    @Override
    public String toString() {
        return "}" + System.getProperty("line.separator");
    }
}
