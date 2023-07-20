package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.service.BrandService;
import com.team3.ecommerce.service.CategoryService;
import com.team3.ecommerce.service.ProductService;
import com.team3.ecommerce.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopService shopService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BrandService brandService;

    // thêm sản phẩm
    @PostMapping("/add")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    // hiển thị tất cả sản phẩm
    @GetMapping("/all")
    public ResponseEntity<Iterable<Product>> allProduct() {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

     // hiển thị chi tiết sản phẩm
    @GetMapping("/detail/{id}")
    public ResponseEntity<Product> detailProduct(@PathVariable Integer id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }


    // Hiển thị danh sách sản phẩm trong 1 shop theo các tiêu chí: tìm theo tên, tìm theo category, tìm theo brand
    @GetMapping("/shop/{idShop}")
    public ResponseEntity<?> findProductByShop(@PathVariable Integer idShop,
                                                               @RequestParam(value = "keyword", required = false) String keyword,
                                                               @RequestParam(value = "idCategory", required = false) Integer idCategory,
                                                               @RequestParam(value = "idBrand", required = false) Integer idBrand,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "1") int size) {

        Optional<Shop> shopOptional = shopService.findByIdShop(idShop);
        if (!shopOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Shop shop = shopOptional.get();
        Category category = null;
        Brand brand = null;

        if (idCategory != null) {
            Optional<Category> categoryOptional = categoryService.findCategoryById(idCategory);
            if (!categoryOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            category = categoryOptional.get();
        }

        if (idBrand != null) {
            Optional<Brand> brandOptional = brandService.findBrandById(idBrand);
            if (!brandOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            brand = brandOptional.get();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products;

        if (keyword != null && !keyword.isEmpty()) {
            products = productService.findByShop(shop, keyword, category, brand, pageable);
        } else {
            products = productService.findByShop(shop, null, category, brand, pageable);
        }

        if (products.isEmpty()) {
            return new ResponseEntity<>("Không tìm thấy sản phẩm ... vui lòng cập nhật giỏ hàng", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    // ngừng bán sản phẩm của 1 shop
    @PutMapping("/{productId}/stop-product-shop/{shopId}")
    public ResponseEntity<?> updateInStock(
            @PathVariable Integer productId,
            @PathVariable Integer shopId
    ) {
        // Kiểm tra xem đối tượng Shop có tồn tại hay không
        Optional<Shop> shop1 = shopService.findByIdShop(shopId);
        if (!shop1.isPresent()) {
            // Nếu không tồn tại, trả về mã trạng thái 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Kiểm tra xem các tham số truyền vào có đầy đủ hay không
        if (productId == null || shopId == null) {
            // Nếu thiếu tham số, trả về mã trạng thái 400 Bad Request
            return ResponseEntity.badRequest().build();
        }

        // Tìm kiếm sản phẩm trong cửa hàng
        Optional<Product> product = productService.findProductsInShopByIdProducts(productId, shop1.get());
        if (!product.isPresent()) {
            // Nếu sản phẩm không tồn tại, trả về mã trạng thái 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Cập nhật trạng thái sản phẩm và lưu vào cơ sở dữ liệu
        product.get().setInStock(false);
        productService.save(product.get());

        // Trả về mã trạng thái 200 OK
        return ResponseEntity.ok().build();
    }



}
