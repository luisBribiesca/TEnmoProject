package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    public static final String API_BASE_URL = "http://localhost:8080/";

    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }




    public boolean postTransferRequest(Transfer newTransfer){

        HttpEntity<Object> entity = makeEntity(newTransfer);

        boolean transferPost = false;
        try {
            transferPost = restTemplate.postForObject(API_BASE_URL + "transfers", entity, boolean.class);
        } catch (RestClientResponseException ex){
            BasicLogger.log(ex.getRawStatusCode() + " : " + ex.getStatusText());
        } catch (ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }

        return transferPost;


    }

    public void updateBalance(Account account){

        HttpEntity<Object> entity = makeEntity(account);

        try{
            restTemplate.put(API_BASE_URL + "accounts/" + account.getAccountId(), entity);

        } catch (RestClientResponseException ex){
        BasicLogger.log(ex.getRawStatusCode() + " : " + ex.getStatusText());
        } catch (ResourceAccessException ex){
        BasicLogger.log(ex.getMessage());
         }

    }

    public void updateTransfer(Transfer transfer){

        HttpEntity<Object> entity = makeEntity(transfer);

        try{
            restTemplate.put(API_BASE_URL + "transfers/" + transfer.getTransferId(), entity);

        } catch (RestClientResponseException ex){
            BasicLogger.log(ex.getRawStatusCode() + " : " + ex.getStatusText());
        } catch (ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }

    }

    public void processTransfer(Account receiving, Account sending, BigDecimal amount, boolean toTransfer){

        if(toTransfer) {
            BigDecimal sendingBalanceUpdate = sending.getBalance().subtract(amount);
            BigDecimal receiveBalanceUpdate = receiving.getBalance().add(amount);
            receiving.setBalance(receiveBalanceUpdate);
            sending.setBalance(sendingBalanceUpdate);
            updateBalance(receiving);
            updateBalance(sending);
        } else {
            System.out.println("\nYou cannot accept the request for" +
                    " money if you are the person who sent the request");
        }


    }

//    public TransferStatus postTransferStatus(String status){
//        TransferStatus newStatus = new TransferStatus(status);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(authToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<TransferStatus> entity = new HttpEntity<>(newStatus, headers);
//
//        TransferStatus postStatus = restTemplate.postForObject(API_BASE_URL, entity, TransferStatus.class);
//
//        return postStatus;
//    }
//
//    public TransferType postTransferType(String type){
//        TransferType newType = new TransferType(type);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(authToken);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<TransferType> entity = new HttpEntity<>(newType, headers);
//
//        TransferType postType = restTemplate.postForObject(API_BASE_URL, entity, TransferType.class);
//
//        return postType;
//
//    }


    private HttpEntity<Object> makeEntity(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(object, headers);
    }


}
