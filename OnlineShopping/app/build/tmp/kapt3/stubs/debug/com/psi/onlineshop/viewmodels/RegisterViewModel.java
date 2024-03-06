package com.psi.onlineshop.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.psi.onlineshop.ShoppingApplication;
import com.psi.onlineshop.data.User;
import com.psi.onlineshop.utils.Constants;
import com.psi.onlineshop.utils.Resource;
import com.psi.onlineshop.utils.UserRegisterFieldsState;
import com.psi.onlineshop.utils.UserRegisterValidation;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\u0016\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u0018R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R&\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000f\u00a8\u0006\u001b"}, d2 = {"Lcom/psi/onlineshop/viewmodels/RegisterViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_register", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/psi/onlineshop/utils/Resource;", "Lcom/psi/onlineshop/data/User;", "_validation", "Lkotlinx/coroutines/channels/Channel;", "Lcom/psi/onlineshop/utils/UserRegisterFieldsState;", "register", "Lkotlinx/coroutines/flow/Flow;", "getRegister", "()Lkotlinx/coroutines/flow/Flow;", "setRegister", "(Lkotlinx/coroutines/flow/Flow;)V", "validation", "getValidation", "checkValidation", "", "user", "password", "", "createAcccountWithEmailAndPassword", "", "app_debug"})
public final class RegisterViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull
    private kotlinx.coroutines.flow.MutableStateFlow<com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.User>> _register;
    @org.jetbrains.annotations.NotNull
    private kotlinx.coroutines.flow.Flow<? extends com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.User>> register;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.channels.Channel<com.psi.onlineshop.utils.UserRegisterFieldsState> _validation = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<com.psi.onlineshop.utils.UserRegisterFieldsState> validation = null;
    
    public RegisterViewModel(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.User>> getRegister() {
        return null;
    }
    
    public final void setRegister(@org.jetbrains.annotations.NotNull
    kotlinx.coroutines.flow.Flow<? extends com.psi.onlineshop.utils.Resource<com.psi.onlineshop.data.User>> p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.psi.onlineshop.utils.UserRegisterFieldsState> getValidation() {
        return null;
    }
    
    public final void createAcccountWithEmailAndPassword(@org.jetbrains.annotations.NotNull
    com.psi.onlineshop.data.User user, @org.jetbrains.annotations.NotNull
    java.lang.String password) {
    }
    
    private final boolean checkValidation(com.psi.onlineshop.data.User user, java.lang.String password) {
        return false;
    }
}