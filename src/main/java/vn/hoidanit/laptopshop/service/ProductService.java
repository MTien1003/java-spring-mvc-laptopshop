package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    public ProductService(ProductRepository productRepository,
                          CartRepository cartRepository,
                          CartDetailRepository cartDetailRepository,
                          UserService userService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService= userService;
    }
    public Product saveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public Product getProductById(long id) {
        return this.productRepository.findById(id);
    }

    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public void handleAddProductToCart(String email, long productId, HttpSession session) {
        User user = this.userService.getUserByEmail(email);
        if(user != null){
            Cart cart= this.cartRepository.findByUser(user);
            if(cart==null){
                Cart otherCart= new Cart();
                otherCart.setUser(user);
                otherCart.setSum(0);
                cart= this.cartRepository.save(otherCart);
            }

            Product product= this.productRepository.findById(productId);
            if(product == null) {
                throw new IllegalArgumentException("Product not found with id: " + productId);
            }
            CartDetail oldDetail= this.cartDetailRepository.findByCartAndProduct(cart, product);
            if(oldDetail == null){
                CartDetail cd=new CartDetail();
                cd.setCart(cart);
                cd.setProduct(product);
                cd.setPrice(product.getPrice());
                cd.setQuantity(1);
                this.cartDetailRepository.save(cd);

                // Update the cart sum
                int s= cart.getSum()+1;
                cart.setSum(s);
                this.cartRepository.save(cart);
                session.setAttribute("sum",s);
            }
            else{
                oldDetail.setQuantity(oldDetail.getQuantity() + 1);
                this.cartDetailRepository.save(oldDetail);
            }
            

        }

    }

    public Cart fetchByUser(User user){
        return this.cartRepository.findByUser(user);
    }

}
