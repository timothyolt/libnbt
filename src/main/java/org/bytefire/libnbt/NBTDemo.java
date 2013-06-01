/*
 * This work is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/.
 */

package org.bytefire.libnbt;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A demo java file showing how to use libnbt using the original examples
 * included in Markus Persson's NBT specification, which is also supplied
 * in updated form with this library.
 * @author Timothy Oltjenbruns
 * @version 1.0, 05/31/2013
 * @see NBTOutputStream
 * @see NBTInputStream
 * @see Tag
 */
public class NBTDemo {
    /**
     * Writes out the two original test files provided with the original
     * NBT specification then reads it back to the console in text mode.
     * @param args nothing currently.
     */
    public static void main( String[] args ) {

        //Declares the four NBT IO streams used in this demo.
        NBTOutputStream testNBT;
        NBTOutputStream bigtestNBT;

        //Declares the two root compound tags for writing to the NBT files
        Map<String, Tag> testRoot = new HashMap<String, Tag>();
        Map<String, Tag> bigtestRoot = new HashMap<String, Tag>();

        //Adds a single new TAG_String to the first root tag
        testRoot.put("name", new TagString("name", "Bananrama"));

        /*
         * Adds all the tags to the second root tag, catching exceptions that
         * might be thrown by defining the TAG_List's in an incorrect format
         */
        try{
            bigtestRoot.put("shortTest",
                new TagShort("shortTest", (short) 32767));
            bigtestRoot.put("longTest",
                new TagLong("longTest", 9223372036854775807L));
            bigtestRoot.put("floatTest",
                new TagFloat("floatTest", 0.49823147F));
            bigtestRoot.put("stringTest", new TagString("stringTest",
                "HELLO WORLD THIS IS A TEST STRING ÅÄÖ!"));
            bigtestRoot.put("intTest", new TagInt("intTest", 2147483647));

            /*
             * To add a compound tag nested in the root tag, or into another
             * compound tag more maps must be declared for those compound tags.
             */
            Map<String, Tag> nestCompound = new HashMap<String, Tag>();
                Map<String, Tag> ham = new HashMap<String, Tag>();

                    /*
                     * Then, the nested compound's subtags are added to their
                     * respective maps.
                     */
                    ham.put("name", new TagString("name", "Hampus"));
                    ham.put("value", new TagFloat("value", 0.75F));
                Map<String, Tag> egg = new HashMap<String, Tag>();
                    egg.put("name", new TagString("name", "Eggbert"));
                    egg.put("value", new TagFloat("value", 0.5F));

                /*
                 * After all the subtags of a nested compound are added to the
                 * map, then the compound tag can be added to its parent tag
                 */
                nestCompound.put("ham", new TagCompound("ham", ham));
                nestCompound.put("egg", new TagCompound("egg", egg));
            bigtestRoot.put("nested compound test",
                new TagCompound("nested compound test", nestCompound));

            /*
             * Lists are defined in a similar manner to a compound tag.
             * First, an list is declared.
             */
            List<Tag> listLong = new ArrayList<Tag>();

                /*
                 * Then, all of it's members are defined and added to
                 * the list. Remember, lists can only contain tags of the
                 * same type, and instead of being identified by name,
                 * they can be identified by number.
                 */
                listLong.add(new TagLong(null, 11));
                listLong.add(new TagLong(null, 12));
                listLong.add(new TagLong(null, 13));
                listLong.add(new TagLong(null, 14));
                listLong.add(new TagLong(null, 15));

            /*
             * Once all the subtags of the list are defined, the list tag itself
             * can be defined and added to the parent tag.
             */
            bigtestRoot.put("listTest (long)",
                new TagList("listTest (long)", listLong));

            bigtestRoot.put("byteTest", new TagByte("byteTest", (byte) 127));

            /*
             * Take note that lists can contain compound tags. This is useful
             * for grouping data pertaining to similar objects.
             * For example a list of names and dates representing a file.
             */
            List<Tag> listCompound = new ArrayList<Tag>();
                Map<String, Tag> compound0 = new HashMap<String, Tag>();
                    compound0.put("name",
                        new TagString("name", "Compound tag #0"));
                    compound0.put("created-on",
                        new TagLong("created-on", 1264099775885L));
                listCompound.add(new TagCompound(null, compound0));
                Map<String, Tag> compound1 = new HashMap<String, Tag>();
                    compound1.put("name",
                        new TagString("name", "Compound tag #1"));
                    compound1.put("created-on",
                        new TagLong("created-on", 1264099775885L));
                listCompound.add(new TagCompound(null, compound1));
            bigtestRoot.put("listTest (compound)",
                new TagList("listTest (compound)", listCompound));

            /*
             * To define an array, either byte or integer, you need to make a
             * single dimensional array of that data type, then fill it with the
             * desired values.
             */
            byte[] byteArray = new byte[1000];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = (byte)((i*i*255+i*7)%100);
            }

            /*
             * Once the array is filled with values, simply use it when you add
             * the array to the parent tag.
             */
            bigtestRoot.put("byteArrayTest", new TagByteArray("byteArrayTest "
                + "(the first 1000 values of (n*n*255+n*7)%100, "
                + "starting with n=0 (0, 62, 34, 16, 8, ...))", byteArray));

            bigtestRoot.put("doubleTest",
                new TagDouble("doubleTest", 0.4931287132182315D));

        } catch (NBTNameException ex) {
            Logger.getLogger(NBTDemo.class.getName())
                .log(Level.SEVERE, null, ex);
        } catch (NBTTagException ex) {
            Logger.getLogger(NBTDemo.class.getName())
                .log(Level.SEVERE, null, ex);
        }

