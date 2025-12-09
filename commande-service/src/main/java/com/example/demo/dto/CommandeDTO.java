package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class CommandeDTO {
    private Long idClient;
    private List<ProductItemDTO> productItems;
}
