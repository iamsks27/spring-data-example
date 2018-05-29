package com.example.controllers;

import com.example.models.ProductDTO;
import com.example.models.ProductInfo;
import com.example.models.mapper.ProductMapper;
import com.example.services.ProductInfoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductInfoService productInfoService;
    private final ProductMapper productMapper;

    public ProductController(ProductInfoService productInfoService, ProductMapper productMapper) {
        this.productInfoService = productInfoService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public void generateTable() {
        try {
            productInfoService.createTable();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PostMapping
    public ProductDTO saveProduct(@RequestBody final ProductDTO product) {
        return productInfoService.saveProductInfo(product);
    }

    @GetMapping("/{id}")
    public ProductInfo getProductById(@PathVariable final String id) {
        return productInfoService.getProductInfoById(id);
    }


    @GetMapping("/query")
    public List<ProductInfo> getProduct(@RequestParam final int offset, @RequestParam final int size, @RequestParam final String fromDate, @RequestParam final String toDate) throws ParseException {
        Pageable pageable = new PageRequest(offset, size);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fDate = sdf.parse(fromDate);
        Date eDate = sdf.parse(toDate);
        System.out.println(fromDate + " : " + toDate);
        System.out.println(fDate + " : " + eDate);
        return productInfoService.getProductInfo(pageable, fDate, eDate);
    }

    @GetMapping("/all")
    public List<ProductDTO> getAllProduct() {
        return productInfoService.getAllProduct();
    }

    @GetMapping("/all/sorted")
    public List<ProductDTO> getAllProductsInOrderByCreatedDate(@RequestParam("page") final int page, @RequestParam("size") final int size) {
        return productInfoService.getAllProductByCreatedDate(page, size);
    }

    @GetMapping("/all/lastThirtyDays")
    public List<ProductDTO> getAllProductsBetweenThirtyDays() {
        return productInfoService.getProductsBetweenThirtyDays();
    }

}
