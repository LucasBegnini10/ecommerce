package com.server.ecommerce.product.controller;

import com.server.ecommerce.infra.RestResponseHandler;
import com.server.ecommerce.product.service.ProductPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products/picture")
public class ProductPictureController {

    private final ProductPictureService productPictureService;

    @Autowired
    public ProductPictureController(ProductPictureService productPictureService) {
        this.productPictureService = productPictureService;
    }

    @PostMapping("{id}")
    public ResponseEntity<Object> uploadPicture(@PathVariable UUID id, @RequestBody List<MultipartFile> files) {
        try {
            productPictureService.savePictures(id, files);

            return RestResponseHandler.generateResponse(
                    "Products uploaded!",
                    HttpStatus.CREATED,
                    null
            );
        } catch (
                IOException ex) {
            return RestResponseHandler.generateResponse(
                    "Error to save profile picture",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ex.getMessage()
            );
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletePicture(@PathVariable long id){
        productPictureService.deletePicture(id);
        return RestResponseHandler.generateResponse(
                "Picture deleted!",
                HttpStatus.OK,
                null
        );
    }

}
