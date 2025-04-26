package com.advocate.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.coyote.BadRequestException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.advocate.dto.request.ClientRequestDto;
import com.advocate.dto.request.UpdateClientRequestDto;
import com.advocate.entity.Address;
import com.advocate.entity.Client;
import com.advocate.enums.Role;
import com.advocate.exception.EntityAlreadyExistsException;
import com.advocate.repository.ClientRepository;
import com.advocate.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String BASE_DIR = "resources/";

    // Add Clients
    public Client addClient(ClientRequestDto clientRequestDto, Long userId) throws EntityAlreadyExistsException {

        Client newClient = clientRepository.findByEmail(clientRequestDto.getEmail());

        if (newClient != null) {
            throw new EntityAlreadyExistsException("Client already exists with given email !");

        }

        newClient = new Client();

        if (userId != -1) {
            newClient.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Client not found with given id")));

        }

        newClient.setAddress(clientRequestDto.getAddress());
        newClient.setEmail(clientRequestDto.getEmail());
        newClient.setMobile(clientRequestDto.getMobile());
        newClient.setFirstName(clientRequestDto.getFirstName());
        newClient.setLastName(clientRequestDto.getLastName());
        newClient.setPassword(clientRequestDto.getPassword());
        System.out.println(clientRequestDto);
        String roleType = clientRequestDto.getRole();
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
                .orElseThrow(() -> new EntityNotFoundException("Client not found with given id"));

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

    // Service method to get all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Service method to get client by ID
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
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
        Client clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with the given ID."));

        if ("INACTIVE".equalsIgnoreCase(clientEntity.getStatus())) {
            throw new IllegalStateException("Case is already deleted.");
        }

        clientEntity.setStatus("INACTIVE");
        clientRepository.save(clientEntity);
    }

    // Add Profile Pic
    public Client addProfilePic(Long clientId, MultipartFile pic) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        try {
            String userDir = BASE_DIR + "clientId-" + clientId + "/";
            Files.createDirectories(Paths.get(userDir));

            String fileExtension = getImageExtension(pic.getBytes());
            if (fileExtension == null) {
                throw new BadRequestException("Invalid image format");
            }

            String filePath = userDir + "prof-pic." + fileExtension;
            Files.write(Paths.get(filePath), pic.getBytes());

            client.setProfilePicPath(filePath);
            return clientRepository.save(client);

        } catch (IOException e) {
            throw new RuntimeException("Error saving profile picture: " + e.getMessage());
        }
    }

    private String getImageExtension(byte[] fileData) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(fileData);

        return switch (mimeType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            default -> null;
        };
    }

    public Resource getProfilePic(Long clientId) {
        try {
            Path userDir = Paths.get(BASE_DIR + "clientId-" + clientId + "/");

            try (Stream<Path> files = Files.list(userDir)) {
                Optional<Path> profilePic = files
                        .filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().startsWith("prof-pic"))
                        .findFirst();

                if (profilePic.isPresent()) {
                    Resource resource = new UrlResource(profilePic.get().toUri());
                    if (resource.exists() && resource.isReadable()) {
                        return resource;
                    }
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException("Error fetching profile picture for clientId: " + clientId, e);
        }
    }

    public String getProfilePicContentType(Long clientId) {
        try {
            Path userDir = Paths.get(BASE_DIR + "clientId-" + clientId + "/");
            try (Stream<Path> files = Files.list(userDir)) {
                Optional<Path> profilePic = files
                        .filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().startsWith("prof-pic"))
                        .findFirst();

                if (profilePic.isPresent()) {
                    return Files.probeContentType(profilePic.get());
                }
            }
            return "application/octet-stream";
        } catch (IOException e) {
            throw new RuntimeException("Error determining content type for clientId: " + clientId, e);
        }
    }

    public String getProfilePicFilename(Long clientId) {
        try {
            Path userDir = Paths.get(BASE_DIR + "clientId-" + clientId + "/");
            try (Stream<Path> files = Files.list(userDir)) {
                Optional<Path> profilePic = files
                        .filter(Files::isRegularFile)
                        .filter(path -> path.getFileName().toString().startsWith("prof-pic"))
                        .findFirst();

                return profilePic.map(path -> path.getFileName().toString()).orElse("profile-pic.jpg");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error getting profile picture filename for clientId: " + clientId, e);
        }
    }

    // Update Profile Pic
    public Client updateProfilePic(Long clientId, MultipartFile pic) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (client.getProfilePicPath() == null) {
            throw new IllegalStateException("No profile picture found. Use addProfilePic to upload one first.");
        }

        try {
            Path existingPicPath = Paths.get(client.getProfilePicPath());
            String newFileExtension = getImageExtension(pic.getBytes());
            if (newFileExtension == null) {
                throw new BadRequestException("Invalid image format");
            }

            Path directory = existingPicPath.getParent();
            String newFileName = "prof-pic." + newFileExtension;
            Path newFilePath = directory.resolve(newFileName);

            if (!existingPicPath.equals(newFilePath)) {
                Files.deleteIfExists(existingPicPath);
            }

            Files.write(newFilePath, pic.getBytes());
            client.setProfilePicPath(newFilePath.toString());

            return clientRepository.save(client);
        } catch (IOException e) {
            throw new RuntimeException("Error updating profile picture: " + e.getMessage());
        }
    }

    // Delete Profile Pic
    public Client deleteProfilePic(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (client.getProfilePicPath() == null) {
            throw new IllegalStateException("No profile picture found to delete.");
        }

        try {
            Path picPath = Paths.get(client.getProfilePicPath());
            Files.deleteIfExists(picPath);
            client.setProfilePicPath(null);

            return clientRepository.save(client);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting profile picture: " + e.getMessage());
        }
    }

}
