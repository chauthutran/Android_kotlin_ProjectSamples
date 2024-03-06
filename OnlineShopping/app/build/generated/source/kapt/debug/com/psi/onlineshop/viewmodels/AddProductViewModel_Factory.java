package com.psi.onlineshop.viewmodels;

import android.app.Application;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AddProductViewModel_Factory implements Factory<AddProductViewModel> {
  private final Provider<Application> applicationProvider;

  public AddProductViewModel_Factory(Provider<Application> applicationProvider) {
    this.applicationProvider = applicationProvider;
  }

  @Override
  public AddProductViewModel get() {
    return newInstance(applicationProvider.get());
  }

  public static AddProductViewModel_Factory create(Provider<Application> applicationProvider) {
    return new AddProductViewModel_Factory(applicationProvider);
  }

  public static AddProductViewModel newInstance(Application application) {
    return new AddProductViewModel(application);
  }
}
