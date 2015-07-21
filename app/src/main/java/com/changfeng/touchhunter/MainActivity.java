package com.changfeng.touchhunter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MainActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    public static final String SLIDER_INTERVAL = "slider interval";
    public static final String IMAGE_INTERVAL = "image interval";

    public static final String SLIDER_INTERVAL_RADIO_ID = "slider interval radio id";
    public static final String IMAGE_INTERVAL_RADIO_ID = "image interval radio id";

    public static final String STOP_SERVICE = "stop service";

    RadioButton sliderThirtySecondsRadioButton;
    RadioButton sliderSixtySecondsRadioButton;
    RadioButton sliderTwoMinutesRadioButton;
    RadioButton sliderFiveMinutesRadioButton;
    RadioButton sliderTenMinutesRadioButton;

    RadioButton imageFiveSecondsRadioButton;
    RadioButton imageTenSecondsRadioButton;
    RadioButton imageFifteenSecondsRadioButton;
    RadioButton imageTwentySecondsRadioButton;
    RadioButton imageThirtySecondsRadioButton;

    RadioGroup sliderRadioGroup;
    RadioGroup imageRadioGroup;

    int sliderInterval;
    int imageInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startHunterButton = (Button) findViewById(R.id.start_hunter_button);
        Button exitButton = (Button) findViewById(R.id.exit_button);
        Button stopServiceButton = (Button) findViewById(R.id.stop_hunter_service_button);

        sliderThirtySecondsRadioButton = (RadioButton) findViewById(R.id.thirty_seconds_radio);
        sliderSixtySecondsRadioButton = (RadioButton) findViewById(R.id.sixty_seconds_radio);
        sliderTwoMinutesRadioButton = (RadioButton) findViewById(R.id.two_minutes_radio);
        sliderFiveMinutesRadioButton = (RadioButton) findViewById(R.id.five_minutes_radio);
        sliderTenMinutesRadioButton = (RadioButton) findViewById(R.id.ten_minutes_radio);

        imageFiveSecondsRadioButton = (RadioButton) findViewById(R.id.image_five_seconds_radio);
        imageTenSecondsRadioButton = (RadioButton) findViewById(R.id.image_ten_seconds_radio);
        imageFifteenSecondsRadioButton = (RadioButton) findViewById(R.id.image_fifteen_seconds_radio);
        imageTwentySecondsRadioButton = (RadioButton) findViewById(R.id.image_twenty_seconds_radio);


        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);


        setSliderRadio(pref.getInt(SLIDER_INTERVAL_RADIO_ID, R.id.thirty_seconds_radio));
        setImageRadio(pref.getInt(IMAGE_INTERVAL_RADIO_ID, R.id.image_five_seconds_radio));

        sliderRadioGroup = (RadioGroup) findViewById(R.id.slider_radio_group);
        imageRadioGroup = (RadioGroup) findViewById(R.id.image_radio_group);

        startHunterButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        stopServiceButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_hunter_button:
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putInt(SLIDER_INTERVAL, getSliderInterval(sliderRadioGroup.getCheckedRadioButtonId()));
                editor.putInt(IMAGE_INTERVAL, getImageInterval(imageRadioGroup.getCheckedRadioButtonId()));
                editor.putInt(SLIDER_INTERVAL_RADIO_ID, sliderRadioGroup.getCheckedRadioButtonId());
                editor.putInt(IMAGE_INTERVAL_RADIO_ID, imageRadioGroup.getCheckedRadioButtonId());
                editor.putBoolean(STOP_SERVICE, false);
                editor.commit();

                startService(new Intent(this, LongRunningService.class));
                break;
            case R.id.exit_button:
                finish();
                break;
            case R.id.stop_hunter_service_button:
                stopHunterService();
            default:
                break;
        }
    }

    void setSliderRadio(int id) {
        switch (id) {
            case R.id.thirty_seconds_radio:
                sliderThirtySecondsRadioButton.setChecked(true);
                break;
            case R.id.sixty_seconds_radio:
                sliderSixtySecondsRadioButton.setChecked(true);
                break;
            case R.id.two_minutes_radio:
                sliderTwoMinutesRadioButton.setChecked(true);
                break;
            case R.id.five_minutes_radio:
                sliderFiveMinutesRadioButton.setChecked(true);
                break;
            case R.id.ten_minutes_radio:
                sliderTenMinutesRadioButton.setChecked(true);
                break;
            default:
                sliderThirtySecondsRadioButton.setChecked(true);
                break;
        }
    }
    int getSliderInterval(int id) {
        int interval;
        switch (id) {
            case R.id.thirty_seconds_radio:
                interval = 30;
                break;
            case R.id.sixty_seconds_radio:
                interval = 60;
                break;
            case R.id.two_minutes_radio:
                interval = 120;
                break;
            case R.id.five_minutes_radio:
                interval = 300;
                break;
            case R.id.ten_minutes_radio:
                interval = 600;
                break;
            default:
                interval = 30;
                break;
        }
        return interval;
    }

    void setImageRadio(int id) {
        switch (id) {
            case R.id.image_five_seconds_radio:
                imageFiveSecondsRadioButton.setChecked(true);
                break;
            case R.id.image_ten_seconds_radio:
                imageTenSecondsRadioButton.setChecked(true);
                break;
            case R.id.image_fifteen_seconds_radio:
                imageFifteenSecondsRadioButton.setChecked(true);
                break;
            case R.id.image_twenty_seconds_radio:
                imageTwentySecondsRadioButton.setChecked(true);
                break;
            case R.id.image_thirty_seconds_radio:
                imageThirtySecondsRadioButton.setChecked(true);
                break;
            default:
                imageFiveSecondsRadioButton.setChecked(true);
                break;
        }
    }

    int getImageInterval(int id) {
        int interval;
        switch (id) {
            case R.id.image_five_seconds_radio:
                interval = 5;
                break;
            case R.id.image_ten_seconds_radio:
                interval = 10;
                break;
            case R.id.image_fifteen_seconds_radio:
                interval = 15;
                break;
            case R.id.image_twenty_seconds_radio:
                interval = 20;
                break;
            case R.id.image_thirty_seconds_radio:
                interval = 30;
                break;
            default:
                interval = 5;
                break;
        }
        return interval;
    }


    void stopHunterService() {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putBoolean(STOP_SERVICE, true);
        editor.commit();
    }



}
