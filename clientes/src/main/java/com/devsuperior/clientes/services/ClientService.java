package com.devsuperior.clientes.services;

import com.devsuperior.clientes.dto.ClientDTO;
import com.devsuperior.clientes.entities.Client;
import com.devsuperior.clientes.repositories.ClientRepository;
import com.devsuperior.clientes.services.exceptions.DatabaseException;
import com.devsuperior.clientes.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    //Busca paginada de recursos - lista inteira de clientes
    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        Page<Client> result = clientRepository.findAll(pageable);
        return result.map(x -> new ClientDTO(x)); //Remetido ao construtor de conversão Entidade/DTO
    }

    //Busca de recurso por id - cliente específico
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Optional<Client> result = clientRepository.findById(id);
        Client client = result.orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));
        ClientDTO dto = new ClientDTO(client);
        return dto;
    }

    //Inserção de novo cliente - novo id
    @Transactional
    public ClientDTO insert(ClientDTO dto) {
        Client entity = new Client();
        copyDtoToEntity(dto, entity);
        entity = clientRepository.save(entity);
        return new ClientDTO(entity);
    }

    //Atualização de dados do cliente
    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {
        try {
            Client entity = clientRepository.getReferenceById(id); //Instanciar um novo cliente pela referência do Id
            copyDtoToEntity(dto, entity);
            entity = clientRepository.save(entity);
            return new ClientDTO(entity); //reconverter para DTO
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
    }

    //Apagar um cliente pelo id
    @Transactional//(propagation = Propagation.SUPPORTS) //
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            clientRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    //Metodo para converter o DTO em Entidade - O construtor de ClientDTO já faz o inverso
    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }



}
