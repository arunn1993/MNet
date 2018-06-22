package com.simp.mnet;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    private enum MODES {
        NAME,
        EMAIL,
        PHONE,
        TYPE,
        SUCCESS,
    }

    private MODES currentMode;

    private RelativeLayout relativeLayout;

    private RelativeLayout nameLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout phoneLayout;
    private RelativeLayout typeLayout;
    private RelativeLayout successLayout;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Spinner typeSpinner;

    private String selectedType;

    private ImageView homeLayout;
    private RelativeLayout nextLayout;
    private RelativeLayout prevLayout;
    private RelativeLayout submitLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        currentMode = MODES.NAME;

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        nextLayout = (RelativeLayout) findViewById(R.id.nextLayout);
        prevLayout = (RelativeLayout) findViewById(R.id.prevLayout);
        submitLayout = (RelativeLayout) findViewById(R.id.submitLayout);
        homeLayout = (ImageView) findViewById(R.id.homeLayout);

        nameLayout = (RelativeLayout) findViewById(R.id.nameLayout);
        emailLayout = (RelativeLayout) findViewById(R.id.emailLayout);
        phoneLayout = (RelativeLayout) findViewById(R.id.phoneLayout);
        typeLayout = (RelativeLayout) findViewById(R.id.typeLayout);
        successLayout = (RelativeLayout) findViewById(R.id.successLayout);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);

        nextLayout.setOnClickListener(nextClickListener);
        prevLayout.setOnClickListener(prevClickListener);
        submitLayout.setOnClickListener(submitClickListener);
        homeLayout.setOnClickListener(homeClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, R.layout.type_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        typeSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
    }

    private AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             selectedType = (String)adapterView.getItemAtPosition(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private View.OnClickListener nextClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation slideLeft = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_left);
            Animation slideLeftCurrent = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_left_current);
            switch (currentMode) {
                case NAME:
                    if(TextUtils.isEmpty(nameEditText.getText())) {
                        showAlert(relativeLayout, "Please enter a name");
                    } else {
                        prevLayout.setVisibility(View.VISIBLE);
                        nextLayout.setBackground(getResources().getDrawable(R.drawable.next_btn_bg_corner));
                        nameLayout.setVisibility(View.GONE);
                        nameLayout.startAnimation(slideLeftCurrent);
                        emailLayout.setVisibility(View.VISIBLE);
                        emailLayout.startAnimation(slideLeft);
                        currentMode = MODES.EMAIL;
                    }
                    break;
                case EMAIL:
                    if(!isValidEmail(emailEditText.getText())) {
                        showAlert(relativeLayout, "Please enter a valid email");
                    } else {
                        emailLayout.setVisibility(View.GONE);
                        emailLayout.startAnimation(slideLeftCurrent);
                        phoneLayout.setVisibility(View.VISIBLE);
                        phoneLayout.startAnimation(slideLeft);
                        currentMode = MODES.PHONE;
                    }
                    break;
                case PHONE:
                    if(!isValidPhone((phoneEditText.getText()))) {
                        showAlert(relativeLayout, "Please enter a valid phone number");
                    } else {
                        phoneLayout.setVisibility(View.GONE);
                        phoneLayout.startAnimation(slideLeftCurrent);
                        typeLayout.setVisibility(View.VISIBLE);
                        typeLayout.startAnimation(slideLeft);
                        nextLayout.setVisibility(View.GONE);
                        submitLayout.setVisibility(View.VISIBLE);
                        currentMode = MODES.TYPE;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private View.OnClickListener prevClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation slideRight = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_right);
            Animation slideRightCurrent = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_right_current);
            switch (currentMode) {
                case EMAIL:
                    prevLayout.setVisibility(View.GONE);
                    nextLayout.setBackground(getResources().getDrawable(R.drawable.next_btn_bg));
                    emailLayout.setVisibility(View.GONE);
                    emailLayout.startAnimation(slideRightCurrent);
                    nameLayout.setVisibility(View.VISIBLE);
                    nameLayout.startAnimation(slideRight);
                    currentMode = MODES.NAME;
                    break;
                case PHONE:
                    phoneLayout.setVisibility(View.GONE);
                    phoneLayout.startAnimation(slideRightCurrent);
                    emailLayout.setVisibility(View.VISIBLE);
                    emailLayout.startAnimation(slideRight);
                    currentMode = MODES.EMAIL;
                    break;
                case TYPE:
                    typeLayout.setVisibility(View.GONE);
                    typeLayout.startAnimation(slideRightCurrent);
                    phoneLayout.setVisibility(View.VISIBLE);
                    phoneLayout.startAnimation(slideRight);
                    nextLayout.setVisibility(View.VISIBLE);
                    submitLayout.setVisibility(View.GONE);
                    currentMode = MODES.PHONE;
                    break;
                default:
                    break;
            }
        }
    };

    private View.OnClickListener submitClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation slideLeft = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_left);
            Animation slideLeftCurrent = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_left_current);
            if(TextUtils.isEmpty(selectedType)) {
                showAlert(relativeLayout, "Select a type");
            } else {
                typeLayout.setVisibility(View.GONE);
                typeLayout.startAnimation(slideLeftCurrent);
                successLayout.setVisibility(View.VISIBLE);
                successLayout.startAnimation(slideLeft);
                submitLayout.setVisibility(View.GONE);
                prevLayout.setVisibility(View.GONE);
                homeLayout.setVisibility(View.VISIBLE);
                nameEditText.setText("");
                emailEditText.setText("");
                phoneEditText.setText("");
                typeSpinner.setSelection(0);
                currentMode = MODES.SUCCESS;
            }
        }
    };

    private View.OnClickListener homeClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation slideLeft = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_left);
            Animation slideLeftCurrent = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_left_current);
            successLayout.setVisibility(View.GONE);
            successLayout.startAnimation(slideLeftCurrent);
            nameLayout.setVisibility(View.VISIBLE);
            nameLayout.startAnimation(slideLeft);
            nextLayout.setVisibility(View.VISIBLE);
            homeLayout.setVisibility(View.GONE);
            currentMode = MODES.NAME;
        }
    };

    private void showAlert(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.error));
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextSize(20);
        textView.setTextColor(getResources().getColor(R.color.white));
        snackbar.show();
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValidPhone(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches());
    }
}
