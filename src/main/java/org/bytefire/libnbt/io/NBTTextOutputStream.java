/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt.io;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.bytefire.libnbt.NBTTagException;
import org.bytefire.libnbt.*;
import static org.bytefire.libnbt.TagType.*;

/**
 * An OutputStream for writing NBT in text form.
 * @author Timothy Oltjenbruns
 * @version 1.1, 06/03/2013
 * @see NBTOutputStream
 * @see java.io.OutputStream
 * @see java.io.Closable
 */
public class NBTTextOutputStream implements Closeable {

    private OutputStreamWriter out;

    /**
     * Writes NBT data to an OutputStreamWriter.
     * @param out an OutputStream to use
     * @throws IOException when there is an IO error
     * @see java.io.DataOutputStream
     * @see java.io.GZIPOutputStream
     */
    public NBTTextOutputStream(OutputStream out) throws IOException {
        this.out = new OutputStreamWriter(out, TagString.charset);
    }

    /**
     * Writes a tag of an unknown type to the stream by using the
     * ID saved in the tag.
     * @param tag tag to be written
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag ID is not known as a valid tag
     * @see Tag
     */
    public void writeTag(Tag tag) throws IOException, NBTTagException {
        writeTag(tag, true);
    }

    /**
     * Writes a tag of an unknown type to the stream by using the
     * ID saved in the tag.
     * @param tag tag to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @throws NBTTagException when the tag ID is not known as a valid tag
     * @see Tag
     */
    public void writeTag(Tag tag, boolean named)
        throws IOException, NBTTagException {
        switch (tag.getTagType()) {
            case TAG_End:
                writeEnd();
                break;
            case TAG_Byte:
                writeByte((TagByte) tag);
                break;
            case TAG_Short:
                writeShort((TagShort) tag);
                break;
            case TAG_Int:
                writeInt((TagInt) tag);
                break;
            case TAG_Long:
                writeLong((TagLong) tag);
                break;
            case TAG_Float:
                writeFloat((TagFloat) tag);
                break;
            case TAG_Double:
                writeDouble((TagDouble) tag);
                break;
            case TAG_ByteArray:
                writeByteArray((TagByteArray) tag);
                break;
            case TAG_String:
                writeString((TagString) tag);
                break;
            case TAG_List:
                writeList((TagList) tag);
                break;
            case TAG_Compound:
                writeCompound((TagCompound) tag);
                break;
            case TAG_IntArray:
                writeIntArray((TagIntArray) tag);
                break;
        }
    }

    /**
     * Writes a @see TagEnd TAG_End to the stream.
     * @throws IOException when there is an IO error
     * @see TagEnd
     */
    public void writeEnd() throws IOException {
        out.write(new TagEnd().toString());
    }

    /**
     * Writes a @see TagByte TAG_Byte to the stream.
     * @param byteTag TAG_Byte to be written
     * @throws IOException when there is an IO error
     * @see TagByte
     */
    public void writeByte(TagByte byteTag) throws IOException {
        out.write(byteTag.toString());
    }

    /**
     * Writes a @see TagShort TAG_Short to the stream.
     * @param shortTag TAG_Short to be written
     * @throws IOException when there is an IO error
     * @see TagShort
     */
    public void writeShort(TagShort shortTag) throws IOException {
        out.write(shortTag.toString());
    }

    /**
     * Writes a @see TagInt TAG_Int to the stream.
     * @param intTag TAG_Int to be written
     * @throws IOException when there is an IO error
     * @see TagInt
     */
    public void writeInt(TagInt intTag) throws IOException {
        out.write(intTag.toString());
    }

    /**
     * Writes a @see TagLong TAG_Long to the stream.
     * @param longTag TAG_Long to be written
     * @throws IOException when there is an IO error
     * @see TagLong
     */
    public void writeLong(TagLong longTag) throws IOException {
        out.write(longTag.toString());
    }

    /**
     * Writes a @see TagFloat TAG_Float to the stream.
     * @param floatTag TAG_Float to be written
     * @throws IOException when there is an IO error
     * @see TagFloat
     */
    public void writeFloat(TagFloat floatTag) throws IOException {
        out.write(floatTag.toString());
    }

    /**
     * Writes a TAG_Double to the stream.
     * @param doubleTag TAG_Double to be written
     * @throws IOException when there is an IO error
     * @see TagDouble
     */
    public void writeDouble(TagDouble doubleTag) throws IOException {
        out.write(doubleTag.toString());
    }

    /**
     * Writes a TAG_ByteArray to the stream.
     * @param byteArrayTag TAG_ByteArray to be written
     * @throws IOException when there is an IO error
     * @see TagByteArray
     */
    public void writeByteArray(TagByteArray byteArrayTag) throws IOException {
        out.write(byteArrayTag.toString());
    }

    /**
     * Writes a TAG_String to the stream.
     * @param stringTag TAG_String to be written
     * @throws IOException when there is an IO error
     * @see TagString
     */
    public void writeString(TagString stringTag) throws IOException {
        out.write(stringTag.toString());
    }

    /**
     * Writes a TAG_List to the stream.
     * @param listTag TAG_List to be written
     * @throws IOException when there is an IO error
     * @see TagList
     */
    public void writeList(TagList listTag) throws IOException{
        out.write(listTag.toString());
    }

    /**
     * Writes a TAG_Compound to the stream.
     * @param compoundTag TAG_Compound to be written
     * @throws IOException when there is an IO error
     * @throws NBTTagException when a subtag's ID is not known as a valid tag
     * @see TagCompound
     */
    public void writeCompound(TagCompound compoundTag) throws IOException{
        out.write(compoundTag.toString());
    }

    /**
     * Writes a TAG_IntArray to the stream.
     * @param intArrayTag TAG_IntArray to be written
     * @throws IOException when there is an IO error
     * @see TagIntArray
     */
    public void writeIntArray(TagIntArray intArrayTag) throws IOException {
        out.write(intArrayTag.toString());
    }

    /**
     * Flushes this OutputStream and forces any buffered output bytes
     * to be written out to the stream.
     * @throws IOException when an IO error occurs
     * @see java.io.DataOutputStream
     */
    public void flush() throws IOException {
        out.flush();
    }

    /**
     * Closes this OutputStream and releases any system resources
     * associated with the stream.
     * @throws IOException when an IO error occurs
     * @see java.io.DataOutputStream
     */
    @SuppressWarnings("empty-statement")
    public void close() throws IOException {
        try {
            out.flush();
        } catch (IOException ignored) {};
        out.close();
    }

}
