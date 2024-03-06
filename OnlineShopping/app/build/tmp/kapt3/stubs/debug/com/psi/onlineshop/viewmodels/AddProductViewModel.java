package com.psi.onlineshop.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.psi.onlineshop.data.Product;
import com.psi.onlineshop.utils.Constants;
import com.psi.onlineshop.utils.Resource;
import dagger.hilt.android.lifecycle.HiltViewModel;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u0012\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0013"}, d2 = {"Lcom/psi/onlineshop/viewmodels/AddProductViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_addNewProduct", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/psi/onlineshop/utils/Resource;", "Lcom/psi/onlineshop/data/Product;", "addNewProduct", "Lkotlinx/coroutines/flow/StateFlow;", "getAddNewProduct", "()Lkotlinx/coroutines/flow/StateFlow;", "saveProduct", "", "product", "imagesByteArrays", "", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class AddProductViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.Product>> _addNewProduct = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.Product>> addNewProduct = null;
    
    @javax.inject.Inject
    public AddProductViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.Product>> getAddNewProduct() {
        return null;
    }
    
    public final void saveProduct(@org.jetbrains.annotations.NotNull
    com.psi.onlineshop.data.Product product, @org.jetbrains.annotations.NotNull
    java.util.List<byte[]> imagesByteArrays) {
    }
}