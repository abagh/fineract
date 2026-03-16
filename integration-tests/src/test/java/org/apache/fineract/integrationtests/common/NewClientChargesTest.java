package org.apache.fineract.integrationtests.common;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.fineract.integrationtests.common.charges.ChargesHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NewClientChargesTest {

    private ResponseSpecification responseSpec;
    private RequestSpecification requestSpec;

    @BeforeEach
    public void setup() {
        Utils.initializeRESTAssured();
        this.requestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        this.requestSpec.header("Authorization", "Basic " + Utils.loginIntoServerAndGetBase64EncodedAuthenticationKey());
        this.responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
    }

    @Test
    public void clientChargeTest() {

        // Creates clientCharge
        final Integer chargeId = ChargesHelper.createCharges(this.requestSpec, this.responseSpec,
                ChargesHelper.getChargeSpecifiedDueDateJSON());
        Assertions.assertNotNull(chargeId);

        // creates client with activation date
        final Integer clientId = ClientHelper.createClient(this.requestSpec, this.responseSpec, "01 October 2011");
        Assertions.assertNotNull(clientId);

        /**
         * create a charge for loan and try to associate to client created in the above lines.it will be an invalid
         * scenario the reason is client is not allowed to have only client charge.
         *
         */
        final Integer loanChargeId = ChargesHelper.createCharges(this.requestSpec, this.responseSpec,
                ChargesHelper.getLoanSpecifiedDueDateJSON());
        Assertions.assertNotNull(loanChargeId);
        ResponseSpecification responseLoanChargeFailure = new ResponseSpecBuilder().expectStatusCode(403).build();
        final Integer clientLoanChargeId = ClientHelper.addChargesForClient(this.requestSpec, responseLoanChargeFailure, clientId,
                ClientHelper.getSpecifiedDueDateChargesClientAsJSON(loanChargeId.toString(), "29 October 2011"));
        Assertions.assertNull(clientLoanChargeId);

        /**
         * associates a clientCharge to a client and pay client charge for 10 USD--success scenario
         **/
        final Integer clientChargeId = ClientHelper.addChargesForClient(this.requestSpec, this.responseSpec, clientId,
                ClientHelper.getSpecifiedDueDateChargesClientAsJSON(chargeId.toString(), "29 October 2011"));
        Assertions.assertNotNull(clientChargeId);
        final String clientChargePaidTransactionId = ClientHelper.payChargesForClients(this.requestSpec, this.responseSpec, clientId,
                clientChargeId, ClientHelper.getPayChargeJSON("25 AUGUST 2015", "10"));
        Assertions.assertNotNull(clientChargePaidTransactionId);
    }
}
