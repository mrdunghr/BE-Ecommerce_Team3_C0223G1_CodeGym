package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.ok(createdProduct);
        } catch (IllegalArgumentException ex) {
            // Xử lý lỗi nếu sản phẩm có cùng tên đã tồn tại
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            // Xử lý lỗi nếu có lỗi xảy ra trong quá trình tạo sản phẩm
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product");
        }
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

    // Sửa sản phẩm
    @PutMapping("/edit/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product products) {
        if (this.productService.findById(id).isPresent()) {
            products.setId(id);
            this.productService.editProduct(products);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
    @PutMapping("/{productId}/stop-product-shop")
    public ResponseEntity<?> closeInStock(
            @PathVariable Integer productId
    ) {
        Optional<Product> product = productService.findById(productId);
        if (!product.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        // Cập nhật trạng thái sản phẩm và lưu vào cơ sở dữ liệu
        product.get().setInStock(false);
        product.get().setEnabled(false);
        productService.editProduct(product.get());
        return ResponseEntity.ok().build();
    }

    // mở bán sản phẩm
    @PutMapping("/{productId}/open-product-shop")
    public ResponseEntity<?> openInStock(
            @PathVariable Integer productId
    ) {
        Optional<Product> product = productService.findById(productId);
        if (!product.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        // Cập nhật trạng thái sản phẩm và lưu vào cơ sở dữ liệu
        product.get().setInStock(true);
        product.get().setEnabled(true);
        productService.editProduct(product.get());
        return ResponseEntity.ok().build();
    }


    // hiển thị 5 sản phẩm bán chạy nhất
    @GetMapping("/list-product-discount")
    public ResponseEntity<Iterable<Product>> listProduct() {
        Iterable<Product> products = productService.findTop6ByOrderByDiscountPercent();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // hiển thị 10 sản phẩm bán chạy nhất
    @GetMapping("/list-product-discount-sale")
    public ResponseEntity<Iterable<Product>> listProductSale() {
        Iterable<Product> products = productService.findTop10ByOrderByDiscountPercent();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // hiển thị tất cả sản phẩm của customer
    @GetMapping("/customer-list/{customerId}")
    public Page<Product> getAllProductsByCustomerId(@PathVariable Integer customerId,
                                                    @RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "4") Integer size) {
        return productService.getAllProductsByCustomerId(customerId, page, size);
    }

    // hiển thị tất cả ảnh của product
    @GetMapping("/{productId}/all-images")
    public Map<String, Object> getProductWithImages(@PathVariable Integer productId) {
        return productService.getProductWithImages(productId);
    }

    // sửa ảnh product
    @PutMapping("/{productId}/update-images")
    public ResponseEntity<Product> updateProductImages(@PathVariable Integer productId,
                                                       @RequestBody Map<String, Object> imageInfo) {
        Product product = productService.updateProductImages(productId, imageInfo);
        return ResponseEntity.ok().body(product);
    }

    // hiển thị product theo category
    @GetMapping("/show-by-category/{category_id}")
    public ResponseEntity<?> showProductByCategory(@PathVariable Integer category_id,
                                                   @RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestParam(defaultValue = "false") boolean list) {

        Category category = categoryService.findCategoryById(category_id).get();

        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        boolean categoryExists = categoryService.checkCategoryExists(category_id);
        if (!categoryExists) {
            return ResponseEntity.notFound().build();
        }

        if (list) {
            List<Product> products = productService.getAllProductByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(products);
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> products = productService.getProductsByCategory(category, pageable);
            if (products.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(products);
        }
    }

    // tìm kiếm product theo tên
    @GetMapping("/search")
    public ResponseEntity<Iterable<Product>> findByNameProduct(@RequestParam("name") String name) {
        Iterable<Product> listProducts = productService.findByNameProduct(name);
        if(!listProducts.iterator().hasNext()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(listProducts,HttpStatus.OK);
    }

    // 3 sản phẩm mới nhất
    @GetMapping("/latest")
    public ResponseEntity<Iterable<Product>> findTop3ByOrderByIdDesc(){
        Iterable<Product> listProducts = productService.findTop3ByOrderByIdDesc();
        if(!listProducts.iterator().hasNext()){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(listProducts,HttpStatus.OK);
    }

    // 3 sản phẩm mới nhất của 1 danh mục
    @GetMapping("/latest/category/{categoryId}")
    public ResponseEntity<?> findTop3ByCategory(@PathVariable Integer categoryId){
        Optional<Category> category1 = categoryService.findCategoryById(categoryId);
        Iterable<Product> listProducts = productService.getLatestProductsByCategory(category1.get());
        return new ResponseEntity<>(listProducts,HttpStatus.OK);
    }

}
