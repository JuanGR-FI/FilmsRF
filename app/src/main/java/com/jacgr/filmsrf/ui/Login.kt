package com.jacgr.filmsrf.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.util.PatternsCompat
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.jacgr.filmsrf.R
import com.jacgr.filmsrf.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    //Para las cajas de texto
    private var email = ""
    private var contrasenia = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogIn.setOnClickListener {
            if (!validaCampos()) return@setOnClickListener

            binding.pbLoading.visibility = View.VISIBLE

            autenticaUsuario(email, contrasenia)
        }

        binding.btnSignUp.setOnClickListener {
            if (!validaCampos()) return@setOnClickListener

            binding.pbLoading.visibility = View.VISIBLE

            registraUsuario(email, contrasenia)
        }

        binding.tvForgotPassword.setOnClickListener {
            mandaEmailDeRestauracion()
        }

    }

    private fun mandaEmailDeRestauracion() {
        val resetMail = EditText(this)
        resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        val passwordResetDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.reset_password_title))
            .setMessage(getString(R.string.reset_password_message))
            .setView(resetMail)
            .setPositiveButton(getString(R.string.reset_password_send_btn)) { _, _ ->
                val mail = resetMail.text.toString()
                if (mail.isNotEmpty()) {
                    firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                        val snack = Snackbar.make(
                            binding.root,
                            getString(R.string.check_mailbox),
                            Snackbar.LENGTH_LONG
                        )
                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_snackbar))
                        snack.show()
                    }.addOnFailureListener {
                        val snack = Snackbar.make(
                            binding.root,
                            getString(R.string.error_sending_mail, it.message),
                            Snackbar.LENGTH_LONG
                        )
                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                        snack.show()
                    }
                } else {
                    val snack = Snackbar.make(
                        binding.root,
                        getString(R.string.invalid_email),
                        Snackbar.LENGTH_LONG
                    )
                    snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                    snack.show()
                }
            }.setNegativeButton(getString(R.string.reset_password_cancel_btn)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun registraUsuario(email: String, contrasenia: String) {
        //Registrando al usuario
        firebaseAuth.createUserWithEmailAndPassword(email, contrasenia)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    //Enviar correo para verificación de email
                    val user_fb = firebaseAuth.currentUser
                    user_fb?.sendEmailVerification()?.addOnSuccessListener {
                        val snack = Snackbar.make(
                            binding.root,
                            getString(R.string.verification_mail_sent),
                            Snackbar.LENGTH_LONG
                        )
                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.orange_snackbar))
                        snack.show()
                    }?.addOnFailureListener {
                        val snack = Snackbar.make(
                            binding.root,
                            getString(R.string.error_verification_email),
                            Snackbar.LENGTH_LONG
                        )
                        snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                        snack.show()
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    binding.pbLoading.visibility = View.GONE
                    manejaErrores(authResult)
                }
            }
    }

    private fun validaCampos(): Boolean {
        email = binding.etLoginEmail.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.etLoginPassword.text.toString().trim()

        if (email.isNotEmpty()) {
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.invalid_email),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()
                return false
            }

        } else {
            val snack =
                Snackbar.make(binding.root, getString(R.string.invalid_email), Snackbar.LENGTH_LONG)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
            snack.show()
            return false
        }

        if (contrasenia.isEmpty() || contrasenia.length < 6) {
            val snack = Snackbar.make(
                binding.root,
                getString(R.string.invalid_password),
                Snackbar.LENGTH_LONG
            )
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
            snack.show()
            return false
        }

        return true
    }

    private fun autenticaUsuario(usr: String, psw: String) {
        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                binding.pbLoading.visibility = View.GONE
                manejaErrores(authResult)
            }
        }
    }

    private fun manejaErrores(task: Task<AuthResult>) {
        var errorCode = ""

        try {
            errorCode = (task.exception as FirebaseAuthException).errorCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        when (errorCode) {
            "ERROR_INVALID_EMAIL" -> {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.invalid_email),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()
            }
            "ERROR_WRONG_PASSWORD" -> {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.invalid_password),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.account_exists_different_credentials),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.email_already_in_use),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.token_expired),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()

            }
            "ERROR_USER_NOT_FOUND" -> {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.user_not_found),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.invalid_password),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()
            }
            "NO_NETWORK" -> {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.netwok_error),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()
            }
            else -> {
                val snack = Snackbar.make(
                    binding.root,
                    getString(R.string.authentication_failed),
                    Snackbar.LENGTH_LONG
                )
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()
            }
        }

    }

}