/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import static org.bytefire.libnbt.TagType.*;

/**
 * An InputStream for reading NBT files.
 * @author Timothy Oltjenbruns
 * @version 1.0, 05/31/2013
 * @see java.io.InputStream
 * @see java.io.Closable
 */
public class NBTInputStream implements Closeable {

    private DataInputStream in;

    private final boolean text;

    /**
     * Reads NBT data from a DataInputStream.
     * @param in an InputStream to use
     * @param gzip whether the file is gzipped
     * @param text whether to read text instead of NBT
     * @throws IOException when there is an IO error
     * @see java.io.DataInputStream
     * @see java.io.GZIPInputStream
     */
    public NBTInputStream(InputStream in, boolean gzip, boolean text)
        throws IOException {
        if (gzip) this.in = new DataInputStream(new GZIPInputStream(in));
        else this.in = new DataInputStream(in);
        this.text = text;
    }

    /**
     * Reads NBT data from a DataInputStream.
     * @param in an InputStream to use
     * @param gzip whether to gzip the file
     * @throws IOException when there is an IO error
     * @see java.io.DataInputStream
     * @see java.io.GZIPInputStream
     */
    public NBTInputStream(InputStream in, boolean gzip) throws IOException {
        if (gzip) this.in = new DataInputStream(new GZIPInputStream(in));
        else this.in = new DataInputStream(in);
        this.text = false;
    }

    /**
     * Reads NBT data from a gzipped DataInputStream.
     * @param in an InputStream to use
     * @throws IOException when there is an IO error
     * @see java.io.DataInputStream
     * @see java.io.GZIPInputStream
     */
    public NBTInputStream(InputStream in) throws IOException {
        this.in = new DataInputStream(new GZIPInputStream(in));
        this.text = false;
    }

    /**
     * Reads a single byte from the stream and interprets it as a TagType
     * @return type interpreted from byte
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TagType
     */
    public TagType readType() throws IOException, NBTTagException {
        return getTag(in.readByte());
    }

    /**
     * Skips through the stream a number of tags.
     * @param tags number of tags to skip
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TagType
     */
    public void skipTags(int tags) throws IOException, NBTTagException {
        skipTags(tags, null);
    }

    /**
     * Skips through the stream a number of tags, assuming the tag to be unnamed
     * and using the given type if the type is not null.
     * @param tags number of tags to skip
     * @param type TagType assumed when reading tags (null for named tags)
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TagType
     */
    public void skipTags(int tags, TagType type)
        throws IOException, NBTTagException {
        while (tags > 0) {
            boolean named = true;
            if (type == null) {
                type = readType();
                named = false;
            }
            switch(type) {
                case TAG_End:
                    break;
                case TAG_Byte:
                    if (named) in.skipBytes(in.readShort());
                    in.skipBytes(1);
                    break;
                case TAG_Short:
                    if (named) in.skipBytes(in.readShort());
                    in.skipBytes(2);
                    break;
                case TAG_Int:
                    if (named) in.skipBytes(in.readShort());
                    in.skipBytes(4);
                    break;
                case TAG_Long:
                    if (named) in.skipBytes(in.readShort());
                    in.skipBytes(8);
                    break;
                case TAG_Float:
                    if (named) in.skipBytes(in.readShort());
                    in.skipBytes(4);
                    break;
                case TAG_Double:
                    if (named) in.skipBytes(in.readShort());
                    in.skipBytes(8);
                    break;
                case TAG_ByteArray:
                    if (named) in.skipBytes(in.readShort());
                    in.skipBytes(in.readInt());
                    break;
                case TAG_String:
                    if (named) in.skipBytes(in.readShort());
                    in.skipBytes(in.readShort());
                    break;
                case TAG_List:
                    if (named) in.skipBytes(in.readShort());
                    TagType listId = readType();
                    skipTags(in.readShort(), listId);
                    break;
                case TAG_Compound:
                    if (named) in.skipBytes(in.readShort());
                    while (true) {
                        TagType compoundId = readType();
                        if (compoundId == TAG_End) break;
                        in.skipBytes(in.readShort());
                        skipTags(1, compoundId);
                    }
                case TAG_IntArray:
                    if (named) in.skipBytes(in.readShort());
                    in.skipBytes(in.readInt() * 4);
                    break;
            }
        }
    }

