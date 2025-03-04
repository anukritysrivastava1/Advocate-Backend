package com.advocate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.CommonResponseDto;
import com.advocate.dto.SignupRequest;
import com.advocate.dto.UpdateClientRequestDto;
import com.advocate.entity.Client;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.ClientService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // add
    @PostMapping("/add")
    public ResponseEntity<CommonResponseDto<Client>> addClient(@RequestBody SignupRequest signupRequest)
            throws EntityAlreadyExistsException {
        System.out.println(signupRequest);
        System.out.println("before service");
        var client = clientService.addClient(signupRequest);

        return ResponseEntity.ok(new CommonResponseDto<>("Users added successfully ", HttpStatus.OK, client));

    }

    @PatchMapping("/update")
    public ResponseEntity<CommonResponseDto<Client>> updateClient(@RequestBody UpdateClientRequestDto updateClientRequestDto) {
        Client updatedClient = clientService.updateClient(updateClientRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("Users updated successfully ", HttpStatus.OK, updatedClient));

    }

    @GetMapping("/status")
    public ResponseEntity<CommonResponseDto<List<Client>>> getClientsByStatus(@RequestParam String status) {
        List<Client> clients = clientService.getAllClientsByStatus(status);
        if (clients.isEmpty()) {
            return ResponseEntity
                    .ok(new CommonResponseDto<>("No user found with status: " + status, HttpStatus.OK, clients));
        }
        return ResponseEntity.ok(new CommonResponseDto<>("Users found with status: " + status, HttpStatus.OK, clients));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponseDto<Client>> deleteClientById(@PathVariable Long id){

        clientService.deleteClientById(id);
        return ResponseEntity.ok(new CommonResponseDto<>("Users deleted successfully. ",  HttpStatus.OK, null));

    }
}
