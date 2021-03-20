package com.example.sellit.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.example.sellit.R
import com.example.sellit.databinding.ActivityBaseBinding

open class BaseActivity<B, VM> : AppCompatActivity() where B : ViewDataBinding, VM : ViewModel {

    private lateinit var activityBaseBinding: ActivityBaseBinding
    protected lateinit var viewModel: VM
    protected lateinit var binding: B


    override fun setContentView(layoutResID: Int) {
        activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater, null, false)

        binding =
            DataBindingUtil.inflate(layoutInflater, layoutResID, activityBaseBinding.content, true)

        super.setContentView(activityBaseBinding.root)
    }

    fun setViewModel(viewModelClass: Class<VM>) {
        viewModel = ViewModelProvider(this).get(viewModelClass)
    }


    fun updateConstraints(@LayoutRes id: Int) {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(this, id)
        newConstraintSet.applyTo(activityBaseBinding.layoutRoot)
        val transition = ChangeBounds()
        transition.interpolator = OvershootInterpolator()
        TransitionManager.beginDelayedTransition(activityBaseBinding.layoutRoot, transition)
    }

    fun <T> changeActivity(
        cls: Class<T>?,
        isActivityFinish: Boolean
    ) {
        val intent = Intent(this, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        if (isActivityFinish) {
            finish()
        }
        overridePendingTransition(
            R.anim.animation_leave,
            R.anim.animation_enter
        )
    }

    fun <T> changeActivityForResult(
        cls: Class<T>?,
        requestCode: Int,
        isActivityFinish: Boolean
    ) {
        val intent = Intent(this, cls)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivityForResult(intent, requestCode)
        if (isActivityFinish) {
            finish()
        }
        overridePendingTransition(
            R.anim.animation_leave,
            R.anim.animation_enter
        )
    }


    protected fun <T> changeActivity(
        cls: Class<T>?,
        data: Bundle?,
        isActivityFinish: Boolean
    ) {
        val resultIntent = Intent(this, cls)
        if (data != null) resultIntent.putExtras(data)
        startActivity(resultIntent)
        if (isActivityFinish) finish()
        overridePendingTransition(
            R.anim.animation_leave,
            R.anim.animation_enter
        )
    }

    fun <T> changeActivityClearTop(
        cls: Class<T>?,
        isActivityFinish: Boolean
    ) {
        val intent = Intent(this, cls)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        if (isActivityFinish) {
            finish()
        }
        overridePendingTransition(
            R.anim.animation_leave,
            R.anim.animation_enter
        )
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.animation_finish_enter,
            R.anim.animation_finish_exit
        )
    }


    fun showProgressbar() {
        activityBaseBinding.progressbar.isVisible = true
    }

    fun hideProgressbar() {
        activityBaseBinding.progressbar.isVisible = false
    }

    protected open fun showSoftKeyboard() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    protected open fun hideSoftKeyboard(editText: EditText? = null) {
        val view = this.currentFocus
        if (editText != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        } else if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}
