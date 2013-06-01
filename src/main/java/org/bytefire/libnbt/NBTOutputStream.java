/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import static org.bytefire.libnbt.TagType.*;

/**
 * An OutputStream for writing NBT files.
 * @author Timothy Oltjenbruns
 * @version 1.0, 05/31/2013
 * @see java.io.OutputStream
 * @see java.io.Closable
 */
public class NBTOutputStream implements Closeable {

    private DataOutputStream out;

    private final boolean text;

    /**
     * Writes NBT data to a DataOutputStream.
     * @param out an OutputStream to use
     * @param gzip whether to gzip the file
     * @param text whether to output text instead of NBT
     * @throws IOException when there is an IO error
     * @see java.io.DataOutputStream
     * @see java.io.GZIPOutputStream
     */
    public NBTOutputStream(OutputStream out, boolean gzip, boolean text)
        throws IOException {
        if (gzip) this.out = new DataOutputStream(new GZIPOutputStream(out));
        else this.out = new DataOutputStream(out);
        this.text = text;
    }

    /**
     * Writes NBT data to a DataOutputStream.
     * @param out an OutputStream to use
     * @param gzip whether to gzip the file
     * @throws IOException when there is an IO error
     * @see java.io.DataOutputStream
     * @see java.io.GZIPOutputStream
     */
    public NBTOutputStream(OutputStream out, boolean gzip) throws IOException {
        if (gzip) this.out = new DataOutputStream(new GZIPOutputStream(out));
        else this.out = new DataOutputStream(out);
        this.text = false;
    }

