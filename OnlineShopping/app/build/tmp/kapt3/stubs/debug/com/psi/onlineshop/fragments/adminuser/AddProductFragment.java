package com.psi.onlineshop.fragments.adminuser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.contract.ActivityResultContracts;
import com.psi.onlineshop.data.Product;
import com.psi.onlineshop.databinding.FragmentAddProductBinding;
import com.psi.onlineshop.viewmodels.AddProductViewModel;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import dagger.hilt.android.AndroidEntryPoint;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u0018\u0010\u0018\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u00162\u0006\u0010\u001a\u001a\u00020\u0019H\u0002J&\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\u001a\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u001c2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u0010&\u001a\u00020$H\u0002J\b\u0010\'\u001a\u00020$H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR \u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\t\"\u0004\b\r\u0010\u000eR\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006("}, d2 = {"Lcom/psi/onlineshop/fragments/adminuser/AddProductFragment;", "Landroidx/fragment/app/Fragment;", "()V", "binding", "Lcom/psi/onlineshop/databinding/FragmentAddProductBinding;", "selectedColors", "", "", "getSelectedColors", "()Ljava/util/List;", "selectedImages", "Landroid/net/Uri;", "getSelectedImages", "setSelectedImages", "(Ljava/util/List;)V", "viewModel", "Lcom/psi/onlineshop/viewmodels/AddProductViewModel;", "getViewModel", "()Lcom/psi/onlineshop/viewmodels/AddProductViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "getImagesByteArrays", "", "", "getSizesList", "", "sizes", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "", "view", "updateColors", "updateImages", "app_debug"})
public final class AddProductFragment extends androidx.fragment.app.Fragment {
    private com.psi.onlineshop.databinding.FragmentAddProductBinding binding;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.List<java.lang.Integer> selectedColors = null;
    @org.jetbrains.annotations.NotNull
    private java.util.List<android.net.Uri> selectedImages;
    
    public AddProductFragment() {
        super();
    }
    
    private final com.psi.onlineshop.viewmodels.AddProductViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<java.lang.Integer> getSelectedColors() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<android.net.Uri> getSelectedImages() {
        return null;
    }
    
    public final void setSelectedImages(@org.jetbrains.annotations.NotNull
    java.util.List<android.net.Uri> p0) {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final java.util.List<java.lang.String> getSizesList(java.lang.String sizes) {
        return null;
    }
    
    private final void updateColors() {
    }
    
    private final void updateImages() {
    }
    
    private final java.util.List<byte[]> getImagesByteArrays() {
        return null;
    }
}