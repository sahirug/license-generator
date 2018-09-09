package com.home.test.ui;

import com.google.gson.JsonObject;
import com.home.test.model.License;
import com.home.test.utils.Constants;
import com.home.test.utils.DecryptionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class GeneratorController {
    @FXML
    private TextField machineId;

    @FXML
    private TextField agentCount;

    @FXML
    private TextField threadCount;

    @FXML
    private DatePicker start;

    @FXML
    private DatePicker expiry;

    @FXML
    private Button generateButton;

    @FXML
    private TextArea appKey;

    @FXML
    private Button copyButton;

    DecryptionManager manager;

    @FXML
    private void initialize(){
        initDecryptionManager();
        setMachineId();
        start.setValue(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        start.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
        expiry.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(start.getValue()) < 0);
            }
        });
    }

    private void setMachineId() {
        try{
            machineId.setText(manager.decryptMessage("L+I6LRLL7pZ8RNmcCZi5dvL67BYFflz1pkecAa7brEU1IJZxf6Ujm5VneERm8RnCbgaCkKP64V1ZxODadHsiEMbe2+w+86AMKyZvEwj2/XkXxNqLx6qD+x716TZ1g9uC55vuPNvmFt2mPBRxmogsjwqZk3qv1P+Uiux7LaeZ6O7u528C4hVDobX8Lv30YkwItPjM6V5ofHLEiRhqBm/LBcwqy06kVA+ua5YQ7hToxbouO+unen+WgHs3exnsndtBuKiNjgleMui1zFmqSevxwXdUdFOS4wJMoFhpg021txExe/ICkHxP8E1zTtQfLpypUJHkJ/FznbceKhlQgbFiWQ=="));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDecryptionManager() {
        try {
            manager = new DecryptionManager(Constants.PRIVATE_KEY_URL.toURI(), Constants.PUBLIC_KEY_URL.toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateAppKey() throws Exception {
        Date startDate = Date.from(Instant.from(start.getValue().atStartOfDay(ZoneId.systemDefault())));
        Date expiryDate = Date.from(Instant.from(expiry.getValue().atStartOfDay(ZoneId.systemDefault())));
        int agentCount = Integer.valueOf(this.agentCount.getText());
        int threadCount = Integer.valueOf(this.threadCount.getText());
        JsonObject licenseObj = new JsonObject();
        licenseObj.addProperty("name", "license-01");
        licenseObj.addProperty("startDate", startDate.getTime());
        licenseObj.addProperty("endDate", expiryDate.getTime());
        licenseObj.addProperty("computerId", machineId.getText());
        licenseObj.addProperty("agentCount", agentCount);
        licenseObj.addProperty("threadCount", threadCount);

        printToConsole(licenseObj.toString(), "License Object");

        appKey.setText(manager.encryptMessage(licenseObj.toString()));
    }

    public void copyKey() throws Exception {
        printToConsole(manager.decryptMessage(appKey.getText()), "Decrypted License");
    }

    private void printToConsole(String message, String label) {
        System.out.println("=====================================================================================");
        System.out.println("@@ "+ label +" = " + message +" @@");
        System.out.println("=====================================================================================");
    }

}
