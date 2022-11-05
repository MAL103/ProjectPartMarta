package cs107;
import java.util.ArrayList;

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
        byte[] header = new byte[]{(byte)QOISpecification.QOI_MAGIC[0], (byte)QOISpecification.QOI_MAGIC[1], (byte)QOISpecification.QOI_MAGIC[2], (byte)QOISpecification.QOI_MAGIC[3],height[0],height[1],height[2],height[3],width[0],width[1],width[2],width[3],channels,colorSpace};
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
        assert(pixel.length==4);
        byte[] encoding = new byte[4];
        encoding[0]=QOISpecification.QOI_OP_RGB_TAG;
        for(int i=1;i<4;i++){
            encoding[i]=pixel[i-1];
        }
        return encoding;

    }

    /**
     * Encode the given pixel using the QOI_OP_RGBA schema
     * @param pixel (byte[]) - The pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) Encoding of the pixel using the QOI_OP_RGBA schema
     */
    public static byte[] qoiOpRGBA(byte[] pixel){
        assert(pixel.length==4);
        byte[] encoding=new byte[5];
        encoding[0]=QOISpecification.QOI_OP_RGBA_TAG;
        for(int i=1;i<5;i++) {
            encoding[i] = pixel[i - 1];
        }
        return encoding;
    }

    /**
     * Encode the index using the QOI_OP_INDEX schema
     * @param index (byte) - Index of the pixel
     * @throws AssertionError if the index is outside the range of all possible indices
     * @return (byte[]) - Encoding of the index using the QOI_OP_INDEX schema
     */
    public static byte[] qoiOpIndex(byte index){
        assert (index<64 && index>=0);
        return ArrayUtils.wrap((byte)(QOISpecification.QOI_OP_INDEX_TAG|index));
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
        byte tag = QOISpecification.QOI_OP_DIFF_TAG;
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
        assert(diff!=null);
        assert(diff.length==3);
        assert(-33<diff[1]&&diff[1]<32);
        assert(-9<(diff[0]-diff[1])&&(diff[0]-diff[1])<8);
        assert(-9<(diff[2]-diff[1])&&(diff[2]-diff[1])<8);
        byte dr=diff[0];
        byte dg=(byte)(diff[1]+32);
        byte db=diff[2];
        byte drg= (byte)((dr-diff[1])+8);
        byte dbg=(byte)((db-diff[1])+8);
        byte[] encoding =new byte[2];
        encoding[0]= (byte)((QOISpecification.QOI_OP_LUMA_TAG) | dg);
        encoding[1]=(byte)((drg<<4)|(dbg));
        return encoding;

    }

    /**
     * Encode the number of similar pixels using the QOI_OP_RUN schema
     * @param count (byte) - Number of similar pixels
     * @throws AssertionError if count is not between 0 (exclusive) and 63 (exclusive)
     * @return (byte[]) - Encoding of count
     */
    public static byte[] qoiOpRun(byte count){
        assert(count>=1 && count<=62);
        byte[] encodePixel = new byte[]{(byte)(QOISpecification.QOI_OP_RUN_TAG | (count-1))};
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
        byte[] previous_pixel = QOISpecification.START_PIXEL;
        byte[][] hash_table = new byte[64][4];
        byte[] repeat = new byte[1];
        //ArrayList<byte[]> repetitions= new ArrayList<byte[]>();
        ArrayList<byte[]> encoding = new ArrayList<byte[]>();
        int counter = 0;
        byte[] index = new byte[1];
        /**
         * Iterate over pixels
         */
        for (int i = 0; i < image.length; ++i) {
            if (i!=0){
                previous_pixel = image[i-1];
            }
            if (ArrayUtils.equals(image[i],previous_pixel)) {
                counter += 1;
                if ((counter == 62) || (i == image.length - 1)) {
                    encoding.add(qoiOpRun((byte) counter));
                    counter = 0;
                }
                continue;
            }
            else {
                if (i!=0 && counter>0){
                    encoding.add(qoiOpRun((byte) counter));
                    counter = 0;
                }
            }
            if (image[i] == hash_table[QOISpecification.hash(image[i])]) {
                encoding.add(ArrayUtils.wrap((byte) i));
                continue;
            } else {
                hash_table[QOISpecification.hash(image[i])] = image[i];
            }
            if (image[i][3] == previous_pixel[3]) {
                byte dr = (byte) (image[i][0] - previous_pixel[0]);
                byte dg = (byte) (image[i][1] - previous_pixel[1]);
                byte db = (byte) (image[i][2] - previous_pixel[2]);
                boolean small = true;
                boolean similar = true;
                byte[] diff = {dr, dg, db};
                for (int j = 0; j < 3; j++) {
                    if ((-2 > diff[j]) || (diff[j] > 2)) {
                        small = false;
                    }
                    if ((-33 > diff[1]) || (diff[1] > 32) ||
                            (-9 > (diff[0] - diff[1])) || (diff[0] - diff[1] > 8) ||
                            (-9 > (diff[2] - diff[1]) || (diff[2] - diff[1] > 8))) {
                        similar = false;
                    }
                }
                if (small) {
                    encoding.add(qoiOpDiff(diff));
                    continue;
                } else if (similar) {
                    encoding.add(qoiOpLuma(diff));
                    continue;
                } else {
                    encoding.add(qoiOpRGB(image[i]));
                    continue;
                }
            }
            else {
                encoding.add(qoiOpRGBA(image[i]));
            }
        }
        byte[][] encodedPixels2D = encoding.toArray(new byte[0][]);
        byte[] encodedPixels1D = ArrayUtils.concat(encodedPixels2D);
        return encodedPixels1D;
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
        assert(image!=null);
        byte[] header = qoiHeader(image);
        byte[][] content = new byte[image.data().length][];
        /**
         * Convert image.data() from int array to byte array to use in encodeData function
         */
        for (int i=0;i<image.data().length;i++){
            for(int ii=0;ii<image.data()[0].length;ii++){
                content[i][ii] = (byte) image.data()[i][ii];
            }
        }
        byte[] encodeContent = encodeData(content);
        byte[] encodePixels = ArrayUtils.concat(header,encodeContent,QOISpecification.QOI_EOF);
        return encodePixels;
    }

}