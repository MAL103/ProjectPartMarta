package cs107;

/**
 * "Quite Ok Image" Encoder
 * @apiNote Second task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.3
 * @since 1.0
 */
public final class QOIEncoder {

    /**
     * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
     */
    private QOIEncoder(){}

    // ==================================================================================
    // ============================ QUITE OK IMAGE HEADER ===============================
    // ==================================================================================

    /**
     * Generate a "Quite Ok Image" header using the following parameters
     * @param image (Helper.Image) - Image to use
     * @throws AssertionError if the colorspace or the number of channels is corrupted or if the image is null.
     *  (See the "Quite Ok Image" Specification or the handouts of the project for more information)
     * @return (byte[]) - Corresponding "Quite Ok Image" Header
     */
    public static byte[] qoiHeader(Helper.Image image){
        assert (image!=null);
        assert(image.channels()==QOISpecification.RGB || image.channels()==QOISpecification.RGBA);
        assert(image.color_space()==QOISpecification.sRGB || image.color_space()==QOISpecification.ALL);
        byte[] width = ArrayUtils.concat((byte)((image.data()).length>>24),(byte)((image.data()).length>>16),(byte)((image.data()).length>>8),(byte)((image.data()).length));
        byte[] height = ArrayUtils.concat((byte)((image.data()[0]).length>>24),(byte)((image.data()[0]).length>>16),(byte)((image.data()[0]).length>>8),(byte)((image.data()[0]).length));
        byte channels = image.channels();
        byte colorSpace = image.color_space();
        byte[] header = new byte[]{113, 111, 105, 102,height[0],height[1],height[2],height[3],width[0],width[1],width[2],width[3],channels,colorSpace};
        return header;
    }

    // ==================================================================================
    // ============================ ATOMIC ENCODING METHODS =============================
    // ==================================================================================

    /**
     * Encode the given pixel using the QOI_OP_RGB schema
     * @param pixel (byte[]) - The Pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) - Encoding of the pixel using the QOI_OP_RGB schema
     */
    public static byte[] qoiOpRGB(byte[] pixel){
        return Helper.fail("Not Implemented");
    }

    /**
     * Encode the given pixel using the QOI_OP_RGBA schema
     * @param pixel (byte[]) - The pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) Encoding of the pixel using the QOI_OP_RGBA schema
     */
    public static byte[] qoiOpRGBA(byte[] pixel){
        return Helper.fail("Not Implemented");
    }

    /**
     * Encode the index using the QOI_OP_INDEX schema
     * @param index (byte) - Index of the pixel
     * @throws AssertionError if the index is outside the range of all possible indices
     * @return (byte[]) - Encoding of the index using the QOI_OP_INDEX schema
     */
    public static byte[] qoiOpIndex(byte index){
        assert (index<64 && index>=0);
        return ArrayUtils.wrap(index);
    }

    /**
     * Encode the difference between 2 pixels using the QOI_OP_DIFF schema
     * @param diff (byte[]) - The difference between 2 pixels
     * @throws AssertionError if diff doesn't respect the constraints or diff's length is not 3
     * (See the handout for the constraints)
     * @return (byte[]) - Encoding of the given difference
     */
    public static byte[] qoiOpDiff(byte[] diff){
        assert(diff!=null && diff.length==3);
        for(int i=0;i<diff.length;i++){
            assert(diff[i]>=-2 && diff[i]<2);
        }
        byte tag = 0b01000000;
        byte dr = (byte) ((diff[0]+2)<<4);
        byte dg = (byte) ((diff[1]+2)<<2);
        byte db = (byte) (diff[2]+2);
        byte[] encodePixel = new byte[]{(byte)(tag | dr | dg | db)};
        return encodePixel;
    }

    /**
     * Encode the difference between 2 pixels using the QOI_OP_LUMA schema
     * @param diff (byte[]) - The difference between 2 pixels
     * @throws AssertionError if diff doesn't respect the constraints
     * or diff's length is not 3
     * (See the handout for the constraints)
     * @return (byte[]) - Encoding of the given difference
     */
    public static byte[] qoiOpLuma(byte[] diff){
        return Helper.fail("Not Implemented");
    }

    /**
     * Encode the number of similar pixels using the QOI_OP_RUN schema
     * @param count (byte) - Number of similar pixels
     * @throws AssertionError if count is not between 0 (exclusive) and 63 (exclusive)
     * @return (byte[]) - Encoding of count
     */
    public static byte[] qoiOpRun(byte count){
        assert(count>=1 && count<=62);
        byte[] encodePixel = new byte[]{(byte)(0b11000000 | (count-1))};
        return encodePixel;

    }

    // ==================================================================================
    // ============================== GLOBAL ENCODING METHODS  ==========================
    // ==================================================================================

    /**
     * Encode the given image using the "Quite Ok Image" Protocol
     * (See handout for more information about the "Quite Ok Image" protocol)
     * @param image (byte[][]) - Formatted image to encode
     * @return (byte[]) - "Quite Ok Image" representation of the image
     */
    public static byte[] encodeData(byte[][] image){
        return Helper.fail("Not Implemented");
    }

    /**
     * Creates the representation in memory of the "Quite Ok Image" file.
     * @apiNote THE FILE IS NOT CREATED YET, THIS IS JUST ITS REPRESENTATION.
     * TO CREATE THE FILE, YOU'LL NEED TO CALL Helper::write
     * @param image (Helper.Image) - Image to encode
     * @return (byte[]) - Binary representation of the "Quite Ok File" of the image
     * @throws AssertionError if the image is null
     */
    public static byte[] qoiFile(Helper.Image image){
        return Helper.fail("Not Implemented");
    }

}