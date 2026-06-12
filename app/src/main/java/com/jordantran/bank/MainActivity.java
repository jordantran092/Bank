package com.jordantran.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {

    ObjectMapper objectMapper;

    public MainActivity() {
        this.objectMapper = new ObjectMapper();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setContentsOfTextView(R.id.outputStatus, getStatus());
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

    private String getStatus() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://jordantran-bookapi.k9hqccrxv6fxw.ca-central-1.cs.amazonlightsail.com/api/v1/bank/status")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseJson = response.body().string();

            BankStatusDTO bankStatusDTO = objectMapper.readValue(responseJson, BankStatusDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return
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