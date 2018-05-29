package com.example.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.example.models.ProductDTO;
import com.example.models.ProductInfo;
import com.example.models.mapper.ProductMapper;
import com.example.repositories.ProductInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductInfoService {

    private final ProductInfoRepository productInfoRepository;
    private final AmazonDynamoDB amazonDynamoDB;
    private final ProductMapper productMapper;

    public ProductInfoService(ProductInfoRepository productInfoRepository, AmazonDynamoDB amazonDynamoDB, ProductMapper productMapper) {
        this.productInfoRepository = productInfoRepository;
        this.amazonDynamoDB = amazonDynamoDB;
        this.productMapper = productMapper;
    }

    public List<ProductInfo> getProductInfo() {
        return productInfoRepository.findAll();
    }

    public void createTable() throws InterruptedException {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        String TABLE_NAME = "ProductInfo";
        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(ProductInfo.class).withTableName(TABLE_NAME);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
        TableUtils.waitUntilActive(amazonDynamoDB, TABLE_NAME);
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(TABLE_NAME);
        TableDescription tableDescription = amazonDynamoDB.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
    }

    public ProductDTO saveProductInfo(ProductDTO productDTO) {
        System.out.println(productDTO);
        ProductInfo productInfo = productMapper.getProductInfo(productDTO);
        final ProductInfo info = productInfoRepository.save(productInfo);
        return productMapper.getProductDTO(info);
    }

    public ProductInfo getProductInfoById(final String id) {
        return productInfoRepository.findById(id);
    }

    public List<ProductInfo> getProductInfo(Pageable pageable, Date fromDate, Date toDate) {
        //final Page<ProductInfo> byCreatedAtBetween = productInfoRepository.findByCreatedAtLessThanEqualAndCreatedAtGreaterThanEqual(fromDate, toDate, pageable);
        //return byCreatedAtBetween.getContent();
        /*final Page<ProductInfo> all = productInfoRepository.findAll(pageable);
        return all.getContent();*/

        return productInfoRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(fromDate, toDate);
    }

    public List<ProductDTO> getAllProduct() {
        final List<ProductInfo> all = productInfoRepository.findAll();
        return productMapper.getProductDTOs(all);
    }

    public List<ProductDTO> getAllProductByCreatedDate(int page, int size) {
        /*List<ProductInfo> allProducts = productInfoRepository.findAll();
        //*Collections.sort(allProducts, Comparator.comparing(ProductInfo::getCreatedAt));
        //return allProducts;
        return allProducts.stream()
                    .distinct()
                    .sorted(Comparator.comparing(ProductInfo::getCreatedAt))
                    .collect(Collectors.toList());*/
        List<ProductDTO> productDTOS = null;
        Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "createdAt");
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MONTH, -6);
        Date lastDate = c.getTime();
        final List<ProductInfo> productInfos = productInfoRepository.findByCreatedAtBetween(lastDate, currentDate);
        if (productInfos.size() > 0) {
            productDTOS = productMapper.getProductDTOs(productInfos);
            productDTOS.sort(Comparator.comparing(ProductDTO::getCreatedAt).reversed());
        }
        /*if (productInfos.getContent().size() > 0) {
            productDTOS = productMapper.getProductDTOs(productInfos.getContent());
            productDTOS.sort(Comparator.comparing(ProductDTO::getCreatedAt).reversed());
        }*/
        return productDTOS;
    }

    public List<ProductDTO> getProductsBetweenThirtyDays() {
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, -30);
        Date lastDate = c.getTime();
        System.out.println(currentDate);
        System.out.println(lastDate);
        List<ProductDTO> productDTOS = new ArrayList<>();
        //final List<ProductInfo> productInfos = productInfoRepository.findByCreatedAtBetween(lastDate, currentDate);
        final List<ProductInfo> productInfos = productInfoRepository.findByCreatedAtGreaterThanEqual(lastDate);
        if (productInfos.size() > 0) {
            productDTOS = productMapper.getProductDTOs(productInfos);
            productDTOS.sort(Comparator.comparing(ProductDTO::getCreatedAt).reversed());
        }
        final Map<String, String> idNameMap = productInfos.stream()
                .collect(Collectors.toMap(ProductInfo::getId, ProductInfo::getMsrp));

        System.out.println(idNameMap);
        return productDTOS;
    }
}
