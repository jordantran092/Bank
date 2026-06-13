package com.jordantran.bank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordantran.bank.domain.dto.BankStatusDTO;
import com.jordantran.bank.domain.dto.ClientDTO;
import com.jordantran.bank.domain.dto.GetStatementDTO;
import com.jordantran.bank.domain.dto.TransactionDepositWithdrawDTO;
import com.jordantran.bank.domain.dto.TransactionTransferDTO;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ObjectMapper objectMapper;
    private OkHttpClient client;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init
        client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        getBankStatusAndSetOutputStatus();
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



    /*
    When add a new account is pressed, will retrieve the name and initial balance and use that to create a new client and then output the status of the bank
     */
    public void computeButtonAddNewAccountClicked(View view) throws JsonProcessingException {
        String name = getInputOfTextField(R.id.inputClientName);
        double amount = Double.parseDouble(getInputOfTextField(R.id.inputStartBalance));
        addClient(name, amount);

    }


    /*
    Performs the service depending on what service chosen in service type drop down menu.
        - If deposit, will get input amount and input name to deposit into
        - If withdraw, will get input amount and input name to withdraw from
        - If transfer, will get input names and input amount to transfer
        - If print statement, will get input name and print the client's statement
        - If an error occurs, status of bank will output the appropriate error
     */
    public void computeButtonConfirmClicked(View view) throws JsonProcessingException {
        String optionSelected = getItemSelected(R.id.optionsServiceType);
        double amount = 0;

        if(!optionSelected.equals("Print Statement")) {
            amount = Integer.parseInt(this.getInputOfTextField(R.id.inputAmount));
        }

        if(optionSelected.equals("Deposit")) {
            String name = this.getInputOfTextField(R.id.inputToAccount);
            depositOrWithdraw(name, amount, "DEPOSIT");
        }
        else if(optionSelected.equals("Withdraw")) {
            String name = this.getInputOfTextField(R.id.inputFromAccount);
            depositOrWithdraw(name, amount, "WITHDRAW");
        }
        else if(optionSelected.equals("Transfer")) {
            String fromName = this.getInputOfTextField(R.id.inputFromAccount);
            String toName = this.getInputOfTextField(R.id.inputToAccount);
            transfer(fromName, toName, amount);
        }
        else if(optionSelected.equals("Print Statement")) {
            String name = this.getInputOfTextField(R.id.inputFromAccount);

            getStatementAndSetOutputStatus(name);

        }

    }

    private void getStatementAndSetOutputStatus(String name) throws JsonProcessingException {



        Request request = new Request.Builder()
                .url("https://jordantran-bookapi.k9hqccrxv6fxw.ca-central-1.cs.amazonlightsail.com/api/v1/bank/clients/" + name)
                .build();

        /*
         Makes a new call, with enqueue denoting asynchronous. So that the main UI thread is not blocked, and the http call can be run in a background thread

         Create a new Callback as Callback is an interface that needs to be implemented to describe what should be done on failure, and on response
         */
        client.newCall(request).enqueue(new Callback() {

            @Override
            // In case http request cannot be sent
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // If status returned was not success (200)
                if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);


                runOnUiThread(new Runnable() { // since onResponse and onFailure is running on a background thread, must switch back to UI thread to make UI changes
                    // Runnable is essentially used to create the block of code (in run() below) to run on the main UI thread outside of this background thread

                    @Override
                    public void run() {
                        try {
                            String responseJson = response.body().string();

                            GetStatementDTO getStatementDTO = objectMapper.readValue(responseJson, GetStatementDTO.class);

                            List<String> statement = getStatementDTO.getStatement();


                            String output = "";
                            for(int i = 0; i < statement.size(); i++) {
                                output += String.format("%s", statement.get(i));
                                if (i < statement.size() - 1) {
                                    output += "\n";
                                }
                            }

                            setContentsOfTextView(R.id.outputStatus, output);

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private void transfer(String fromName, String toName, double amount) throws JsonProcessingException {

        TransactionTransferDTO transactionTransferDTO = TransactionTransferDTO.builder()
                .fromName(fromName)
                .toName(toName)
                .amount(amount)
                .build();

        String json = objectMapper.writeValueAsString(transactionTransferDTO);

        RequestBody requestBody = RequestBody.create(json, JSON);


        Request request = new Request.Builder()
                .url("https://jordantran-bookapi.k9hqccrxv6fxw.ca-central-1.cs.amazonlightsail.com/api/v1/bank/clients/bulk")
                .patch(requestBody)
                .build();

        /*
         Makes a new call, with enqueue denoting asynchronous. So that the main UI thread is not blocked, and the http call can be run in a background thread

         Create a new Callback as Callback is an interface that needs to be implemented to describe what should be done on failure, and on response
         */
        client.newCall(request).enqueue(new Callback() {

            @Override
            // In case http request cannot be sent
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // If status returned was not success or conflict, where conflict can still be expected
                if (!(response.code() == 200 || response.code() == 409)) throw new IOException("Unexpected code: " + response);

                // Set status only when request has been fulfilled, if set it immediately right after where it was called, it may set status too soon
                getBankStatusAndSetOutputStatus();
            }
        });

    }


    private void depositOrWithdraw(String name, double amount, String transactionType) throws JsonProcessingException {

        TransactionDepositWithdrawDTO transactionDepositWithdrawDTO = TransactionDepositWithdrawDTO.builder()
                .transactionType(transactionType)
                .name(name)
                .amount(amount)
                .build();

        String json = objectMapper.writeValueAsString(transactionDepositWithdrawDTO);

        RequestBody requestBody = RequestBody.create(json, JSON);


        Request request = new Request.Builder()
                .url("https://jordantran-bookapi.k9hqccrxv6fxw.ca-central-1.cs.amazonlightsail.com/api/v1/bank/clients/" + transactionDepositWithdrawDTO.getName())
                .patch(requestBody)
                .build();

        /*
         Makes a new call, with enqueue denoting asynchronous. So that the main UI thread is not blocked, and the http call can be run in a background thread

         Create a new Callback as Callback is an interface that needs to be implemented to describe what should be done on failure, and on response
         */
        client.newCall(request).enqueue(new Callback() {

            @Override
            // In case http request cannot be sent
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // If status returned was not success or conflict, where conflict can still be expected
                if (!(response.code() == 200 || response.code() == 409)) throw new IOException("Unexpected code: " + response);

                // Set status only when request has been fulfilled, if set it immediately right after where it was called, it may set status too soon
                getBankStatusAndSetOutputStatus();
            }
        });

    }


    private void addClient(String name, double amount) throws JsonProcessingException {

        ClientDTO clientDTO = ClientDTO.builder()
                .name(name)
                .balance(amount)
                .build();

        String clientJson = objectMapper.writeValueAsString(clientDTO);

        RequestBody requestBody = RequestBody.create(clientJson, JSON);


        Request request = new Request.Builder()
                .url("https://jordantran-bookapi.k9hqccrxv6fxw.ca-central-1.cs.amazonlightsail.com/api/v1/bank/clients")
                .post(requestBody)
                .build();

        /*
         Makes a new call, with enqueue denoting asynchronous. So that the main UI thread is not blocked, and the http call can be run in a background thread

         Create a new Callback as Callback is an interface that needs to be implemented to describe what should be done on failure, and on response
         */
        client.newCall(request).enqueue(new Callback() {

            @Override
            // In case http request cannot be sent
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // If status returned was not success or conflict, where conflict can still be expected
                if (!(response.code() == 201 || response.code() == 409)) throw new IOException("Unexpected code: " + response);

                // Set status only when request has been fulfilled, if set it right after addClient call where it was called, it may set status too soon
                getBankStatusAndSetOutputStatus();
            }
        });

    }

    private void getBankStatusAndSetOutputStatus() {

        Request request = new Request.Builder()
                .url("https://jordantran-bookapi.k9hqccrxv6fxw.ca-central-1.cs.amazonlightsail.com/api/v1/bank/status")
                .build();

        /*
         Makes a new call, with enqueue denoting asynchronous. So that the main UI thread is not blocked, and the http call can be run in a background thread

         Create a new Callback as Callback is an interface that needs to be implemented to describe what should be done on failure, and on response
         */
        client.newCall(request).enqueue(new Callback() {

            @Override
            // In case http request cannot be sent
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // If status returned was not success
                if (!response.isSuccessful()) throw new IOException("Unexpected code: " + response);


                runOnUiThread(new Runnable() { // since onResponse and onFailure is running on a background thread, must switch back to UI thread to make UI changes
                    // Runnable is essentially used to create the block of code (in run() below) to run on the main UI thread outside of this background thread

                    @Override
                    public void run() {
                        try {
                            String responseJson = response.body().string();

                            BankStatusDTO bankStatusDTO = objectMapper.readValue(responseJson, BankStatusDTO.class);

                            setContentsOfTextView(R.id.outputStatus, bankStatusDTO.getStatus());
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

}