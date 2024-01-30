package com.server.ecommerce.product.service;

import com.server.ecommerce.file.FileService;
import com.server.ecommerce.product.domain.Product;
import com.server.ecommerce.product.domain.ProductPicture;
import com.server.ecommerce.product.exception.PictureLimitExceededException;
import com.server.ecommerce.product.exception.ProductPictureNotFoundException;
import com.server.ecommerce.product.repository.ProductPictureRepository;
import com.server.ecommerce.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductPictureService {

    private final static int MAX_PICTURES_BY_PRODUCT = 3;


    private final ProductPictureRepository productPictureRepository;

    private final FileService fileService;

    private final ProductService productService;

    @Autowired
    public ProductPictureService(
            ProductPictureRepository productPictureRepository,
            FileService fileService,
            ProductService productService
    ){
        this.productPictureRepository = productPictureRepository;
        this.fileService = fileService;
        this.productService = productService;
    }

    public void savePictures(UUID productId, List<MultipartFile> multipartFiles) throws IOException {
        Product product = productService.getProductById(productId);
        Set<ProductPicture> pictures = product.getPictures();

        validateUploadFiles(pictures, multipartFiles);

        for(int i = 0; i < multipartFiles.size(); i++){
            File file = FileUtils.convertMultipartFileToFile(multipartFiles.get(i));
            savePicture(file, product, i);
        }
    }

    private void savePicture(File file, Product product, int index){
        String key = getKeyNamePicture(product.getId(), file);

        int pictureOrder = product.getPictures().size() + 1 + index;

        fileService.save(key, file);

        file.delete();

        String pictureUrl =
                String.format(
                        "https://%s/%s",
                        System.getenv("CLOUD_FRONT_DOMAIN"),
                        key);

        productPictureRepository.save(
                new ProductPicture(product, pictureUrl, pictureOrder, key)
        );
    }

    private void validateUploadFiles(Set<ProductPicture> pictures, List<MultipartFile> multipartFiles){
        if(!canUploadQuantityPictures(pictures, multipartFiles.size())) {
            throw new PictureLimitExceededException();
        }
    }

    private boolean canUploadQuantityPictures(Set<ProductPicture> pictures, int quantity){
        int currentQuantity = pictures.size();
        return currentQuantity + quantity <= MAX_PICTURES_BY_PRODUCT;
    }

    private String getKeyNamePicture(UUID productId, File file){
        return String.format("%s/%s.png", productId, file.getName());
    }

    public ProductPicture findPictureById(long id){
        return productPictureRepository
                .findById(id)
                .orElseThrow(ProductPictureNotFoundException::new);
    }

    public void deletePicture(long id){
        ProductPicture picture = findPictureById(id);
        fileService.delete(picture.getPictureKey());

        Product product = picture.getProduct();
        productPictureRepository.updateIndexPicturesByProductIdAndDeletedIndex(
                product.getId(),
                picture.getPictureOrder()
        );

        productPictureRepository.delete(picture);
    }

}
