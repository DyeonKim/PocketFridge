package com.andback.pocketfridge.present.views.user.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andback.pocketfridge.domain.usecase.user.GetLoginUseCase
import com.andback.pocketfridge.domain.usecase.user.GetSignUpUseCase
import com.andback.pocketfridge.present.config.SingleLiveEvent
import com.andback.pocketfridge.present.utils.PageSet
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getLoginUseCase: GetLoginUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val email = MutableLiveData<String>()
    val pw = MutableLiveData<String>()

    // view가 다음으로 넘어갈 페이지를 observe 하기 위함
    val pageNumber = SingleLiveEvent<PageSet>()

    // 로딩을 보여줄지 결정하기 위함
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _toastMsg = SingleLiveEvent<String>()
    val toastMsg: LiveData<String> get() = _toastMsg

    private fun login(req: MutableMap<String, String>) {
        _isLoading.value = true

        compositeDisposable.add(
            getLoginUseCase.execute(req)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _toastMsg.value = it.message
                        _isLoading.value = false
                        pageNumber.value = PageSet.MAIN
                    },
                    {
                        _isLoading.value = false
                        showError(it)
                    },
                )
        )
    }

    fun onLoginClick() {
        login(getEnteredUserInfo())
    }

    fun onSocialLoginClick() {

    }

    fun onSignUpClick() {
        pageNumber.value = PageSet.EMAIL_AUTH
    }

    fun onFindPwClick() {
        pageNumber.value = PageSet.FIND_PW
    }

    private fun showError(t : Throwable) {
        if (t is HttpException && (t.code() in 400 until 500)){
            var responseBody = t.response()?.errorBody()?.string()
            val jsonObject = JSONObject(responseBody!!.trim())
            var message = jsonObject.getString("message")
            _toastMsg.value = message
            Log.d("LoginViewModel", "${t.code()}")
        } else {
            _toastMsg.value = t.message
        }
    }

    private fun getEnteredUserInfo(): MutableMap<String, String>{
        val req = mutableMapOf<String, String>()
        req["userEmail"] = email.value.toString()
        req["userPassword"] = pw.value.toString()

        return req
    }
}