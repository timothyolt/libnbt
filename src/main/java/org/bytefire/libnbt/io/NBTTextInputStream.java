/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytefire.libnbt.*;
import static org.bytefire.libnbt.TagType.*;

/**
 * An InputStream for reading text files into NBT data.
 * @author Timothy Oltjenbruns
 * @version 1.1, 06/06/2013
 * @see java.io.InputStream
 * @see java.io.Closable
 */
public class NBTTextInputStream implements Closeable {

    private BufferedReader in;

    private int maxLimit;

    private String eol;

    /**
     * Reads NBT data from a BufferedReader.
     * @param in an InputStream to use
     * @throws IOException when there is an IO error
     * @see java.io.DataInputStream
     * @see java.io.GZIPInputStream
     */
    public NBTTextInputStream(InputStream in) throws IOException {
        this.in =
            new BufferedReader(new InputStreamReader(in, TagString.charset));
        this.maxLimit = 256;
        eol = System.getProperty("line.separator");
    }

    /**
     * Reads NBT data from a BufferedReader.
     * @param in an InputStream to use
     * @param maxLimit used for skipping whitespace and reading names
     * @throws IOException when there is an IO error
     * @see java.io.DataInputStream
     * @see java.io.GZIPInputStream
     */
    public NBTTextInputStream(InputStream in, int maxLimit) throws IOException {
        this.in =
            new BufferedReader(new InputStreamReader(in, TagString.charset));
        this.maxLimit = maxLimit;
        eol = System.getProperty("line.separator");
    }

    private void skipWSpc(int limit) throws IOException {
        for (int i = 0; i < limit; i++){
            in.mark(2);
            char temp = (char)in.read();
            if (temp != ' '){
                in.reset();
                break;
            }
        }
    }

    private String readUntil(char end, int limit) throws IOException {
        return readUntil(new char[]{end}, limit);
    }

    private String readUntil(char[] end, int limit) throws IOException {
        char[] buffer = new char[limit];
        loop: for (int i = 0; i < limit; i++) {
            in.mark(2);
            char temp = (char)in.read();
            for(char e : end) if (e == temp) {
                in.reset();
                buffer = Arrays.copyOf(buffer, i);
                break loop;
            }
            else buffer[i] = temp;
        }
        return String.copyValueOf(buffer);
    }

