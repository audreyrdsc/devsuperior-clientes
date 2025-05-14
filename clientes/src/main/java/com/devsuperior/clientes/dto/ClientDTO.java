package com.devsuperior.clientes.dto;

import com.devsuperior.clientes.entities.Client;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClientDTO {

    private Long id;
    @NotEmpty(message = "Campo não pode ser vazio")
    @NotBlank(message = "Campo deve ser preenchido")
    @Size(min = 3, max = 255, message = "Nome precisa ter entre 3 e 255 caracteres")
    private String name;
    @CPF
    private String cpf;
    @PositiveOrZero(message = "A renda informada deve ser maior ou igual a zero")
    private Double income;
    @PastOrPresent(message = "A data de nascimento não pode ser no futuro")
    private LocalDate birthDate;
    @PositiveOrZero(message = "A quantidade de filhos deve ser maior ou igual a zero")
    private Integer children;

    public ClientDTO(Client entity) {
        id = entity.getId();
        name = entity.getName();
        cpf = entity.getCpf();
        income = entity.getIncome();
        birthDate = entity.getBirthDate();
        children = entity.getChildren();
    }

}
