package com.changfeng.touchhunter;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class ImageSliderActivity extends Activity implements BaseSliderView.OnSliderClickListener {

    private static final String TAG = "ImageSliderActivity";
    private static final String IMAGE_FOLDER = "sliderimages";

    private static int imageInterval = 5;

    private static final String IMAGE_FOLDER_PATH_EXTERNAL = "/mnt/extsd/"+IMAGE_FOLDER;
    ArrayList<String> imageUrls;

    SliderLayout sliderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);

        sliderLayout = (SliderLayout) findViewById(R.id.slider);


        imageUrls = getImageUrls(IMAGE_FOLDER_PATH_EXTERNAL);

        if (imageUrls.isEmpty()) {
            imageUrls = getImageUrls(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + IMAGE_FOLDER);
        }

        HashMap<String, File> file_maps = new HashMap<>();
        for (String s : imageUrls) {
            file_maps.put(s, new File(s));
        }


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getApplicationContext());
//            initialize  a SliderLayout
            textSliderView
//                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);


            sliderLayout.addSlider(textSliderView);

            imageInterval = getSharedPreferences("data", MODE_PRIVATE).getInt(MainActivity.IMAGE_INTERVAL, 5);
            sliderLayout.setDuration(imageInterval);
        }

    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        finish();
    }

    private ArrayList<String> getImageUrls(String path) {
        ArrayList<String> urls = new ArrayList<>();
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                if (isImageFileType(file)) {
                    urls.add(file.getAbsolutePath());
                }
            }
        }
        return urls;
    }
    static private String getFileSuffix(File file) {
        String filename = file.getName().trim();
        int lastDotIndex = file.getName().lastIndexOf('.');

        if (lastDotIndex >= 0 && lastDotIndex < filename.length()) {
            return file.getName().substring(lastDotIndex + 1, filename.length());
        } else {
            return "";
        }
    }

    static private boolean isImageFileType(File file) {
        return getFileSuffix(file).equals(ImageFileType.JPG)
                || getFileSuffix(file).equals(ImageFileType.PNG)
                || getFileSuffix(file).equals(ImageFileType.BMP);
    }
    class ImageFileType {
        static final String JPG = "jpg";
        static final String PNG = "png";
        static final String BMP = "bmp";
    }

}