    /**
     * Reads a single byte from the stream and interprets it as a TagType
     * @return type interpreted from byte
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TagType
     */
    public TagType readType() throws IOException, NBTTagException {
        skipWSpc(maxLimit);
        String tag = readUntil(new char[]{'(', ':'}, 16);
        TagType type;
        try {
            type = TagType.valueOf(tag);
        } catch (IllegalArgumentException e) {
            throw new NBTTagException(tag + " does not represent a valid tag.");
        }
        return type;
        //} else throw new NBTTagException("No tag found where expected.");
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
        for (;tags > 0;tags--) {
            if (type == null) type = readType();
            switch(type) {
                case TAG_End:
                    break;
                case TAG_Byte:
                case TAG_Short:
                case TAG_Int:
                case TAG_Long:
                case TAG_Float:
                case TAG_Double:
                    in.readLine();
                    break;
                case TAG_ByteArray: {
                    readUntil(':', maxLimit);
                    String entries = in.readLine();
                    int lines = (int)Math.ceil(
                        Integer.valueOf(entries.split(" ")[1])/16);
                    for (int i = 0; i < lines + 2; i++) in.readLine();
                    break;}
                case TAG_String:
                    in.readLine();
                    break;
                case TAG_List: {
                    readUntil(':', maxLimit);
                    String entries = in.readLine();
                    int lines = (int)Math.ceil(
                        Integer.valueOf(entries.split(" ")[1])/16);
                    in.readLine();
                    skipTags(lines);
                    in.readLine();
                    break;}
                case TAG_Compound: {
                    readUntil(':', maxLimit);
                    String entries = in.readLine();
                    int lines = (int)Math.ceil(
                        Integer.valueOf(entries.split(" ")[1])/16);
                    in.readLine();
                    skipTags(lines);
                    in.readLine();
                    break;}
                case TAG_IntArray:
                    readUntil(':', maxLimit);
                    String entries = in.readLine();
                    int lines = (int)Math.ceil(
                        Integer.valueOf(entries.split(" ")[1])/16);
                    for (int i = 0; i < lines + 2; i++) in.readLine();
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
        return readNextTag(0, null);
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
        return readNextTag(0, null);
    }

    /**
     * Reads the next tag in the stream to memory.
     * @param offset number of tags to skip before reading a tag
     * @param type if named, all tags are skipped until one of this type is
     * found and read; if unnamed, the type it assumes the tag to be; set to
     * null, the next tag is directly read
     * @return the tag read
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see Tag
     * @see TagType
     */
    public Tag readNextTag(int offset, TagType type)
        throws IOException, NBTTagException {
        skipTags(offset);
        TagType readType;
        if (type != null) {
            readType = readType();
            for (int i = 0; !readType.equals(type); i++) {
                if (i != 0) readType = readType();
                skipTags(1, readType);
            }
        }
        else readType = readType();
        switch (readType) {
            case TAG_End:
                return readEnd();
            case TAG_Byte:
                return readByte();
            case TAG_Short:
                return readShort();
            case TAG_Int:
                return readInt();
            case TAG_Long:
                return readLong();
            case TAG_Float:
                return readFloat();
            case TAG_Double:
                return readDouble();
            case TAG_ByteArray:
                return readByteArray();
            case TAG_String:
                return readString();
            case TAG_List:
                return readList();
            case TAG_Compound:
                return readCompound();
            case TAG_IntArray:
                return readIntArray();
            default: return null; //Does not happen, unless a new tag is added
                                  //and this has not been updated
        }
    }

    /**
     * Reads the next bytes in the stream as a TAG_End (excluding the tag type).
     * @return a TAG_End from the stream
     * @see TagType#TAG_End
     */
    public TagEnd readEnd() {
        return new TagEnd();
    }

    /**
     * Reads the next bytes in the stream as a TAG_Byte
     * (excluding the tag type).
     * @return a TAG_Byte from the stream
     * @throws IOException when there is an IO error
     * @see TagType#TAG_Byte
     */
    public TagByte readByte() throws IOException {
        String name = null;
        if ((char)in.read() == '('){
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        in.skip(2);
        return new TagByte(name, Byte.valueOf(in.readLine()));
    }

    /**
     * Reads the next bytes in the stream as a TAG_Short
     * (excluding the tag type).
     * @return a TAG_Short from the stream
     * @throws IOException when there is an IO error
     * @see TagType#TAG_Short
     */
    public TagShort readShort() throws IOException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        in.skip(2);
        return new TagShort(name, Short.valueOf(in.readLine()));
    }

    /**
     * Reads the next bytes in the stream as a TAG_Int
     * (excluding the tag type).
     * @return a TAG_Int from the stream
     * @throws IOException when there is an IO error
     * @see TagType#TAG_Int
     */
    public TagInt readInt() throws IOException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        in.skip(2);
        return new TagInt(name, Integer.valueOf(in.readLine()));
    }

    /**
     * Reads the next bytes in the stream as a TAG_Long
     * (excluding the tag type).
     * @return a TAG_Long from the stream
     * @throws IOException when there is an IO error
     * @see TagType#TAG_Long
     */
    public TagLong readLong() throws IOException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        in.skip(2);
        return new TagLong(name, Long.valueOf(in.readLine()));
    }