    /**
     * Writes NBT data to a gzipped DataOutputStream.
     * @param out an OutputStream to use
     * @throws IOException when there is an IO error
     * @see java.io.DataOutputStream
     * @see java.io.GZIPOutputStream
     */
    public NBTOutputStream(OutputStream out) throws IOException {
        this.out = new DataOutputStream(new GZIPOutputStream(out));
        this.text = false;
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
                writeByte((TagByte) tag, named);
                break;
            case TAG_Short:
                writeShort((TagShort) tag, named);
                break;
            case TAG_Int:
                writeInt((TagInt) tag, named);
                break;
            case TAG_Long:
                writeLong((TagLong) tag, named);
                break;
            case TAG_Float:
                writeFloat((TagFloat) tag, named);
                break;
            case TAG_Double:
                writeDouble((TagDouble) tag, named);
                break;
            case TAG_ByteArray:
                writeByteArray((TagByteArray) tag, named);
                break;
            case TAG_String:
                writeString((TagString) tag, named);
                break;
            case TAG_List:
                writeList((TagList) tag, named);
                break;
            case TAG_Compound:
                writeCompound((TagCompound) tag, named);
                break;
            case TAG_IntArray:
                writeIntArray((TagIntArray) tag, named);
                break;
        }
    }

    /**
     * Writes a @see TagEnd TAG_End to the stream.
     * @throws IOException when there is an IO error
     * @see TagEnd
     */
    public void writeEnd() throws IOException {
        out.write(TAG_End.getID());
    }

    /**
     * Writes a @see TagByte TAG_Byte to the stream.
     * @param byteTag TAG_Byte to be written
     * @throws IOException when there is an IO error
     * @see TagByte
     */
    public void writeByte(TagByte byteTag) throws IOException {
        writeByte(byteTag, true);
    }

    /**
     * Writes a @see TagByte TAG_Byte to the stream.
     * @param byteTag TAG_Byte to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagByte
     */
    public void writeByte(TagByte byteTag, boolean named)
        throws IOException {
        if (named) {
            out.writeByte(TAG_Byte.getID());
            out.writeUTF(byteTag.getName());
        }
        out.writeByte(byteTag.getPayload());
    }

    /**
     * Writes a @see TagShort TAG_Short to the stream.
     * @param shortTag TAG_Short to be written
     * @throws IOException when there is an IO error
     * @see TagShort
     */
    public void writeShort(TagShort shortTag) throws IOException {
        writeShort(shortTag, true);
    }

    /**
     * Writes a @see TagShort TAG_Short to the stream.
     * @param shortTag TAG_Short to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagShort
     */
    public void writeShort(TagShort shortTag, boolean named)
        throws IOException {
        if (named) {
            out.writeByte(TAG_Short.getID());
            out.writeUTF(shortTag.getName());
        }
        out.writeShort(shortTag.getPayload());
    }

    /**
     * Writes a @see TagInt TAG_Int to the stream.
     * @param intTag TAG_Int to be written
     * @throws IOException when there is an IO error
     * @see TagInt
     */
    public void writeInt(TagInt intTag) throws IOException {
        writeInt(intTag, true);
    }

    /**
     * Writes a @see TagInt TAG_Int to the stream.
     * @param intTag TAG_Int to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagInt
     */
    public void writeInt(TagInt intTag, boolean named) throws IOException {
        if (named) {
            out.writeByte(TAG_Int.getID());
            out.writeUTF(intTag.getName());
        }
        out.writeInt(intTag.getPayload());
    }

    /**
     * Writes a @see TagLong TAG_Long to the stream.
     * @param longTag TAG_Long to be written
     * @throws IOException when there is an IO error
     * @see TagLong
     */
    public void writeLong(TagLong longTag) throws IOException {
        writeLong(longTag, true);
    }

    /**
     * Writes a @see TagLong TAG_Long to the stream.
     * @param longTag TAG_Long to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagLong
     */
    public void writeLong(TagLong longTag, boolean named) throws IOException {
        if (named) {
            out.writeByte(TAG_Long.getID());
            out.writeUTF(longTag.getName());
        }
        out.writeLong(longTag.getPayload());
    }

    /**
     * Writes a @see TagFloat TAG_Float to the stream.
     * @param floatTag TAG_Float to be written
     * @throws IOException when there is an IO error
     * @see TagFloat
     */
    public void writeFloat(TagFloat floatTag) throws IOException {
        writeFloat(floatTag, true);
    }

    /**
     * Writes a @see TagFloat TAG_Float to the stream.
     * @param floatTag TAG_Float to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagFloat
     */
    public void writeFloat(TagFloat floatTag, boolean named)
        throws IOException {
        if (named) {
            out.writeByte(TAG_Float.getID());
            out.writeUTF(floatTag.getName());
        }
        out.writeFloat(floatTag.getPayload());
    }

    /**
     * Writes a TAG_Double to the stream.
     * @param doubleTag TAG_Double to be written
     * @throws IOException when there is an IO error
     * @see TagDouble
     */
    public void writeDouble(TagDouble doubleTag) throws IOException {
        writeDouble(doubleTag, true);
    }

    /**
     * Writes a TAG_Double to the stream.
     * @param doubleTag TAG_Double to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagDouble
     */
    public void writeDouble(TagDouble doubleTag, boolean named)
        throws IOException {
        if (named) {
            out.writeByte(TAG_Double.getID());
            out.writeUTF(doubleTag.getName());
        }
        out.writeDouble(doubleTag.getPayload());
    }

    /**
     * Writes a TAG_ByteArray to the stream.
     * @param byteArrayTag TAG_ByteArray to be written
     * @throws IOException when there is an IO error
     * @see TagByteArray
     */
    public void writeByteArray(TagByteArray byteArrayTag) throws IOException {
        writeByteArray(byteArrayTag, true);
    }

    /**
     * Writes a TAG_ByteArray to the stream.
     * @param byteArrayTag TAG_ByteArray to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagByteArray
     */
    public void writeByteArray(TagByteArray byteArrayTag, boolean named)
        throws IOException {
        if (named) {
            out.writeByte(TAG_ByteArray.getID());
            out.writeUTF(byteArrayTag.getName());
        }
        out.writeInt(byteArrayTag.getPayload().length);
        out.write(byteArrayTag.getPayload());
    }

    /**
     * Writes a TAG_String to the stream.
     * @param stringTag TAG_String to be written
     * @throws IOException when there is an IO error
     * @see TagString
     */
    public void writeString(TagString stringTag) throws IOException {
        writeString(stringTag, true);
    }

    /**
     * Writes a TAG_String to the stream.
     * @param stringTag TAG_String to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagString
     */
    public void writeString(TagString stringTag, boolean named)
        throws IOException {
        if (named) {
            out.writeByte(TAG_String.getID());
            out.writeUTF(stringTag.getName());
        }
        out.writeUTF(stringTag.getPayload());
    }

    /**
     * Writes a TAG_List to the stream.
     * @param listTag TAG_List to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagList
     */
    public void writeList(TagList listTag) throws IOException, NBTTagException {
        writeList(listTag, true);
    }

    /**
     * Writes a TAG_List to the stream.
     * @param listTag TAG_List to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagList
     */
    public void writeList(TagList listTag, boolean named) throws IOException {
        if (named) {
            out.writeByte(TAG_List.getID());
            out.writeUTF(listTag.getName());
        }
        out.writeByte(listTag.getType().getID());
        out.writeInt(listTag.getPayload().size());
        for (Tag tag : listTag.getPayload()) {
            try {
                writeTag(tag, false);
            } catch (NBTTagException ex) {
                Logger.getLogger(NBTOutputStream.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Writes a TAG_Compound to the stream.
     * @param compoundTag TAG_Compound to be written
     * @throws IOException when there is an IO error
     * @throws NBTTagException when a subtag's ID is not known as a valid tag
     * @see TagCompound
     */
    public void writeCompound(TagCompound compoundTag)
        throws IOException, NBTTagException {
        writeCompound(compoundTag, true);
    }

    /**
     * Writes a TAG_Compound to the stream.
     * @param compoundTag TAG_Compound to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @throws NBTTagException when a subtag's ID is not known as a valid tag
     * @see TagCompound
     */
    public void writeCompound(TagCompound compoundTag, boolean named)
        throws IOException, NBTTagException {
        if (named) {
            out.writeByte(TAG_Compound.getID());
            out.writeUTF(compoundTag.getName());
        }
        boolean terminated = false;
        for (Map.Entry<String, Tag> tag : compoundTag.getPayload().entrySet()) {
            if (tag.getValue().getTagType().equals(TAG_End) && (!terminated))
                writeEnd();
            else writeTag(tag.getValue(), true);
        }
        if (!terminated) writeEnd();
    }

    /**
     * Writes a TAG_IntArray to the stream.
     * @param intArrayTag TAG_IntArray to be written
     * @throws IOException when there is an IO error
     * @see TagIntArray
     */
    public void writeByteArray(TagIntArray intArrayTag) throws IOException {
        writeIntArray(intArrayTag, true);
    }

    /**
     * Writes a TAG_IntArray to the stream.
     * @param intArraytag TAG_IntArray to be written
     * @param named whether the tag's name and ID should be written
     * @throws IOException when there is an IO error
     * @see TagIntArray
     */
    public void writeIntArray(TagIntArray intArraytag, boolean named)
        throws IOException {
        if (named) {
            out.writeByte(TAG_ByteArray.getID());
            out.writeUTF(intArraytag.getName());
        }
        out.writeInt(intArraytag.getPayload().length);
        for (int integer : intArraytag.getPayload()) {
            out.writeInt(integer);
        }
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
        try{
            out.flush();
        } catch (IOException ignored) {};
        out.close();
    }
}