    /**
     * Reads the next tag in the stream to memory.
     * @return the tag read
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see Tag
     * @see TagType
     */
    public Tag readNextTag() throws IOException, NBTTagException {
        return readNextTag(0, null, true);
    }

    /**
     * Reads the next tag in the stream to memory.
     * @param offset number of tags to skip before reading a tag
     * @return the tag read
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see Tag
     * @see TagType
     */
    public Tag readNextTag(int offset) throws IOException, NBTTagException {
        return readNextTag(0, null, true);
    }

    /**
     * Reads the next tag in the stream to memory.
     * @param offset number of tags to skip before reading a tag
     * @param type all tags are skipped until one of this type is found and
     * read; set to null, the next tag is directly read
     * @return the tag read
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see Tag
     * @see TagType
     */
    public Tag readNextTag(int offset, TagType type)
        throws IOException, NBTTagException {
        return readNextTag(0, type, true);
    }

    /**
     * Reads the next tag in the stream to memory.
     * @param offset number of tags to skip before reading a tag
     * @param type if named, all tags are skipped until one of this type is
     * found and read; if unnamed, the type it assumes the tag to be; set to
     * null, the next tag is directly read
     * @param named whether the tag should be named
     * @return the tag read
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see Tag
     * @see TagType
     */
    public Tag readNextTag(int offset, TagType type, boolean named)
        throws IOException, NBTTagException {
        skipTags(offset);
        TagType readType;
        if (named) {
            readType = readType();
            if (type != null) while (!readType.equals(type)) {
                in.skipBytes(in.readShort());
                skipTags(1, readType);
            }
        }
        else readType = type;
        switch (readType) {
            case TAG_End:
                return readEnd();
            case TAG_Byte:
                return readByte(named);
            case TAG_Short:
                return readShort(named);
            case TAG_Int:
                return readInt(named);
            case TAG_Long:
                return readLong(named);
            case TAG_Float:
                return readFloat(named);
            case TAG_Double:
                return readDouble(named);
            case TAG_ByteArray:
                return readByteArray(named);
            case TAG_String:
                return readString(named);
            case TAG_List:
                return readList(named);
            case TAG_Compound:
                return readCompound(named);
            case TAG_IntArray:
                return readIntArray(named);
            default: return null; //Does not happen, unless a new tag is added
                                  //and this has not been updated
        }
    }

    /**
     * Reads the next bytes in the stream as a TAG_End (excluding the tag type).
     * @return a TAG_End from the stream
     * @see TAG_End
     */
    public TagEnd readEnd() {
        return new TagEnd();
    }

