package com.example.nexusproject_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.synap.pay.SynapPayButton;
import com.synap.pay.handler.payment.SynapAuthorizeHandler;
import com.synap.pay.model.payment.SynapAddress;
import com.synap.pay.model.payment.SynapCardStorage;
import com.synap.pay.model.payment.SynapCountry;
import com.synap.pay.model.payment.SynapCurrency;
import com.synap.pay.model.payment.SynapDocument;
import com.synap.pay.model.payment.SynapFeatures;
import com.synap.pay.model.payment.SynapMetadata;
import com.synap.pay.model.payment.SynapOrder;
import com.synap.pay.model.payment.SynapPerson;
import com.synap.pay.model.payment.SynapProduct;
import com.synap.pay.model.payment.SynapSettings;
import com.synap.pay.model.payment.SynapTransaction;
import com.synap.pay.model.payment.response.SynapAuthorizeResponse;
import com.synap.pay.model.security.SynapAuthenticator;
import com.synap.pay.theming.SynapLightTheme;
import com.synap.pay.theming.SynapTheme;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasarelaDePago extends AppCompatActivity {
    private SynapPayButton paymentWidget;
    private FrameLayout synapForm;
    private Button synapButton;

    public void openTarjetasLink(View view) {
        // Abre el enlace en un navegador
        String url = "https://docs.synapsolutions.com/payments";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasarela_de_pago);

        // Asociar elementos UI
        synapForm = findViewById(R.id.contenedorTarjetas);
        synapForm.setVisibility(View.GONE);

        synapButton = findViewById(R.id.btnPagar);
        synapButton.setVisibility(View.GONE);
        synapButton.setOnClickListener(view -> paymentWidget.pay());

        Button startPayment = findViewById(R.id.btnContinuar);
        startPayment.setOnClickListener(view -> startPayment());
    }

    private void startPayment() {
        // Hacer visible el CardView y el botón de pago
        synapForm.setVisibility(View.VISIBLE);
        synapButton.setVisibility(View.VISIBLE);

        // Hacer invisible el botón "Continuar"
        Button startPayment = findViewById(R.id.btnContinuar);
        startPayment.setVisibility(View.GONE);  // Ocultar el botón una vez presionado

        try {
            this.paymentWidget = SynapPayButton.create(synapForm);

            SynapTheme theme = new SynapLightTheme();
            SynapPayButton.setTheme(theme);
            SynapPayButton.setEnvironment(SynapPayButton.Environment.SANDBOX);

            SynapTransaction transaction = this.buildTransaction();
            SynapAuthenticator authenticator = this.buildAuthenticator(transaction);

            SynapPayButton.setListener(event -> {
                switch (event) {
                    case START_PAY:
                        synapButton.setVisibility(View.GONE);
                        break;
                    case INVALID_CARD_FORM:
                        synapButton.setVisibility(View.VISIBLE);
                        break;
                    case VALID_CARD_FORM:
                        break;
                }
            });

            this.paymentWidget.configure(authenticator, transaction, new SynapAuthorizeHandler() {
                @Override
                public void success(SynapAuthorizeResponse response) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if (response.getResult() != null && response.getResult().getAccepted()) {
                            showMessage("Pago exitoso: " + response.getResult().getMessage());
                        } else if (response.getResult() != null) {
                            showMessage("Pago denegado: " + response.getResult().getMessage());
                        } else {
                            Toast.makeText(PasarelaDePago.this, "Error: respuesta de pago inválida.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void failed(SynapAuthorizeResponse response) {
                    new Handler(Looper.getMainLooper()).post(() -> showMessage("Error: " + response.getMessage().getText()));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al configurar el widget: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private SynapTransaction buildTransaction() {
        String number = String.valueOf(System.currentTimeMillis());

        SynapCountry country = new SynapCountry();
        country.setCode("PER");

        SynapCurrency currency = new SynapCurrency();
        currency.setCode("PEN");

        String amount = "1.00";

        SynapPerson customer = new SynapPerson();
        customer.setName("Javier");
        customer.setLastName("Pérez");

        SynapAddress address = new SynapAddress();
        address.setCountry("PER");
        address.setLevels(new ArrayList<>());
        address.getLevels().add("150000");
        address.setLine1("Ca Carlos Ferreyros 180");
        address.setZip("15036");
        customer.setAddress(address);

        customer.setEmail("javier.perez@synapsolutions.com");
        customer.setPhone("999888777");

        SynapDocument document = new SynapDocument();
        document.setType("DNI");
        document.setNumber("44556677");
        customer.setDocument(document);

        SynapPerson shipping = customer;
        SynapPerson billing = customer;

        SynapProduct productItem = new SynapProduct();
        productItem.setCode("123");
        productItem.setName("Llavero");
        productItem.setQuantity("1");
        productItem.setUnitAmount("1.00");
        productItem.setAmount("1.00");

        List<SynapProduct> products = new ArrayList<>();
        products.add(productItem);

        SynapMetadata metadataItem = new SynapMetadata();
        metadataItem.setName("nombre1");
        metadataItem.setValue("valor1");

        List<SynapMetadata> metadataList = new ArrayList<>();
        metadataList.add(metadataItem);

        SynapOrder order = new SynapOrder();
        order.setNumber(number);
        order.setAmount(amount);
        order.setCountry(country);
        order.setCurrency(currency);
        order.setProducts(products);
        order.setCustomer(customer);
        order.setShipping(shipping);
        order.setBilling(billing);
        order.setMetadata(metadataList);

        SynapSettings settings = new SynapSettings();
        settings.setBrands(Arrays.asList("VISA", "MSCD", "AMEX", "DINC"));
        settings.setLanguage("es_PE");
        settings.setBusinessService("MOB");

        SynapTransaction transaction = new SynapTransaction();
        transaction.setOrder(order);
        transaction.setSettings(settings);

        SynapFeatures features = new SynapFeatures();
        SynapCardStorage cardStorage = new SynapCardStorage();
        cardStorage.setUserIdentifier("javier.perez@synapsolutions.com");
        features.setCardStorage(cardStorage);
        transaction.setFeatures(features);

        return transaction;
    }

    private SynapAuthenticator buildAuthenticator(SynapTransaction transaction) {
        String apiKey = "ab254a10-ddc2-4d84-8f31-d3fab9d49520";
        String signatureKey = "eDpehY%YPYgsoludCSZhu*WLdmKBWfAo";
        String signature = generateSignature(transaction, apiKey, signatureKey);

        SynapAuthenticator authenticator = new SynapAuthenticator();
        authenticator.setIdentifier(apiKey);
        authenticator.setSignature(signature);

        return authenticator;
    }

    private void showMessage(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton("OK", (dialog, id) -> {
            Handler looper = new Handler(getApplicationContext().getMainLooper());
            looper.post(() -> {
                synapForm.setVisibility(View.GONE);
                synapButton.setVisibility(View.GONE);
            });
            dialog.cancel();
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private String generateSignature(SynapTransaction transaction, String apiKey, String signatureKey) {
        // Implementación de firma SHA-512
        return "eDpehY%YPYgsoludCSZhu*WLdmKBWfAo"; // Ejemplo de retorno.
    }
}
