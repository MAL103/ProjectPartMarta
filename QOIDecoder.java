package cs107;

import static cs107.Helper.Image;

/**
 * "Quite Ok Image" Decoder
 * @apiNote Third task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.3
 * @since 1.0
 */
public final class QOIDecoder {

    /**
     * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
     */
    private QOIDecoder(){}

    // ==================================================================================
    // =========================== QUITE OK IMAGE HEADER ================================
    // ==================================================================================

    /**
     * Extract useful information from the "Quite Ok Image" header
     * @param header (byte[]) - A "Quite Ok Image" header
     * @return (int[]) - Array such as its content is {width, height, channels, color space}
     * @throws AssertionError See handouts section 6.1
     */
    public static int[] decodeHeader(byte[] header){
        assert(header!=null && header.length==QOISpecification.HEADER_SIZE);
        assert(ArrayUtils.extract(header,0,4)==QOISpecification.QOI_MAGIC);
        assert(header[12]==QOISpecification.RGB || header[12]==QOISpecification.RGBA);
        assert(header[13]==QOISpecification.sRGB || header[13]==QOISpecification.ALL);
        int height = (int)(header[4]<<24 | header[5]<<16 | header[6]<<8 | header[7]);
        int width = (int)(header[8]<<24 | header[9]<<16 | header[10]<<8 | header[11]);
        int[] decodedHeader = new int[]{width,height,(int)header[12],(int)header[13]};
        return  decodedHeader;
    }

    // ==================================================================================
    // =========================== ATOMIC DECODING METHODS ==============================
    // ==================================================================================

    /**
     * Store the pixel in the buffer and return the number of consumed bytes
     * @param buffer (byte[][]) - Buffer where to store the pixel
     * @param input (byte[]) - Stream of bytes to read from
     * @param alpha (byte) - Alpha component of the pixel
     * @param position (int) - Index in the buffer
     * @param idx (int) - Index in the input
     * @return (int) - The number of consumed bytes
     * @throws AssertionError See handouts section 6.2.1
     */
    public static int decodeQoiOpRGB(byte[][] buffer, byte[] input, byte alpha, int position, int idx){
        return Helper.fail("Not Implemented");
    }

    /**
     * Store the pixel in the buffer and return the number of consumed bytes
     * @param buffer (byte[][]) - Buffer where to store the pixel
     * @param input (byte[]) - Stream of bytes to read from
     * @param position (int) - Index in the buffer
     * @param idx (int) - Index in the input
     * @return (int) - The number of consumed bytes
     * @throws AssertionError See handouts section 6.2.2
     */
    public static int decodeQoiOpRGBA(byte[][] buffer, byte[] input, int position, int idx){
        return Helper.fail("Not Implemented");
    }

    /**
     * Create a new pixel following the "QOI_OP_DIFF" schema.
     * @param previousPixel (byte[]) - The previous pixel
     * @param chunk (byte) - A "QOI_OP_DIFF" data chunk
     * @return (byte[]) - The newly created pixel
     * @throws AssertionError See handouts section 6.2.4
     */
    public static byte[] decodeQoiOpDiff(byte[] previousPixel, byte chunk){
        assert(previousPixel!=null && previousPixel.length==4);
        byte tag = QOISpecification.QOI_OP_DIFF_TAG;
        assert(((chunk>>6)<<6)==tag);
        byte dr = (byte)(((chunk-tag)>>4)-2);
        byte dg = (byte)(((chunk-tag-((dr+2)<<4))>>2)-2);
        byte db = (byte)((chunk-tag-((dr+2)<<4)-((dg+2)<<2))-2);
        byte[] thisPixel = new byte[]{(byte)(previousPixel[0]+dr),(byte)(previousPixel[1]+dg),(byte)(previousPixel[2]+db),(byte)(previousPixel[3])};
        return thisPixel;
    }
//
    /**
     * Create a new pixel following the "QOI_OP_LUMA" schema
     * @param previousPixel (byte[]) - The previous pixel
     * @param data (byte[]) - A "QOI_OP_LUMA" data chunk
     * @return (byte[]) - The newly created pixel
     * @throws AssertionError See handouts section 6.2.5
     */
    public static byte[] decodeQoiOpLuma(byte[] previousPixel, byte[] data){
        return Helper.fail("Not Implemented");
    }

    /**
     * Store the given pixel in the buffer multiple times
     * @param buffer (byte[][]) - Buffer where to store the pixel
     * @param pixel (byte[]) - The pixel to store
     * @param chunk (byte) - a QOI_OP_RUN data chunk
     * @param position (int) - Index in buffer to start writing from
     * @return (int) - number of written pixels in buffer
     * @throws AssertionError See handouts section 6.2.6
     */
    public static int decodeQoiOpRun(byte[][] buffer, byte[] pixel, byte chunk, int position){
        assert(buffer!=null && pixel!=null && position>0 && position<buffer.length);
        assert(pixel.length==4 && buffer[0].length==4);
        int count =-1;
        int repetitions = chunk - QOISpecification.QOI_OP_RUN_TAG +1;
        assert(buffer.length>=position+repetitions);
        for (int i=position;i<=repetitions;i++){
            buffer[i] = pixel;
        }
        for (byte[] pix:buffer){
            if (pix==pixel){
                count+=1;
            }
        }
        return count;
    }

    // ==================================================================================
    // ========================= GLOBAL DECODING METHODS ================================
    // ==================================================================================

    /**
     * Decode the given data using the "Quite Ok Image" Protocol
     * @param data (byte[]) - Data to decode
     * @param width (int) - The width of the expected output
     * @param height (int) - The height of the expected output
     * @return (byte[][]) - Decoded "Quite Ok Image"
     * @throws AssertionError See handouts section 6.3
     */
    public static byte[][] decodeData(byte[] data, int width, int height){
        return Helper.fail("Not Implemented");
    }

    /**
     * Decode a file using the "Quite Ok Image" Protocol
     * @param content (byte[]) - Content of the file to decode
     * @return (Image) - Decoded image
     * @throws AssertionError if content is null
     */
    public static Image decodeQoiFile(byte[] content){
        return Helper.fail("Not Implemented");
    }

}