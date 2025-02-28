package com.advocate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.advocate.dto.SignupRequest;
import com.advocate.dto.UpdateClientRequestDto;
import com.advocate.entity.Address;
import com.advocate.entity.Client;
import com.advocate.enums.Role;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.repository.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Add Clients
    public Client addClient(SignupRequest signupRequest) throws EntityAlreadyExistsException {
        Client newClient = clientRepository.findByEmail(signupRequest.getEmail());

        if (newClient != null) {
            throw new EntityAlreadyExistsException("User already exists with given email !");

        }

        newClient = new Client();
        newClient.setAddress(signupRequest.getAddress());
        newClient.setEmail(signupRequest.getEmail());
        newClient.setMobile(signupRequest.getMobile());
        newClient.setFirstName(signupRequest.getFirstName());
        newClient.setLastName(signupRequest.getLastName());
        newClient.setPassword(signupRequest.getPassword());
        System.out.println(signupRequest);
        String roleType = signupRequest.getRole();
        System.out.println("Role is " + roleType);
        if (roleType == null || roleType.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: Role cannot be empty!");
        }

        try {
            Role role = Role.valueOf(roleType.trim().toUpperCase());
            System.out.println(role);
            newClient.setRole(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Error: Invalid role '" + roleType + "'. Allowed values: ADMIN, SUBADMIN, USER.");
        }

        System.out.println(newClient);

        return clientRepository.save(newClient);
    }

    // Update Clients
    public Client updateClient(UpdateClientRequestDto updateClientRequestDto) {
        Client client = clientRepository.findById(updateClientRequestDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with given id"));

        client.setAddress(
                (Address) checkAndUpdateValueIfPresent(client.getAddress(), updateClientRequestDto.getAddress()));
        client.setEmail((String) checkAndUpdateValueIfPresent(client.getEmail(), updateClientRequestDto.getEmail()));
        client.setMobile((String) checkAndUpdateValueIfPresent(client.getMobile(), updateClientRequestDto.getMobile()));
        client.setFirstName(
                (String) checkAndUpdateValueIfPresent(client.getFirstName(), updateClientRequestDto.getFirstName()));
        client.setLastName(
                (String) checkAndUpdateValueIfPresent(client.getLastName(), updateClientRequestDto.getLastName()));
        client.setPassword(
                (String) checkAndUpdateValueIfPresent(client.getPassword(), updateClientRequestDto.getPassword()));

        return clientRepository.save(client);

    }

    private Object checkAndUpdateValueIfPresent(Object object1, Object object2) {
        return (object2 == null || object2.toString().equals("")) ? object1 : object2;
    }

    // Status of Client
    public List<Client> getAllClientsByStatus(String status) {
        status = status.trim().toUpperCase();
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Error: Status cannot be empty!");
        } else if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
            throw new IllegalArgumentException(
                    "Error: Invalid status '" + status + "'. Allowed values: ACTIVE, INACTIVE.");
        }
        return clientRepository.findByStatus(status);
    }

    // Delete Clients
    public void deleteClientById(Long id) {
        clientRepository.findById(id)
                .ifPresentOrElse(clientRepository::delete,
                        () -> {
                            throw new EntityNotFoundException("User not found with given id");
                        });

    }

}
