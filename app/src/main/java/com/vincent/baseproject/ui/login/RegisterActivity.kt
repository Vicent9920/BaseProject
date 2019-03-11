package com.vincent.baseproject.ui.login

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.vincent.baselibrary.helper.EditTextInputHelper
import com.vincent.baseproject.R
import com.vincent.baseproject.common.UIActivity
import com.vincent.baseproject.data.LoginRepository
import com.vincent.baseproject.data.Resource
import com.vincent.dialoglibrary.ToastDialog
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.app_toolbar.*
import java.util.regex.Pattern

class RegisterActivity : UIActivity() {
    private lateinit var viewModel: LoginViewModel
    private val mEditTextInputHelper by lazy {
        EditTextInputHelper(register_btn_commit)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        tv_leftMenu.text = "登录"
        tv_leftMenu.visibility = View.VISIBLE
        initToolBar(app_toolbar, false)
        mEditTextInputHelper.addView(register_et_email, register_et_password1, register_et_password2)
    }

    override fun initData() {
        super.initData()
        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LoginViewModel(LoginRepository.getInstance()) as T
            }
        })[LoginViewModel::class.java]
    }

    override fun initEvent() {
        super.initEvent()
        tv_leftMenu.setOnClickListener {
            val from = intent.getBooleanExtra("fromLogin", false)
            if (from) {
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        register_btn_commit.setOnClickListener {
            val username = register_et_email.text.toString()
            val password = register_et_password1.text.toString()
            val rPassword = register_et_password2.text.toString()
            if (!checkUserName(username)) {
                ToastDialog.Builder(this)
                    .setType(ToastDialog.Type.ERROR)
                    .setMessage("请输入正确的邮箱地址")
                    .show()
                return@setOnClickListener
            }
            if (!checkPassword(password)) {
                ToastDialog.Builder(this)
                    .setType(ToastDialog.Type.ERROR)
                    .setMessage("密码不符合规范\n以字母开头，长度在6~18之间，只能包含字母、数字和下划线")
                    .show()
                return@setOnClickListener
            }
            if (password != rPassword) {
                ToastDialog.Builder(this)
                    .setType(ToastDialog.Type.ERROR)
                    .setMessage("两次密码输入不一致")
                    .show()
                return@setOnClickListener
            }
            viewModel.register(username, password, rPassword)
        }
        viewModel.registerState.observe(this, object : Observer<Resource<String>> {
            override fun onChanged(t: Resource<String>?) {
                t ?: return
                when (t.status) {
                    0 -> {
                        ToastDialog.Builder(this@RegisterActivity)
                            .setType(ToastDialog.Type.FINISH)
                            .setMessage(t.data!!)
                            .show()
                    }
                    1 -> {
                        ToastDialog.Builder(this@RegisterActivity)
                            .setType(ToastDialog.Type.ERROR)
                            .setMessage(t.message!!)
                            .show()
                    }
                }
            }
        })
    }

    override fun isSupportSwipeBack(): Boolean {
        // 注册页不建议开起侧滑
        return false
    }

    private fun checkUserName(username: String): Boolean {
        if (username.isBlank()) return false
        val regExp = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+com"
        val p = Pattern.compile(regExp)
        val m = p.matcher(username)
        return m.matches()
    }

    private fun checkPassword(password: String): Boolean {
        if (password.isBlank()) return false
        val regExp = "^[a-zA-Z]\\w{5,17}\$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(password)
        return m.matches()
    }
}
