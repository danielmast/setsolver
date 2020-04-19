package nl.danielmast.setsolver;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.awt.image.BufferedImage;

import static org.opencv.imgproc.Imgproc.cvtColor;

public class VideoCap {

    static final int RESIZE_WIDTH = 800;

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    VideoCapture cap;
    Mat2Image mat2Img = new Mat2Image();

    VideoCap(){
        cap = new VideoCapture();
        cap.open(0);
        boolean wset = cap.set(Videoio.CAP_PROP_FRAME_WIDTH, 1280);
        boolean hset = cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, 960);
    }

    BufferedImage getOneFrame() {
        cap.read(mat2Img.mat);

        Mat original = mat2Img.mat;

//        Mat resized = new Mat();
//        if (original.width() > RESIZE_WIDTH) {
//            long startResize = System.currentTimeMillis();
//            resized = new Mat();
//            Imgproc.resize(original, resized, new Size(
//                    RESIZE_WIDTH,
//                    original.rows() * RESIZE_WIDTH / original.cols()
//            ));
////            System.out.println("Time resize: " + (System.currentTimeMillis() - startResize) + " ms");
//        } else {
//            original.copyTo(resized);
//        }
//
//        long startConvertToRGB = System.currentTimeMillis();
//        Mat rgb = new Mat();
//        cvtColor(resized, rgb, COLOR_BGR2RGB);
////        System.out.println("Time convertToRGB: " + (System.currentTimeMillis() - startConvertToRGB) + " ms");
//
//        Mat result = resized;
//        try {
//            long start = System.currentTimeMillis();
//
//            Map<Card, Point> cards = ImageProcessor.getCards(rgb);
//            System.out.println(String.format("%d Cards: %s", cards.size(), cards));
//
////            System.out.println("Time getCards: " + (System.currentTimeMillis() - startGetCards) + " ms");
//
////            System.out.println(String.format("%d Cards: %s", cards.size(), cards));
//
//            long startFindSets = System.currentTimeMillis();
//            Set<CardSet> sets = SetFinder.findSets(cards.keySet());
//            System.out.println(String.format("%d Sets:", sets.size()));
////            System.out.println("Time findSets: " + (System.currentTimeMillis() - startFindSets) + " ms");
//
//            System.out.println(sets);
//
//            long startDrawSets = System.currentTimeMillis();
//            result = ImageProcessor.drawSets(resized, sets, cards);
////            System.out.println("Time one frame: " + (System.currentTimeMillis() - start) + " ms");
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }

//        return mat2Img.getImage(result);
        return mat2Img.getImage(original);
    }
}