        /*
         * Once all the content of the root tags are defined, just use the map
         * they were added to to create the root compound tag.
         */
        Tag testOut = new TagCompound("hello world", testRoot);
        Tag bigtestOut = new TagCompound("hello world", bigtestRoot);

        /*
         * To write these maps to a file, just create an NBTOutputStream
         * pointing where you want the file created or updated, and call the
         * writeTag method. This takes care of writing the entire root tag and
         * all of its subtags for you. Remember to close the stream (which
         * attempts to flush the stream as well) to make sure nothing in the
         * buffer remains unwritten to the file.
         */
        try {
            testNBT = new NBTOutputStream(
                new FileOutputStream("test.nbt"), true);
            testNBT.writeTag(testOut);
            testNBT.close();

            bigtestNBT = new NBTOutputStream(
                new FileOutputStream("bigtest.nbt"), true);
            bigtestNBT.writeTag(bigtestOut);
            bigtestNBT.close();
        } catch (IOException ex) {
            Logger.getLogger(NBTDemo.class.getName())
                .log(Level.SEVERE, null, ex);
        } catch (NBTTagException ex) {
            Logger.getLogger(NBTDemo.class.getName())
                .log(Level.SEVERE, null, ex);
        }

        try {
            /*
             * To read NBT files, simply create an NBTInputStream with the file
             * you want to use.
             */
            NBTInputStream testIn = new NBTInputStream(
                new FileInputStream("test.nbt"));
            NBTInputStream bigtestIn = new NBTInputStream(
                new FileInputStream("bigtest.nbt"));

            /*
             * If you want the whole file read (and the contents are wrapped by
             * a root compound tag) just have it read the next tag. For
             * debugging, you can also covnert any tag to a string which will
             * follow the format for text NBT used in specification 19133
             */
            Logger.getLogger(NBTDemo.class.getName())
                .info(testIn.readNextTag().toString());

            Logger.getLogger(NBTDemo.class.getName())
                .info(bigtestIn.readNextTag().toString());

        } catch (IOException ex) {
            Logger.getLogger(NBTDemo.class.getName())
                .log(Level.SEVERE, null, ex);
        } catch (NBTTagException ex) {
            Logger.getLogger(NBTDemo.class.getName())
                .log(Level.SEVERE, null, ex);
        }

    }
}
