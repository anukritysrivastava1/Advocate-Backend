package com.advocate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;

import com.advocate.dto.request.ClientRequestDto;
import com.advocate.dto.request.UpdateClientRequestDto;
import com.advocate.dto.response.CommonResponseDto;
import com.advocate.entity.Client;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // add
    @PostMapping("/")
    public ResponseEntity<CommonResponseDto<Client>> addClient(@RequestBody ClientRequestDto clientRequestDto, @RequestParam Long userId)
            throws EntityAlreadyExistsException {
        
        var client = clientService.addClient(clientRequestDto, userId);

        return ResponseEntity.ok(new CommonResponseDto<>("Users added successfully ", HttpStatus.OK, client));

    }

    @PutMapping("/")
    public ResponseEntity<CommonResponseDto<Client>> updateClient(
            @RequestBody UpdateClientRequestDto updateClientRequestDto) {
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
    public ResponseEntity<CommonResponseDto<Void>> deleteCaseById(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity.ok(new CommonResponseDto<>("Client deleted successfully.", HttpStatus.OK, null));
    }

    @PostMapping(value = "/{clientId}/addProfilePic", consumes = "multipart/form-data")
    @Operation(
        summary = "Upload profile picture",
        description = "Uploads a profile picture for the given client ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Profile picture uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Server error")
        }
    )
    public ResponseEntity<CommonResponseDto<Client>> addProfilePic(
            @Parameter(description = "Profile picture file", required = true)
            @RequestParam("file") MultipartFile file,
            @PathVariable Long clientId) {
        Client client = clientService.addProfilePic(clientId, file);
        return ResponseEntity.ok(new CommonResponseDto<>("Profile pic added successfully", HttpStatus.OK, client));
    }
    

    @GetMapping("/{clientId}/profile-picture")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable Long clientId) {
        Resource profilePic = clientService.getProfilePic(clientId);

        if (profilePic == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = clientService.getProfilePicContentType(clientId);
        String filename = clientService.getProfilePicFilename(clientId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .body(profilePic);
    }

    @PutMapping("/{clientId}/updateProfilePic")
    public ResponseEntity<CommonResponseDto<Client>> updateProfilePic(@RequestParam("file") MultipartFile file,
            @PathVariable Long clientId) {
        try {
            Client client = clientService.updateProfilePic(clientId, file);
            return ResponseEntity
                    .ok(new CommonResponseDto<>("Profile pic updated successfully.", HttpStatus.OK, client));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponseDto<>("Error updating profile picture", HttpStatus.INTERNAL_SERVER_ERROR,
                            null));
        }
    }

    @DeleteMapping("/{clientId}/deleteProfilePic")
    public ResponseEntity<CommonResponseDto<Client>> deleteProfilePic(@PathVariable Long clientId) {
        Client client = clientService.deleteProfilePic(clientId);
        return ResponseEntity.ok(new CommonResponseDto<>("Profile pic deleted successfully.", HttpStatus.OK, client));
    }

}
