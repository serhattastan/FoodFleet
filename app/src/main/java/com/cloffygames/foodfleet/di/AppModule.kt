package com.cloffygames.foodfleet.di

import android.app.Application
import android.content.Context
import com.cloffygames.foodfleet.data.datasource.AuthenticationDataSource
import com.cloffygames.foodfleet.data.datasource.FirebaseFoodDataSource
import com.cloffygames.foodfleet.data.datasource.FoodDataSource
import com.cloffygames.foodfleet.data.repo.AuthenticationRepository
import com.cloffygames.foodfleet.data.repo.FirebaseFoodRepository
import com.cloffygames.foodfleet.data.repo.FoodRepository
import com.cloffygames.foodfleet.retrofit.ApiUtils
import com.cloffygames.foodfleet.retrofit.FoodDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule, uygulama genelinde kullanılacak bağımlılıkları sağlayan bir Dagger Hilt modülüdür.
 * Bu modül, SingletonComponent'e yüklendiği için tüm uygulama yaşam döngüsü boyunca aynı bağımlılıkları sunar.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /**
     * Firebase Authentication sağlayıcısını sağlar.
     *
     * @return FirebaseAuth nesnesi.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Google Sign-In için gerekli yapılandırma seçeneklerini sağlar.
     *
     * @return GoogleSignInOptions nesnesi.
     */
    @Provides
    @Singleton
    fun provideGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("856886753550-j2oe4qa5chec6gt16m7oemf060f5eb9p.apps.googleusercontent.com")  // Google API Client ID
            .requestEmail()
            .build()
    }

    /**
     * Uygulama context'ini sağlar.
     *
     * @param application Application nesnesi.
     * @return Context nesnesi.
     */
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    /**
     * Google Sign-In istemcisini sağlar.
     *
     * @param context Uygulama context'i.
     * @param googleSignInOptions Google Sign-In yapılandırma seçenekleri.
     * @return GoogleSignInClient nesnesi.
     */
    @Provides
    @Singleton
    fun provideGoogleSignInClient(
        context: Context,
        googleSignInOptions: GoogleSignInOptions
    ): GoogleSignInClient {
        return GoogleSignIn.getClient(context, googleSignInOptions)
    }

    /**
     * AuthenticationRepository sağlayıcısını sağlar.
     *
     * @param ads AuthenticationDataSource nesnesi.
     * @return AuthenticationRepository nesnesi.
     */
    @Provides
    @Singleton
    fun provideAuthenticationRepository(ads: AuthenticationDataSource): AuthenticationRepository {
        return AuthenticationRepository(ads)
    }

    /**
     * AuthenticationDataSource sağlayıcısını sağlar.
     *
     * @param firebaseAuth Firebase Authentication nesnesi.
     * @param googleSignInClient Google Sign-In istemcisi.
     * @return AuthenticationDataSource nesnesi.
     */
    @Provides
    @Singleton
    fun provideAuthenticationDataSource(firebaseAuth: FirebaseAuth, googleSignInClient: GoogleSignInClient): AuthenticationDataSource {
        return AuthenticationDataSource(firebaseAuth, googleSignInClient)
    }

    /**
     * Firebase Firestore sağlayıcısını sağlar.
     *
     * @return FirebaseFirestore nesnesi.
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * FirebaseFoodRepository sağlayıcısını sağlar.
     *
     * @param fds FirebaseFoodDataSource nesnesi.
     * @return FirebaseFoodRepository nesnesi.
     */
    @Provides
    @Singleton
    fun provideFirebaseFoodRepository(fds: FirebaseFoodDataSource): FirebaseFoodRepository {
        return FirebaseFoodRepository(fds)
    }

    /**
     * FirebaseFoodDataSource sağlayıcısını sağlar.
     *
     * @param collectionFoods Firebase Firestore koleksiyon referansı.
     * @return FirebaseFoodDataSource nesnesi.
     */
    @Provides
    @Singleton
    fun provideFirebaseFoodDataSource(collectionFoods: CollectionReference): FirebaseFoodDataSource {
        return FirebaseFoodDataSource(collectionFoods)
    }

    /**
     * Firebase Firestore'dan 'yemekler' koleksiyonunu sağlar.
     *
     * @return Firebase Firestore CollectionReference nesnesi.
     */
    @Provides
    @Singleton
    fun provideCollectionReference(): CollectionReference {
        return FirebaseFirestore.getInstance().collection("yemekler")
    }

    /**
     * FoodDao sağlayıcısını sağlar.
     *
     * @return FoodDao Retrofit arayüzü.
     */
    @Provides
    @Singleton
    fun provideFoodDao(): FoodDao {
        return ApiUtils.getFoodDao()
    }

    /**
     * FoodDataSource sağlayıcısını sağlar.
     *
     * @param fdao API ile yemek verilerini almak için kullanılan FoodDao nesnesi.
     * @return FoodDataSource nesnesi.
     */
    @Provides
    @Singleton
    fun provideFoodDataSource(fdao: FoodDao): FoodDataSource {
        return FoodDataSource(fdao)
    }

    /**
     * FoodRepository sağlayıcısını sağlar.
     *
     * @param fds FoodDataSource nesnesi.
     * @return FoodRepository nesnesi.
     */
    @Provides
    @Singleton
    fun provideFoodRepository(fds: FoodDataSource): FoodRepository {
        return FoodRepository(fds)
    }

}