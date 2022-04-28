package lishui.module.gitee.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lishui.module.gitee.R
import lishui.module.gitee.viewmodel.GiteeViewModel

/**
 * @author lishui.lin
 * Created it on 2021/5/27
 */

class LoginFragment : Fragment(R.layout.fragment_gitee_login) {

    private lateinit var userNameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginTextView: TextView

    private lateinit var viewModel: GiteeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(GiteeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userNameInput = view.findViewById(R.id.gitee_username_input)
        passwordInput = view.findViewById(R.id.gitee_password_input)
        loginTextView = view.findViewById(R.id.gitee_login_btn)

        loginTextView.setOnClickListener {
            val user = userNameInput.text.toString()
            val password = passwordInput.text.toString()
            if (user.isBlank() || password.isBlank()) {
                return@setOnClickListener
            }
            viewModel.loginGitee(userName = user, password = password)
        }
    }
}