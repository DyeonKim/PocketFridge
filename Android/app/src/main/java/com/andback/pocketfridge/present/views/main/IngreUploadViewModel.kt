package com.andback.pocketfridge.present.views.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andback.pocketfridge.data.model.FridgeEntity
import com.andback.pocketfridge.data.model.MainCategoryEntity
import com.andback.pocketfridge.data.model.SubCategoryEntity
import com.andback.pocketfridge.domain.model.Ingredient
import com.andback.pocketfridge.domain.usecase.category.GetCategoryUseCase
import com.andback.pocketfridge.domain.usecase.ingredient.UploadIngreUseCase
import com.andback.pocketfridge.present.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class IngreUploadViewModel @Inject constructor(
    private val uploadIngreUseCase: UploadIngreUseCase,
    private val getCategoryUseCase: GetCategoryUseCase
): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    // region view 상태
    // 데이터를 바탕으로 업로드할 식재료 객체 생성
    private val _selectedFridge = MutableLiveData<FridgeEntity>()
    val selectedFridge: LiveData<FridgeEntity> get() = _selectedFridge
    private val _selectedStorage = MutableLiveData<Storage>()
    val selectedStorage: LiveData<Storage> get() = _selectedStorage
    private val _selectedMainCategory = MutableLiveData<MainCategoryEntity>()
    val selectedMainCategory: LiveData<MainCategoryEntity> get() = _selectedMainCategory
    private val _selectedSubCategory = MutableLiveData<SubCategoryEntity>()
    val selectedSubCategory: LiveData<SubCategoryEntity> get() = _selectedSubCategory
    private val _selectedSubCategories = MutableLiveData<List<SubCategoryEntity>>()
    val selectedSubCategories: LiveData<List<SubCategoryEntity>> get() = _selectedSubCategories
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name
    private val _datePurchased = MutableLiveData<String>()
    val ingreDatePurchased: LiveData<String> get() = _datePurchased
    private val _dateExpiry = MutableLiveData<String>()
    val ingreDateExpiry: LiveData<String> get() = _dateExpiry

    // endregion

    // region 카테고리 리스트 라이브 데이터
    private val _mainCategories = MutableLiveData<List<MainCategoryEntity>>()
    val mainCategories: LiveData<List<MainCategoryEntity>>
        get() = _mainCategories

    private val _subCategories = MutableLiveData<List<SubCategoryEntity>>()
    val subCategories: LiveData<List<SubCategoryEntity>>
        get() = _subCategories
    // endregion

    // region 에러 라이브 데이터
    private val _isNameError = MutableLiveData(false)
    val isNameError: LiveData<Boolean>
        get() = _isNameError
    private val _isQuantityError = MutableLiveData(false)
    val isQuantityError: LiveData<Boolean>
        get() = _isQuantityError
    private val _isDatePurchasedError = MutableLiveData(false)
    val isDatePurchasedError: LiveData<Boolean>
        get() =_isDatePurchasedError
    private val _isDateExpiryError = MutableLiveData(false)
    val isDateExpiryError: LiveData<Boolean>
        get() = _isDateExpiryError
    private val _isCategoryError = MutableLiveData(false)
    val isCategoryError: LiveData<Boolean>
        get() =_isCategoryError
    private val _isStorageError = MutableLiveData(false)
    val isStorageError: LiveData<Boolean>
        get() = _isStorageError
    private val _isFridgeIdError = MutableLiveData(false)
    val isFridgeIdError: LiveData<Boolean>
        get() = _isFridgeIdError
    private val _isNetworkError = MutableLiveData(false)
    val isNetworkError: LiveData<Boolean>
        get() =_isNetworkError
    private val _isServerError = MutableLiveData(false)
    val isServerError: LiveData<Boolean>
        get() = _isServerError

    // endregion

    init {
        getCategoryUseCase.getAllCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if(!it.data.isNullOrEmpty()) {
                        when {
                            it.data[0] is MainCategoryEntity -> {
                                _mainCategories.value = it.data as List<MainCategoryEntity>
                            }
                            it.data[0] is SubCategoryEntity -> {
                                _subCategories.value = it.data as List<SubCategoryEntity>
                            }
                            else -> {
                                // TODO: error 처리
                            }
                        }
                    }
                },
                {
                    // TODO: 카테고리 에러 처리
                },
                {
                    // TODO: complete 처리
                },
                {
                    // TODO: onSubscribe 처리
                }
            )
    }

    fun onUploadBtnClick() {
        compositeDisposable.add(
            uploadIngreUseCase.uploadIngre(getIngredientFromInput())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        // TODO: 식재료 등록 성공      
                    },
                    { throwable ->
                        handleException(throwable)
                    },
                    {
                        // TODO: hideLoading() 
                    },
                    {
                        // TODO: showLoading() 
                    }
                )
        )
    }

    private fun handleException(e: Throwable) {
        clearError()
        when(e) {
            is HttpException -> {
                _isServerError.value = true
            }
            is IOException -> {
                _isNetworkError.value = true
            }
            is IngreNameException -> {
                _isNameError.value = true
            }
            is IngreQuantityException -> {
                _isQuantityError.value = true
            }
            is IngreFridgeIdException -> {
                _isFridgeIdError.value = true
            }
            is IngreCategoryException -> {
                _isCategoryError.value = true
            }
            is IngreDatePurchasedException -> {
                _isDatePurchasedError.value = true
            }
            is IngreDateExpiryException -> {
                _isDateExpiryError.value = true
            }
        }
    }

    private fun clearError() {
        _isNameError.value = false
        _isQuantityError.value = false
        _isDatePurchasedError.value = false
        _isDateExpiryError.value = false
        _isCategoryError.value = false
        _isStorageError.value = false
        _isFridgeIdError.value = false
        _isNetworkError.value = false
        _isServerError.value = false
    }

    private fun getIngredientFromInput(): Ingredient {
        // TODO: 수량 data가 필요해지면 추가
        // TODO: mapper 필요
        return Ingredient(quantity = 1, category = selectedSubCategory.value?.subCategoryId?: -1, name = name.value?: "", purchasedDate = ingreDatePurchased.value.toString(), expiryDate = ingreDateExpiry.value.toString(), fridgeId = selectedFridge.value?.refrigeratorId?: -1, storage = selectedStorage.value?:Storage.Fridge)
    }

    fun setFridge() {
        _selectedStorage.value = Storage.Fridge
    }

    fun setFreeze() {
        _selectedStorage.value = Storage.Freeze
    }

    fun setRoom() {
        _selectedStorage.value = Storage.Room
    }

    private fun setDefaultData() {
        _name.value = ""
        _datePurchased.value = ""
        _dateExpiry.value = ""
        _selectedStorage.value = Storage.Fridge
        _selectedFridge.value = getDefaultFridge()
        _selectedMainCategory.value = getDefaultMainCategory()
        _selectedSubCategory.value = getDefaultSubCategory()
    }

    /**
     * 메인 카테고리의 첫 번째 값이 디폴트
     */
    private fun getDefaultMainCategory(): MainCategoryEntity? {
        return if(!mainCategories.value.isNullOrEmpty()) {
            mainCategories.value!![0]
        } else {
            null
        }
    }

    /**
     * 디폴트 메인 카테고리의 서브 카테고리의 첫 번째 값이 디폴트
     */
    private fun getDefaultSubCategory(): SubCategoryEntity? {
        return if(!subCategories.value.isNullOrEmpty() && selectedMainCategory.value != null) {
            _selectedSubCategories.value = subCategories.value!!.filter { it.mainCategoryId == selectedMainCategory.value!!.mainCategoryId }
            _selectedSubCategories.value!![0]
        } else {
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun clearData() {
        clearError()
        setDefaultData()
    }
}