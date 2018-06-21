package com.simp.mnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private enum MODES {
        NAME,
        EMAIL,
        PHONE,
        TYPE,
        SUCCESS,
    }

    private MODES currentMode;

    private RelativeLayout nameLayout;
    private RelativeLayout emailLayout;
    private RelativeLayout phoneLayout;
    private RelativeLayout typeLayout;
    private RelativeLayout successLayout;

    private TextView nameError;
    private TextView emailError;
    private TextView phoneError;
    private TextView typeError;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private Spinner typeSpinner;

    private String selectedType;

    private Button nextButton;
    private Button previousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentMode = MODES.NAME;

        nextButton = (Button) findViewById(R.id.fabNext);
        previousButton = (Button) findViewById(R.id.fabPrev);

        nameLayout = (RelativeLayout) findViewById(R.id.nameLayout);
        emailLayout = (RelativeLayout) findViewById(R.id.emailLayout);
        phoneLayout = (RelativeLayout) findViewById(R.id.phoneLayout);
        typeLayout = (RelativeLayout) findViewById(R.id.typeLayout);
        successLayout = (RelativeLayout) findViewById(R.id.successLayout);

        nameError = (TextView) findViewById(R.id.nameError);
        emailError = (TextView) findViewById(R.id.emailError);
        phoneError = (TextView) findViewById(R.id.phoneError);
        typeError = (TextView) findViewById(R.id.typeError);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);

        nextButton.setOnClickListener(nextClickListener);
        previousButton.setOnClickListener(prevClickListener);

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
            Animation zoomIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in);
            Animation zoomOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_out);
            switch (currentMode) {
                case NAME:
                    if(TextUtils.isEmpty(nameEditText.getText())) {
                        nameError.setVisibility(View.VISIBLE);
                    } else {
                        previousButton.setVisibility(View.VISIBLE);
                        previousButton.startAnimation(zoomIn);
                        nameError.setVisibility(View.GONE);
                        nameLayout.setVisibility(View.GONE);
                        nameLayout.startAnimation(slideLeftCurrent);
                        emailLayout.setVisibility(View.VISIBLE);
                        emailLayout.startAnimation(slideLeft);
                        currentMode = MODES.EMAIL;
                    }
                    break;
                case EMAIL:
                    if(!isValidEmail(emailEditText.getText())) {
                        emailError.setVisibility(View.VISIBLE);
                    } else {
                        emailError.setVisibility(View.GONE);
                        emailLayout.setVisibility(View.GONE);
                        emailLayout.startAnimation(slideLeftCurrent);
                        phoneLayout.setVisibility(View.VISIBLE);
                        phoneLayout.startAnimation(slideLeft);
                        currentMode = MODES.PHONE;
                    }
                    break;
                case PHONE:
                    if(!isValidPhone((phoneEditText.getText()))) {
                        phoneError.setVisibility(View.VISIBLE);
                    } else {
                        phoneError.setVisibility(View.GONE);
                        phoneLayout.setVisibility(View.GONE);
                        phoneLayout.startAnimation(slideLeftCurrent);
                        typeLayout.setVisibility(View.VISIBLE);
                        typeLayout.startAnimation(slideLeft);
                        currentMode = MODES.TYPE;
                    }
                    break;
                case TYPE:
                    if(TextUtils.isEmpty(selectedType)) {
                        typeError.setVisibility(View.VISIBLE);
                    } else {
                        previousButton.setVisibility(View.GONE);
                        previousButton.startAnimation(zoomOut);
                        typeError.setVisibility(View.GONE);
                        typeLayout.setVisibility(View.GONE);
                        typeLayout.startAnimation(slideLeftCurrent);
                        successLayout.setVisibility(View.VISIBLE);
                        successLayout.startAnimation(slideLeft);
                        nameEditText.setText("");
                        emailEditText.setText("");
                        phoneEditText.setText("");
                        typeSpinner.setSelection(0);
                        currentMode = MODES.SUCCESS;
                    }
                    break;
                case SUCCESS:
                    successLayout.setVisibility(View.GONE);
                    successLayout.startAnimation(slideLeftCurrent);
                    nameLayout.setVisibility(View.VISIBLE);
                    nameLayout.startAnimation(slideLeft);
                    currentMode = MODES.NAME;
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
            Animation zoomOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_out);
            switch (currentMode) {
                case EMAIL:
                    previousButton.setVisibility(View.GONE);
                    previousButton.startAnimation(zoomOut);
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
                    currentMode = MODES.PHONE;
                    break;
                default:
                    break;
            }
        }
    };

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public boolean isValidPhone(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches());
    }
}
