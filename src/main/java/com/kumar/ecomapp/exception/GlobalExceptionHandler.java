package com.kumar.ecomapp.exception;

import com.kumar.ecomapp.exception.user.NoUsersFoundException;
import com.kumar.ecomapp.exception.user.UserAlreadyExistsException;
import com.kumar.ecomapp.exception.user.UserNotFoundException;
import com.kumar.ecomapp.exception.user.UserUpdateException;
import com.kumar.ecomapp.payload.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.kumar.ecomapp.exception.address.AddressLimitExceededException;
import com.kumar.ecomapp.exception.address.AddressNotFoundException;
import com.kumar.ecomapp.exception.address.AddressUpdateException;
import com.kumar.ecomapp.exception.address.NoAddressesFoundException;
import com.kumar.ecomapp.exception.category.CategoryAlreadyExistsException;
import com.kumar.ecomapp.exception.category.CategoryNotFoundException;
import com.kumar.ecomapp.exception.category.NoCategoriesFoundException;
import com.kumar.ecomapp.exception.category.CategoryUpdateException;
import com.kumar.ecomapp.exception.product.ProductAlreadyExistsException;
import com.kumar.ecomapp.exception.product.ProductNotFoundException;
import com.kumar.ecomapp.exception.product.NoProductsFoundException;
import com.kumar.ecomapp.exception.product.ProductUpdateException;
import com.kumar.ecomapp.exception.cart.CartNotFoundException;
import com.kumar.ecomapp.exception.cart.NoCartItemsFoundException;
import com.kumar.ecomapp.exception.cart.CartUpdateException;
import com.kumar.ecomapp.exception.order.NoOrdersFoundException;
import com.kumar.ecomapp.exception.order.OrderNotFoundException;
import com.kumar.ecomapp.exception.order.OrderUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleUserAlreadyExists(
            UserAlreadyExistsException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        "USR_409",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        "USR_404",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(NoUsersFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoUsersFound(
            NoUsersFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        "USR_404",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<ApiResponse<?>> handleUserUpdate(
            UserUpdateException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        "USR_409",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        message,
                        "VAL_400",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        "REQ_400",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Invalid value: " + ex.getValue(),
                        "REQ_400",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDatabaseException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        "Database constraint violated",
                        "DB_409",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        "REQ_400",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        logger.error("Unexpected Exception", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "Something went wrong",
                        "GEN_500",
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleAddressNotFound(
            AddressNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "ADDR_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(NoAddressesFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoAddressesFound(
            NoAddressesFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "ADDR_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(AddressUpdateException.class)
    public ResponseEntity<ApiResponse<?>> handleAddressUpdate(
            AddressUpdateException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "ADDR_409",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(AddressLimitExceededException.class)
    public ResponseEntity<ApiResponse<?>> handleAddressLimitExceeded(
            AddressLimitExceededException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "ADDR_400",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleCategoryAlreadyExists(
            CategoryAlreadyExistsException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "CAT_409",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleCategoryNotFound(
            CategoryNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "CAT_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(NoCategoriesFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoCategoriesFound(
            NoCategoriesFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "CAT_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(CategoryUpdateException.class)
    public ResponseEntity<ApiResponse<?>> handleCategoryUpdate(
            CategoryUpdateException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "CAT_409",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleProductAlreadyExists(
            ProductAlreadyExistsException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "PROD_409",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleProductNotFound(
            ProductNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "PROD_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(NoProductsFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoProductsFound(
            NoProductsFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "PROD_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(ProductUpdateException.class)
    public ResponseEntity<ApiResponse<?>> handleProductUpdate(
            ProductUpdateException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "PROD_409",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleCartNotFound(
            CartNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "CART_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(NoCartItemsFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoCartItemsFound(
            NoCartItemsFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "CART_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(CartUpdateException.class)
    public ResponseEntity<ApiResponse<?>> handleCartUpdate(
            CartUpdateException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "CART_409",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleOrderNotFound(
            OrderNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "ORD_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(NoOrdersFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoOrdersFound(
            NoOrdersFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "ORD_404",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(OrderUpdateException.class)
    public ResponseEntity<ApiResponse<?>> handleOrderUpdate(
            OrderUpdateException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.error(
                                ex.getMessage(),
                                "ORD_409",
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalState(
            IllegalStateException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        "USR_409",
                        request.getRequestURI()
                ));
    }


}