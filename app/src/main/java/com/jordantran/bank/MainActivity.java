package com.jordantran.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordantran.bank.domain.dto.BankStatusDTO;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private ObjectMapper objectMapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init
        this.objectMapper = new ObjectMapper();
        String status = getStatus();
        setContentsOfTextView(R.id.outputStatus, status);
    }

    /* Sets the content of a text label */
    private void setContentsOfTextView(int id, String newContents) {
        View view = findViewById(id);
        TextView textView = (TextView) view;
        textView.setText(newContents);
    }

    /* Gets the input of a text input */
    private String getInputOfTextField(int id) {
        View view = findViewById(id);
        EditText editText = (EditText) view;
        String input = editText.getText().toString();
        return input;
    }

    /* Gets the item selected from a spinner drop down menu */
    private String getItemSelected(int id) {
        View view = findViewById(id);
        Spinner spinner = (Spinner) view;
        String string = spinner.getSelectedItem().toString();
        return string;
    }

    /* Helper Methods */

    public String getStatus() {

        // 'final' so that result is fixed so inner class, of Callback, will use same reference, to avoid any desynchronization
        final String[] result = new String[1];

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://jordantran-bookapi.k9hqccrxv6fxw.ca-central-1.cs.amazonlightsail.com/api/v1/bank/status")
                .build();

        client.newCall(request).enqueue(new Callback() {

            // In case http request cannot be sent
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {

                    // If http response status was not successful e.g. not 200s
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    
                    String responseJson = responseBody.string();

                    BankStatusDTO bankStatusDTO = objectMapper.readValue(responseJson, BankStatusDTO.class);

                    result[0] = bankStatusDTO.getStatus();
                }
            }
        });


        return result[0];
    }


//    /*
//    When add a new account is pressed, will retrieve the name and initial balance and use that to create a new client and then output the status of the bank
//     */
//    public void computeButtonAddNewAccountClicked(View view) {
//        String name = getInputOfTextField(R.id.inputClientName);
//        double amount = Double.parseDouble(getInputOfTextField(R.id.inputStartBalance));
//        bank.addClient(name, amount);
//        setContentsOfTextView(R.id.outputStatus, bank.getStatus());
//    }
//
//    /*
//    Performs the service depending on what service chosen in service type drop down menu.
//        - If deposit, will get input amount and input name to deposit into
//        - If withdraw, will get input amount and input name to withdraw from
//        - If transfer, will get input names and input amount to transfer
//        - If print statement, will get input name and print the client's statement
//        - If an error occurs, status of bank will output the appropriate error
//     */
//    public void computeButtonConfirmClicked(View view) {
//        String optionSelected = getItemSelected(R.id.optionsServiceType);
//        double amount = 0;
//        String output = "";
//
//        if(!optionSelected.equals("Print Statement")) {
//            amount = Integer.parseInt(this.getInputOfTextField(R.id.inputAmount));
//        }
//
//        if(optionSelected.equals("Deposit")) {
//            String name = this.getInputOfTextField(R.id.inputToAccount);
//            bank.deposit(name, amount);
//        }
//        else if(optionSelected.equals("Withdraw")) {
//            String name = this.getInputOfTextField(R.id.inputFromAccount);
//            bank.withdraw(name, amount);
//        }
//        else if(optionSelected.equals("Transfer")) {
//            String fromName = this.getInputOfTextField(R.id.inputFromAccount);
//            String toName = this.getInputOfTextField(R.id.inputToAccount);
//            bank.transfer(fromName, toName, amount);
//        }
//        else if(optionSelected.equals("Print Statement")) {
//            String name = this.getInputOfTextField(R.id.inputFromAccount);
//
//            String[] statement = bank.getStatement(name);
//            for(int i = 0; i < statement.length; i++) {
//                output += String.format("%s", statement[i]);
//                if (i < statement.length - 1) {
//                    output += "\n";
//                }
//            }
//        }
//
//
//        if(!optionSelected.equals("Print Statement")) {
//            output = bank.getStatus();
//        }
//        setContentsOfTextView(R.id.outputStatus, output);
//    }

}