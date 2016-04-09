package com.example.dougfultz.unitconverter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.math.BigDecimal;
import java.math.MathContext;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "UnitConverter";

    private final Integer[] edit_XX = new Integer[]{
            R.id.edit_Bytes, //0
            R.id.edit_KB,    //1
            R.id.edit_MB,    //2
            R.id.edit_GB,    //3
            R.id.edit_TB,    //4
            R.id.edit_PB,    //5
            R.id.edit_EB,    //6
            R.id.edit_ZB,    //7
            R.id.edit_YB     //8
    };

    private final BigDecimal[] bigDecimalsValues = new BigDecimal[]{
            new BigDecimal(-1), //0
            new BigDecimal(-1), //1
            new BigDecimal(-1), //2
            new BigDecimal(-1), //3
            new BigDecimal(-1), //4
            new BigDecimal(-1), //5
            new BigDecimal(-1), //6
            new BigDecimal(-1), //7
            new BigDecimal(-1)  //8
    };

    private void createInputTextWatchers() {
        Log.d(TAG, "createInputTextWatchers: Begin");

        EditText tempEditText;
        InputTextWatcher tempInputTextWatcher;

        Log.d(TAG, "createInputTextWatchers: edit_XX.length=" + edit_XX.length);

        for (Integer anEdit_XX : edit_XX) {
            tempEditText = (EditText) findViewById(anEdit_XX);
            tempInputTextWatcher = new InputTextWatcher(tempEditText);
            assert tempEditText != null;
            Log.d(TAG, "createInputTextWatchers: Adding text watcher to " + tempEditText.getResources().getResourceName(tempEditText.getId()));
            tempEditText.addTextChangedListener(tempInputTextWatcher);
        }

        Log.d(TAG, "createInputTextWatchers: End");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Begin");

        Log.d(TAG, "onCreate: Build GUI from XML");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create and add text watchers to text boxes
        createInputTextWatchers();

        Log.d(TAG, "onCreate: Set first text box to 0");
        EditText tempEditText = (EditText) findViewById(edit_XX[0]);
        assert tempEditText != null;
        tempEditText.setText("0");

        Log.d(TAG, "onCreate: End");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private Integer findIndexOfEdit_XX(Integer val) {
        Log.d(TAG, "findIndexOfEdit_XX: Begin");

        Log.d(TAG, "findIndexOfEdit_XX: val=" + val);
        for (int i = 0; i < edit_XX.length; i++) {
            Log.d(TAG, "findIndexOfEdit_XX: edit_XXX[" + i + "]=" + edit_XX[i]);

            if (edit_XX[i].equals(val)) {
                Log.d(TAG, "findIndexOfEdit_XX: Found " + val + " at index: " + i);
                Log.d(TAG, "findIndexOfEdit_XX: End");
                return (i);
            }
        }

        Log.d(TAG, "findIndexOfEdit_XX: Not found!");
        Log.d(TAG, "findIndexOfEdit_XX: End");
        return (-1);
    }

    private void formatBigDecimal(BigDecimal num) {
        //Returns properly formatted BigDecimal
        Log.d(TAG, "formatString: Begin");

        Log.d(TAG, "formatString: num.scale()=" + num.scale());
        Log.d(TAG, "formatString: num.precision()=" + num.precision());
        //Log.d(TAG, "formatString: num.precision()="+num.);

        Log.d(TAG, "formatString: End");
    }

    private class InputTextWatcher implements TextWatcher {
        private final EditText textBox;

        private InputTextWatcher(EditText textBox) {
            this.textBox = textBox;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": Begin");

            Integer changedIndex;
            BigDecimal current, previous, multiple;
            EditText tempEditText;
            String tempString;

            Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + "=[" + textBox.getText() + "]");

            if (!textBox.getText().toString().equals("")) {
                multiple = new BigDecimal(1024);
                changedIndex = findIndexOfEdit_XX(textBox.getId());
                current = new BigDecimal(textBox.getText().toString());
                previous = bigDecimalsValues[changedIndex];
                Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": was: " + previous.toString() + " is: " + current.toString());

                Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": removing TextChangedListener");
                textBox.removeTextChangedListener(this);

                //Check if the text actually changed
                if (current.equals(previous)) {
                    Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": text didn't change");
                } else {
                    //update value for changed item
                    Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": text changed, updating value");
                    bigDecimalsValues[changedIndex] = new BigDecimal(textBox.getText().toString());

                    //recalculate all values before changed text box (multiplying by 1024)
                    for (int i = changedIndex - 1; i >= 0; i--) {
                        bigDecimalsValues[i] = bigDecimalsValues[changedIndex].multiply(multiple.pow(changedIndex - i));
                        //bigDecimalsValues[i] = bigDecimalsValues[i + 1].multiply(multiple);
                        Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": changing value [" + i + "]=" + bigDecimalsValues[i].toString());
                    }

                    //recalculate all values after changed text box (dividing by 1024)
                    for (int i = changedIndex + 1; i < bigDecimalsValues.length; i++) {
                        //noinspection BigDecimalMethodWithoutRoundingCalled
                        bigDecimalsValues[i] = bigDecimalsValues[changedIndex].divide(multiple.pow(i - changedIndex));
                        //bigDecimalsValues[i] = bigDecimalsValues[i - 1].divide(multiple);
                        Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": changing value [" + i + "]=" + bigDecimalsValues[i].toString());
                    }

                    //update all values
                    for (int i = 0; i < edit_XX.length; i++) {
                        formatBigDecimal(bigDecimalsValues[i]);

                        if (i == changedIndex)
                            continue;

                        //set precision
                        Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": precision=" + bigDecimalsValues[i].precision());
                        int precision=getResources().getInteger(R.integer.precision);
                        Log.d(TAG, bigDecimalsValues[i].precision() + " " + precision);
                        if (bigDecimalsValues[i].precision() > precision) {
                            Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": setting precision=" + precision);
                            bigDecimalsValues[i] = new BigDecimal(bigDecimalsValues[i].toString(), new MathContext(precision));
                        }

                        tempEditText = (EditText) findViewById(edit_XX[i]);
                        //tempString = bigDecimalsValues[i].toString();
                        tempString = bigDecimalsValues[i].toEngineeringString();
                        Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": setting text to: " + tempString);
                        assert tempEditText != null;
                        tempEditText.setText(tempString);
                    }
                }

                Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": adding TextChangedListener");
                textBox.addTextChangedListener(this);

                Log.d(TAG, "afterTextChange: " + textBox.getResources().getResourceName(textBox.getId()) + ": End");
            }
        }
    }
}
