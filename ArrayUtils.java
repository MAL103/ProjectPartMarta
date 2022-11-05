package cs107;

/**
 * Utility class to manipulate arrays.
 * @apiNote First Task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.3
 * @since 1.0
 */
public final class ArrayUtils {

    /**
     * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
     */
    private ArrayUtils(){}

    // ==================================================================================
    // =========================== ARRAY EQUALITY METHODS ===============================
    // ==================================================================================

    /**
     * Check if the content of both arrays is the same
     * @param a1 (byte[]) - First array
     * @param a2 (byte[]) - Second array
     * @return (boolean) - true if both arrays have the same content (or both null), false otherwise
     * @throws AssertionError if one of the parameters is null
     */
    public static boolean equals(byte[] a1, byte[] a2){
        assert ((a1 != null && a2!=null) || (a1==null && a2==null));

        for (int i=0;(i< a1.length) && (i<a2.length);i++){
            if (a1[i]!=a2[i]){
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the content of both arrays is the same
     * @param a1 (byte[][]) - First array
     * @param a2 (byte[][]) - Second array
     * @return (boolean) - true if both arrays have the same content (or both null), false otherwise
     * @throws AssertionError if one of the parameters is null
     */
    public static boolean equals(byte[][] a1, byte[][] a2){
        assert ((a1==null && a2==null) || (a1 !=null && a2 !=null));
        if (a1.length != a2.length) {
            return false;
        }
        else{
            for(int i=0;i< a1.length; ++i) {
                for (int j = 0; j < a1[i].length; ++j) {
                    if (a1[i][j] != a2[i][j]){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // ==================================================================================
    // ============================ ARRAY WRAPPING METHODS ==============================
    // ==================================================================================

    /**
     * Wrap the given value in an array
     * @param value (byte) - value to wrap
     * @return (byte[]) - array with one element (value)
     */
    public static byte[] wrap(byte value){
        byte[] array = {value};
        return array;
    }

    // ==================================================================================
    // ========================== INTEGER MANIPULATION METHODS ==========================
    // ==================================================================================

    /**
     * Create an Integer using the given array. The input needs to be considered
     * as "Big Endian"
     * (See handout for the definition of "Big Endian")
     * @param bytes (byte[]) - Array of 4 bytes
     * @return (int) - Integer representation of the array
     * @throws AssertionError if the input is null or the input's length is different from 4
     */
    public static int toInt(byte[] bytes){
        assert(bytes!=null && bytes.length==4);
        int shift = 24;
        int integer = 0;
        for (int i = 0;i<bytes.length;i++){
            integer += (int) (bytes[i]<<shift);
            shift-=8;
        }
        return integer;
    }

    /**
     * Separate the Integer (word) to 4 bytes. The Memory layout of this integer is "Big Endian"
     * (See handout for the definition of "Big Endian")
     * @param value (int) - The integer
     * @return (byte[]) - Big Endian representation of the integer
     */
    public static byte[] fromInt(int value){
        byte[] bytes = new byte[4];
        int shift = 24;
        for (int i=0;i<4;i++){
            bytes[i] = (byte) (value>>shift);
            value-=(byte)(value>>shift)<<shift;
            shift-=8;
        }
        return bytes;
    }

    // ==================================================================================
    // ========================== ARRAY CONCATENATION METHODS ===========================
    // ==================================================================================

    /**
     * Concatenate a given sequence of bytes and stores them in an array
     * @param bytes (byte ...) - Sequence of bytes to store in the array
     * @return (byte[]) - Array representation of the sequence
     * @throws AssertionError if the input is null
     */
    public static byte[] concat(byte ... bytes){
        assert(bytes!=null);
        byte[] tab= new byte[bytes.length];
        for(int i=0;i<bytes.length;i++){
            tab[i]=bytes[i];
        }
        return tab;
    }

    /**
     * Concatenate a given sequence of arrays into one array
     * @param tabs (byte[] ...) - Sequence of arrays
     * @return (byte[]) - Array representation of the sequence
     * @throws AssertionError if the input is null
     * or one of the inner arrays of input is null.
     */
    public static byte[] concat(byte[] ... tabs){
        assert (tabs!=null);
        for(int i=0;i<tabs.length;i++){
            assert(tabs[i]!=null);
        }
        int totalLength = 0;
        //build length
        for(int i=0;i<tabs.length;i++){
            totalLength += tabs[i].length;
        }
        //build the array
        byte[] tab = new byte[totalLength];
        int index=0;
        for(int i=0;i< tabs.length;++i){
            System.arraycopy(tabs[i],0,tab,index,tabs[i].length);
            index+=tabs[i].length;
        }
        return tab;
    }

    // ==================================================================================
    // =========================== ARRAY EXTRACTION METHODS =============================
    // ==================================================================================

    /**
     * Extract an array from another array
     * @param input (byte[]) - Array to extract from
     * @param start (int) - Index in the input array to start the extract from
     * @param length (int) - The number of bytes to extract
     * @return (byte[]) - The extracted array
     * @throws AssertionError if the input is null or start and length are invalid.
     * start + length should also be smaller than the input's length
     */
    public static byte[] extract(byte[] input, int start, int length){
        assert(input!=null);
        assert(start>=0 && start<input.length);
        assert(length>=0 && length<input.length);
        assert((start+length) <=input.length);
        byte[] extract= new byte[length];
        int i=start;
        for(int j=0;j<length;j++){
            extract[j] = input[j+start];
        }
        return extract;
    }

    /**
     * Create a partition of the input array.
     * (See handout for more information on how this method works)
     * @param input (byte[]) - The original array
     * @param sizes (int ...) - Sizes of the partitions
     * @return (byte[][]) - Array of input's partitions.
     * The order of the partition is the same as the order in sizes
     * @throws AssertionError if one of the parameters is null
     * or the sum of the elements in sizes is different from the input's length
     */
    public static byte[][] partition(byte[] input, int ... sizes) {
        assert (input!=null && sizes!=null);
        int total = 0;
        for (int i= 0; i<sizes.length;i++){
            total+=sizes[i];
        }
        assert(total==input.length);
        int j = 0;
        byte[][] output = new byte[sizes.length][];
        for (int i=0;i< sizes.length;i++){
            byte[] subList = new byte[sizes[i]];
            for (int ii=0;ii<sizes[i];ii++){
                subList[ii] = input[j];
                j += 1;
            }
            output[i] = subList;
        }
        System.out.println(output);
        return output;
    }

    // ==================================================================================
    // ============================== ARRAY FORMATTING METHODS ==========================
    // ==================================================================================

    /**
     * Format a 2-dim integer array
     * where each dimension is a direction in the image to
     * a 2-dim byte array where the first dimension is the pixel
     * and the second dimension is the channel.
     * See handouts for more information on the format.
     * @param input (int[][]) - image data
     * @return (byte [][]) - formatted image data
     * @throws AssertionError if the input is null
     * or one of the inner arrays of input is null
     */
    public static byte[][] imageToChannels(int[][] input){
        assert(input!=null);
        int line_length=input[0].length;
        for(int i=1;i<input.length;++i){
            assert (input[i] != null);
            assert (input[i].length == line_length);
        }

        byte[] r= new byte[input.length*input[0].length];
        byte[] g=new byte[input.length*input[0].length];
        byte[] b=new byte[input.length*input[0].length];
        byte[] a=new byte[input.length*input[0].length];
        int k=0;
        for (int i=0;i<input.length;++i) {
            for (int j = 0; j < input[i].length; ++j) {
                a[k] =(byte)((input[i][j]>>24) & 0xFF);
                r[k] = (byte)((input[i][j] >> 16) & 0xFF);
                g[k] = (byte)((input[i][j] >> 8) & 0xFF);
                b[k] = (byte)(input[i][j] & 0xFF);
                k++;
            }
        }
        byte[][] output= new byte[input.length*input[0].length][4];
        for(int i=0;i<input.length*input[0].length;++i){

            output[i][0] = r[i];
            output[i][1] = g[i];
            output[i][2] = b[i];
            output[i][3] = a[i];

        }
        return output;
    }

    /**
     * Format a 2-dim byte array where the first dimension is the pixel
     * and the second is the channel to a 2-dim int array where the first
     * dimension is the height and the second is the width
     * @param input (byte[][]) : linear representation of the image
     * @param height (int) - Height of the resulting image
     * @param width (int) - Width of the resulting image
     * @return (int[][]) - the image data
     * @throws AssertionError if the input is null
     * or one of the inner arrays of input is null
     * or input's length differs from width * height
     * or height is invalid
     * or width is invalid
     */
    public static int[][] channelsToImage(byte[][] input, int height, int width){
        assert (input[0].length==4);
        assert(input !=null);
        assert(input.length == height*width);
        int z=0;
        for(int i=0;i<input.length;i++) {
            assert (input[i] != null);
        }
        int[] a= new int[input.length];
        int[] r= new int[input.length];
        int[] g= new int[input.length];
        int[] b= new int[input.length];
        int [][]output = new int[height][width];
        int k=0;
        for(int i=0;i<input.length;i++){
            r[k]=input[i][0];
            g[k]=input[i][1];
            b[k]=input[i][2];
            //System.out.print(b[k]);
            a[k]=input[i][3];
            k++;
        }
        //for( int element:b){ System.out.print(element);}
        for(int i=0;i<height;i++)
        {
            for (int j = 0; j < width; j++) {
                if (z != k) {
                    output[i][j] = ((a[z] << 24) + (r[z] << 16) + (g[z] << 8) + b[z]);
                    z++;
                }
            }
        }

        return output;
    }

}