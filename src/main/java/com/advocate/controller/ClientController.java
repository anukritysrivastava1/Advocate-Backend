package com.advocate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advocate.dto.request.ClientRequestDto;
import com.advocate.dto.request.UpdateClientRequestDto;
import com.advocate.dto.response.CommonResponseDto;
import com.advocate.entity.Client;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // add
    @PostMapping("/")
    public ResponseEntity<CommonResponseDto<Client>> addClient(@RequestBody ClientRequestDto clientRequestDto)
            throws EntityAlreadyExistsException {
        System.out.println(clientRequestDto);
        System.out.println("before service");
        var client = clientService.addClient(clientRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("Users added successfully ", HttpStatus.OK, client));

    }

    @PutMapping("/")
    public ResponseEntity<CommonResponseDto<Client>> updateClient(@RequestBody UpdateClientRequestDto updateClientRequestDto) {
        Client updatedClient = clientService.updateClient(updateClientRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("Client updated successfully ", HttpStatus.OK, updatedClient));

    }

    @GetMapping("/status")
    public ResponseEntity<CommonResponseDto<List<Client>>> getAllClientsByStatus(@RequestParam String status) {
        List<Client> clients = clientService.getAllClientsByStatus(status);
        if (clients.isEmpty()) {
            return ResponseEntity
                    .ok(new CommonResponseDto<>("No user found with status: " + status, HttpStatus.OK, clients));
        }
        return ResponseEntity.ok(new CommonResponseDto<>("Users found with status: " + status, HttpStatus.OK, clients));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto<Client>> deleteClientById(@PathVariable Long id){

        clientService.deleteClientById(id);
        return ResponseEntity.ok(new CommonResponseDto<>("Users deleted successfully. ",  HttpStatus.OK, null));

    }
}