    /**
     * Reads the next bytes in the stream as a TAG_Float
     * (excluding the tag type).
     * @return a TAG_Float from the stream
     * @throws IOException when there is an IO error
     * @see TagType#TAG_Float
     */
    public TagFloat readFloat() throws IOException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        in.skip(2);
        return new TagFloat(name, Float.valueOf(in.readLine()));
    }

    /**
     * Reads the next bytes in the stream as a TAG_Double
     * (excluding the tag type).
     * @return a TAG_Double from the stream
     * @throws IOException when there is an IO error
     * @see TagType#TAG_Double
     */
    public TagDouble readDouble() throws IOException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        in.skip(2);
        return new TagDouble(name, Double.valueOf(in.readLine()));
    }

    /**
     * Reads the next bytes in the stream as a TAG_ByteArray
     * (excluding the tag type).
     * @return a TAG_ByteArray from the stream
     * @throws IOException when there is an IO error
     * @see TagType#TAG_ByteArray
     */
    public TagByteArray readByteArray() throws IOException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        String entries = in.readLine().split(" ")[1];
        int length = Integer.valueOf(entries.substring(1, entries.length()));
        int lines = (int)Math.ceil(length/16);
        byte[] array = new byte[length];
        in.readLine();
        for (int i = 0; i < lines + 1; i++) {
            skipWSpc(maxLimit);
            String[] line = in.readLine().split(", ");
            for (int cu = 0; cu < line.length; cu++)
                array[i*16+cu] = Byte.valueOf(line[cu]);
        }
        in.readLine();
        return new TagByteArray(name, array);
    }

    /**
     * Reads the next bytes in the stream as a TAG_String
     * (excluding the tag type).
     * @return a TAG_String from the stream
     * @throws IOException when there is an IO error
     * @see TagType#TAG_String
     */
    public TagString readString() throws IOException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        in.skip(2);
        return new TagString(name, in.readLine());
    }

    /**
     * Reads the next bytes in the stream as a TAG_List
     * (excluding the tag type).
     * @return a TAG_List from the stream
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TagType#TAG_List
     */
    public TagList readList() throws IOException, NBTTagException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        String[] header = in.readLine().split(" ");
        int length = Integer.valueOf(header[1]);
        List<Tag> list = new ArrayList<Tag>();
        in.readLine();
        for (int i = 0; i < length; i++)
            list.add(readNextTag(0, TagType.valueOf(header[5])));
        in.readLine();
        try {
            return new TagList(name, list);
        } catch (NBTNameException ex) {
            Logger.getLogger(NBTInputStream.class.getName())
                .log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Reads the next bytes in the stream as a TAG_Compound
     * (excluding the tag type).
     * @return a TAG_Compound from the stream
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag byte does not indicate a known tag
     * @see TagType#TAG_Compound
     */
    public TagCompound readCompound()
        throws IOException, NBTTagException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        String[] header = in.readLine().split(" ");
        int length = Integer.valueOf(header[1]);
        Map<String, Tag> compound = new HashMap<String, Tag>();
        in.readLine();
        for (int i = 0; i < length; i++) {
            Tag subTag = readNextTag();
            compound.put(subTag.getName(), subTag);
        }
        in.readLine();
        return new TagCompound(name, compound);
    }

    /**
     * Reads the next bytes in the stream as a TAG_IntArray
     * (excluding the tag type).
     * @return a TAG_IntArray from the stream
     * @throws IOException when there is an IO error
     * @see TagType#TAG_IntArray
     */
    public TagIntArray readIntArray() throws IOException {
        String name = null;
        if ((char)in.read() == '(') {
            in.skip(1);
            name = readUntil('"', maxLimit);
            in.skip(2);
        }
        String entries = in.readLine().split(" ")[1];
        int length = Integer.valueOf(entries.substring(1, entries.length()));
        int lines = (int)Math.ceil(length/16);
        int[] array = new int[length];
        in.readLine();
        for (int i = 0; i < lines + 1; i++) {
            skipWSpc(maxLimit);
            String[] line = in.readLine().split(", ");
            for (int cu = 0; cu < line.length; cu++)
                array[i*16+cu] = Integer.valueOf(line[cu]);
        }
        in.readLine();
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
