package io.mangel.issuemanager.activities.login

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import io.mangel.issuemanager.R
import io.mangel.issuemanager.activities.AbstractLoadingViewModel
import io.mangel.issuemanager.extensions.afterTextChanged
import kotlinx.android.synthetic.main.activity_login.view.*

data class LoginViewModel<T>(private val context: T, private val view: View) : AbstractLoadingViewModel()
        where T : Context, T : LoginViewModel.Login {
    private val viewHolder = ViewHolder(view)

    init {
        viewHolder.usernameExitText.afterTextChanged {
            checkCanSubmit()
        }

        viewHolder.passwordEditText.apply {
            afterTextChanged {
                checkCanSubmit()
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        login()
                }
                false
            }
        }

        viewHolder.createTrialAccountButton.setOnClickListener {
            viewHolder.loadingProgressBar.visibility = View.VISIBLE
            context.createTrialAccount()
        }

        viewHolder.loginButton.setOnClickListener {
            viewHolder.loadingProgressBar.visibility = View.VISIBLE
            login()
        }
    }

    private fun login() {
        context.login(viewHolder.usernameExitText.text.toString(), viewHolder.passwordEditText.text.toString())
    }

    private var passwordTouched = false
    private var usernameTouched = false

    private fun checkCanSubmit() {
        val usernameBlank = viewHolder.usernameExitText.text.isBlank()
        val passwordBlank = viewHolder.passwordEditText.text.isBlank()
        usernameTouched = usernameBlank || usernameTouched
        passwordTouched = passwordBlank || passwordTouched

        if (usernameBlank && usernameTouched) {
            viewHolder.usernameExitText.error = context.getString(R.string.invalid_email)
        } else if (passwordBlank && passwordTouched) {
            viewHolder.passwordEditText.error = context.getString(R.string.password_too_short)
        }

        viewHolder.loginButton.isEnabled = !usernameBlank && !passwordBlank;
    }

    interface Login {
        fun login(email: String, password: String)

        fun createTrialAccount()
    }

    override fun getLoadingIndicator(): ProgressBar {
        return viewHolder.loadingProgressBar
    }

    fun setUsernamePassword(username: String, password: String) {
        viewHolder.usernameExitText.setText(username, TextView.BufferType.EDITABLE)
        viewHolder.passwordEditText.setText(password, TextView.BufferType.EDITABLE)

        checkCanSubmit()
    }

    class ViewHolder(view: View) {
        val usernameExitText: EditText = view.username
        val passwordEditText: EditText = view.password
        val loginButton: Button = view.login
        val createTrialAccountButton: Button = view.create_trial_account
        val loadingProgressBar: ProgressBar = view.loading
    }
}