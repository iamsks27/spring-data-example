package com.example.models.mapper;

import com.example.models.ProductDTO;
import com.example.models.ProductInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductInfo getProductInfo(ProductDTO product) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCost(product.getCost());
        productInfo.setCreatedAt(product.getCreatedAt());
        productInfo.setMsrp(product.getMsrp());
        productInfo.setCount(product.getCount());
        return productInfo;
    }

    public ProductDTO getProductDTO(ProductInfo info) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCost(info.getCost());
        productDTO.setCreatedAt(info.getCreatedAt());
        productDTO.setId(info.getId());
        productDTO.setMsrp(info.getMsrp());
        productDTO.setCount(info.getCount());
        return productDTO;
    }


    public List<ProductDTO> getProductDTOs(List<ProductInfo> productInfos) {
        return productInfos.stream()
                    .map(this::getProductDTO)
                    .collect(Collectors.toList());
    }
}

