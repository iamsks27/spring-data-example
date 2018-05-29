package com.example.repositories;

import com.example.models.ProductId;
import com.example.models.ProductInfo;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

@EnableScan
public interface ProductInfoRepository extends PagingAndSortingRepository<ProductInfo, ProductId> {

    List<ProductInfo> findAll();

    Page<ProductInfo> findByCreatedAtBetween(Date fromDate, Date toDate, Pageable pageable);

    Page<ProductInfo> findByCreatedAtLessThanEqualAndCreatedAtGreaterThanEqual(Date fromDate, Date toDate, Pageable pageable);

    List<ProductInfo> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(Date fromDate, Date toDate);

    ProductInfo findById(String id);

    Page<ProductInfo> findAll(Pageable pageable);

    List<ProductInfo> findByCreatedAtBetween(Date lastDate, Date currentDate);

    List<ProductInfo> findByCreatedAtGreaterThanEqual(Date lastDate);

    //List<ProductInfo> findAllByOrderByCreatedAtAsc();
}
