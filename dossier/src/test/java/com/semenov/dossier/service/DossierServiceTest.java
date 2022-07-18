package com.semenov.dossier.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semenov.dossier.client.DealClient;
import com.semenov.dossier.dto.EmailMessageDTO;
import com.semenov.dossier.dto.LoanOfferDTO;
import com.semenov.dossier.entity.Application;
import com.semenov.dossier.entity.Client;
import com.semenov.dossier.entity.Credit;
import com.semenov.dossier.model.AdditionalServices;
import com.semenov.dossier.model.CreditStatus;
import com.semenov.dossier.model.Employment;
import com.semenov.dossier.model.Gender;
import com.semenov.dossier.model.MaritalStatus;
import com.semenov.dossier.model.Passport;
import com.semenov.dossier.model.Status;
import com.semenov.dossier.model.Theme;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DossierServiceTest {

    @Mock
    private DocumentService documentService;

    @Mock
    private MailService mailService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DealClient dealClient;

    @InjectMocks
    private DossierService dossierService;


    @Test
    public void finishRegistrationMessage() throws JsonProcessingException {
        String text = "Dear ivan, finish registration your application id-1, please.";
        String mail = "email";

        Client client = Client.builder()
                .id(1L)
                .firstName("ivan")
                .middleName("ivanic")
                .lastName("ivanov")
                .email("zasds@sdaf.ru")
                .application(new Application())
                .passport(new Passport())
                .birthDate(LocalDate.of(2000, 2, 20))
                .account("Sdfs")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(new Employment())
                .build();

        Credit credit = Credit.builder()
                .psk(BigDecimal.valueOf(100500))
                .rate(BigDecimal.valueOf(15))
                .monthlyPayment(BigDecimal.valueOf(100500))
                .amount(BigDecimal.valueOf(100200))
                .paymentSchedule(new ArrayList<>())
                .term(60)
                .creditStatus(CreditStatus.CALCULATED)
                .additionalServices(new AdditionalServices())
                .id(1L)
                .build();

        Application application = Application.builder()
                .credit(credit)
                .id(1L)
                .client(client)
                .creationDate(LocalDate.now())
                .status(Status.APPROVED)
                .statusHistory(new ArrayList<>())
                .appliedOffer(new LoanOfferDTO())
                .build();

        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .applicationId(1L)
                .address("email")
                .theme(Theme.SEND_DOCUMENTS)
                .build();

        when(objectMapper.readValue(text, EmailMessageDTO.class)).thenReturn(emailMessageDTO);
        when((dealClient.getApplicationById(emailMessageDTO.getApplicationId()))).thenReturn(application);

        dossierService.finishRegistrationMessage(text);

        verify(mailService, times(1)).sendMessage(
                argThat(email -> email.equals(mail)),
                argThat(data -> data.equals(text))
        );
    }

    @Test
    public void sendDocumentRequest() {
    }

    @Test
    public void sendDocument() {
    }

    @Test
    public void getSesCode() {
    }

    @Test
    public void signDocument() {
    }
}