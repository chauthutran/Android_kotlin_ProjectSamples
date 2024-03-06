package com.psi.onlineshop.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.psi.onlineshop.ShoppingApplication;
import com.psi.onlineshop.data.User;
import com.psi.onlineshop.utils.Constants;
import com.psi.onlineshop.utils.Resource;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000b\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\nJ\u000e\u0010\u000f\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\nR\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u0014"}, d2 = {"Lcom/psi/onlineshop/viewmodels/LoginViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_login", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/psi/onlineshop/utils/Resource;", "Lcom/psi/onlineshop/data/User;", "_resetPassword", "", "login", "Lkotlinx/coroutines/flow/SharedFlow;", "getLogin", "()Lkotlinx/coroutines/flow/SharedFlow;", "resetPassword", "getResetPassword", "", "email", "password", "app_debug"})
public final class LoginViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.User>> _login = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.User>> login = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.psi.onlineshop.utils.Resource<java.lang.String>> _resetPassword = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<com.psi.onlineshop.utils.Resource<java.lang.String>> resetPassword = null;
    
    public LoginViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.User>> getLogin() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<com.psi.onlineshop.utils.Resource<java.lang.String>> getResetPassword() {
        return null;
    }
    
    public final void login(@org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password) {
    }
    
    public final void resetPassword(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
    }
}