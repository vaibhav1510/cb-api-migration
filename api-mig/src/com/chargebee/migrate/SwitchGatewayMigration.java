/*
 * Copyright (c) 2016 ChargeBee Inc
 * All Rights Reserved.
 */
package com.chargebee.migrate;

import com.chargebee.Environment;
import com.chargebee.Result;
import com.chargebee.models.*;
import com.chargebee.models.enums.Gateway;
import static com.chargebee.util.Utils.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 *
 * @author vaibhav
 */
public class SwitchGatewayMigration {

    public static void main(String[] args) throws Exception {

        String domain = "mannar";//ConsoleUtil.readIn("domain");
        String apiKey = "live___dev__qKxp2Sa4kcXdGD5hyacEXayYxtCkLcu8e";//ConsoleUtil.readIn("api_key");
        String srcGwStr = "pin";
        String destGwStr = "stripe";

        Gateway srcGw = Gateway.valueOf(srcGwStr.toUpperCase());
        Gateway destGw = Gateway.valueOf(destGwStr.toUpperCase());

//        System.setProperty("com.chargebee.api.protocol", "http");
//        System.setProperty("com.chargebee.api.domain.suffix", "localcb.in:8080");
        Environment.configure(domain, apiKey);
        execute(srcGw, destGw);
    }

    private static void execute(Gateway srcGw, Gateway destGw) throws Exception {
        String splitBy = ",";
        File f = new File("output.txt");
        FileWriter fw = new FileWriter(f);

        BufferedReader br = new BufferedReader(new FileReader("cust.csv"));
        String line;
        int i = 0;
        while ((line = br.readLine()) != null) {
            String custId = line.split(splitBy)[0];
            String out;
            i++;
            try {
                Result cust = Customer.retrieve(custId).request();
                if (cust.customer().paymentMethod() == null) {
                    throw new Exception("No card present for this customer");
                }
                if (!cust.customer().paymentMethod().gateway().equals(srcGw)) {
                    throw new Exception("Current Customer's Gateway is not " + srcGw.name());
                }
                Thread.sleep(500);

                Result res = Card.switchGatewayForCustomer(custId)
                        .gateway(destGw).request();

                Customer.PaymentMethod pm = res.customer().paymentMethod();
                out = csv(i, custId, "SUCCESSFUL", "", pm.gateway().name(), pm.referenceId());
            } catch (Exception ex) {
                out = csv(i, custId, "FAILURE", ex.getMessage());
            }
            print(fw, out);
        }
        fw.close();
    }

    private static void print(FileWriter fw, String out) throws Exception {
        System.out.println(out);
        fw.append(out + "\n");
    }
}
