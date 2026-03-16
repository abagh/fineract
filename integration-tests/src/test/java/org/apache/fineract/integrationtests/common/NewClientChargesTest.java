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
import org.junit.jupiter.api.Nested;

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
    @DisplayName("A client charge can be created")
    public void guid_62865C8558974FF2AADD608C3B0400BF_Test() {
        final Integer chargeId = ChargesHelper.createCharges(this.requestSpec, this.responseSpec,
                ChargesHelper.getChargeSpecifiedDueDateJSON());
        Assertions.assertNotNull(chargeId);
    }

    @Test
    @DisplayName("A client with activation date can be created")
    public void guid_E4661D4032CD4153B5297CB76BCA7AD0_Test() {
        final Integer clientId = ClientHelper.createClient(this.requestSpec, this.responseSpec, "01 October 2011");
        Assertions.assertNotNull(clientId);
    }

    @Test
    @DisplayName("A charge for a load can be created")
    public void guid_C24A6B0516B54550AD6023E7EFE81225_Test() {
        final Integer loanChargeId = ChargesHelper.createCharges(this.requestSpec, this.responseSpec,
                ChargesHelper.getLoanSpecifiedDueDateJSON());
        Assertions.assertNotNull(loanChargeId)
    }
        
    @Nested
    public class GUID_EC59659813FA44E98E198972C305FC28 {

        Integer clientId;
        Integer loanChargeId;
        
        @BeforeEach
        public void setup() {
            clientId = ClientHelper.createClient(this.requestSpec, this.responseSpec, "01 October 2011");
            loanChargeId = ChargesHelper.createCharges(this.requestSpec, this.responseSpec,
                ChargesHelper.getLoanSpecifiedDueDateJSON());
        }

        @Test
        @DisplayName("Given a client\n"
                     "Given a charge for a loan\n" +
                     "The charge can be associated with the client")
        public void guid_14C97BB9A60A413589E757C53B34D0F3_Test() {
            final Integer clientLoanChargeId = ClientHelper.addChargesForClient(this.requestSpec, responseLoanChargeFailure, clientId,
                ClientHelper.getSpecifiedDueDateChargesClientAsJSON(loanChargeId.toString(), "29 October 2011"));
            Assertions.assertNull(clientLoanChargeId);
        }
    }
}