    /**
     * Reads the next bytes in the stream as a named TAG_Byte
     * (excluding the tag type).
     * @return a TAG_Byte from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Byte
     */
    public TagByte readByte() throws IOException {
        return readByte(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_Byte
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_Byte from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Byte
     */
    public TagByte readByte(boolean named) throws IOException {
        String name = null;
        if (named) name = in.readUTF();
        return new TagByte(name, in.readByte());
    }

    /**
     * Reads the next bytes in the stream as a named TAG_Short
     * (excluding the tag type).
     * @return a TAG_Short from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Short
     */
    public TagShort readShort() throws IOException {
        return readShort(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_Short
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_Short from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Short
     */
    public TagShort readShort(boolean named) throws IOException {
        String name = null;
        if (named) name = in.readUTF();
        return new TagShort(name, in.readShort());
    }

    /**
     * Reads the next bytes in the stream as a named TAG_Int
     * (excluding the tag type).
     * @return a TAG_Int from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Int
     */
    public TagInt readInt() throws IOException {
        return readInt(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_Int
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_Int from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Int
     */
    public TagInt readInt(boolean named) throws IOException {
        String name = null;
        if (named) name = in.readUTF();
        return new TagInt(name, in.readInt());
    }

    /**
     * Reads the next bytes in the stream as a named TAG_Long
     * (excluding the tag type).
     * @return a TAG_Long from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Long
     */
    public TagLong readLong() throws IOException {
        return readLong(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_Long
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_Long from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Long
     */
    public TagLong readLong(boolean named) throws IOException {
        String name = null;
        if (named) name = in.readUTF();
        return new TagLong(name, in.readLong());
    }

    /**
     * Reads the next bytes in the stream as a named TAG_Float
     * (excluding the tag type).
     * @return a TAG_Float from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Float
     */
    public TagFloat readFloat() throws IOException {
        return readFloat(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_Float
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_Float from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Float
     */
    public TagFloat readFloat(boolean named) throws IOException {
        String name = null;
        if (named) name = in.readUTF();
        return new TagFloat(name, in.readFloat());
    }

    /**
     * Reads the next bytes in the stream as a named TAG_Double
     * (excluding the tag type).
     * @return a TAG_Double from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Double
     */
    public TagDouble readDouble() throws IOException {
        return readDouble(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_Double
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_Double from the stream
     * @throws IOException when there is an IO error
     * @see TAG_Double
     */
    public TagDouble readDouble(boolean named) throws IOException {
        String name = null;
        if (named) name = in.readUTF();
        return new TagDouble(name, in.readDouble());
    }

    /**
     * Reads the next bytes in the stream as a named TAG_ByteArray
     * (excluding the tag type).
     * @return a TAG_ByteArray from the stream
     * @throws IOException when there is an IO error
     * @see TAG_ByteArray
     */
    public TagByteArray readByteArray() throws IOException {
        return readByteArray(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_ByteArray
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_ByteArray from the stream
     * @throws IOException when there is an IO error
     * @see TAG_ByteArray
     */
    public TagByteArray readByteArray(boolean named) throws IOException {
        String name = null;
        if (named) name = in.readUTF();
        byte[] array = new byte[in.readInt()];
        in.read(array);
        return new TagByteArray(name, array);
    }

    /**
     * Reads the next bytes in the stream as a named TAG_String
     * (excluding the tag type).
     * @return a TAG_String from the stream
     * @throws IOException when there is an IO error
     * @see TAG_String
     */
    public TagString readString() throws IOException {
        return readString(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_String
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_String from the stream
     * @throws IOException when there is an IO error
     * @see TAG_String
     */
    public TagString readString(boolean named) throws IOException {
        String name = null;
        if (named) name = in.readUTF();
        return new TagString(name, in.readUTF());
    }

    /**
     * Reads the next bytes in the stream as a named TAG_List
     * (excluding the tag type).
     * @return a TAG_List from the stream
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TAG_List
     */
    public TagList readList() throws IOException, NBTTagException {
        return readList(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_List
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_List from the stream
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TAG_List
     */
    public TagList readList(boolean named) throws IOException, NBTTagException {
        String name = null;
        if (named) name = in.readUTF();
        TagType listType = readType();
        int length = in.readInt();
        List<Tag> list = new ArrayList<Tag>();
        for (int i = 0; i < length; i++)
            list.add(readNextTag(0, listType, false));
        try {
            return new TagList(name, list);
        } catch (NBTNameException ex) {
            Logger.getLogger(NBTInputStream.class.getName())
                .log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Reads the next bytes in the stream as a named TAG_Compound
     * (excluding the tag type).
     * @return a TAG_Compound from the stream
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TAG_Compound
     */
    public TagCompound readCompound() throws IOException, NBTTagException {
        return readCompound(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_Compound
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_Compound from the stream
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TAG_Compound
     */
    public TagCompound readCompound(boolean named)
        throws IOException, NBTTagException {
        String name = null;
        if (named) name = in.readUTF();
        Map<String, Tag> compound = new HashMap<String, Tag>();
        while (true){
            Tag subTag = readNextTag();
            if (subTag.getTagType().equals(TAG_End)) break;
            compound.put(subTag.getName(), subTag);
        }
        return new TagCompound(name, compound);
    }

    /**
     * Reads the next bytes in the stream as a named TAG_IntArray
     * (excluding the tag type).
     * @return a TAG_IntArray from the stream
     * @throws IOException when there is an IO error
     * @see TAG_IntArray
     */
    public TagIntArray readIntArray() throws IOException {
        return readIntArray(true);
    }

    /**
     * Reads the next bytes in the stream as a TAG_IntArray
     * (excluding the tag type).
     * @param named whether the tag is named
     * @return a TAG_IntArray from the stream
     * @throws IOException when there is an IO error
     * @see TAG_IntArray
     */
    public TagIntArray readIntArray(boolean named) throws IOException {
        String name = null;
        if (named) name = in.readUTF();
        int[] array = new int[in.readInt()];
        for (int i = 0; i < array.length; i++) array[i] = in.readInt();
        return new TagIntArray(name, array);
    }

    /**
     * Closes this InputStream and releases any system resources
     * associated with the stream.
     * @throws IOException when an IO error occurs
     * @see java.io.DataInputStream
     */
    public void close() throws IOException {
        in.close();
    }
}
