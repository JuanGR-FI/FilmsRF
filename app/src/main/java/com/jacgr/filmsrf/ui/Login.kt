package com.jacgr.filmsrf.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.util.PatternsCompat
import com.google.android.material.snackbar.Snackbar
import com.jacgr.filmsrf.R
import com.jacgr.filmsrf.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    //Para las cajas de texto
    private var email = ""
    private var contrasenia = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogIn.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener

            binding.pbLoading.visibility = View.VISIBLE

            autenticaUsuario(email, contrasenia)
        }

        binding.btnSignUp.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener

            binding.pbLoading.visibility = View.VISIBLE

            registraUsuario(email, contrasenia)
        }

    }

    private fun registraUsuario(email: String, contrasenia: String) {
        //Registrando al usuario
        /*firebaseAuth.createUserWithEmailAndPassword(email, contrasenia).addOnCompleteListener { authResult->
            if(authResult.isSuccessful){
                //Enviar correo para verificación de email
                val user_fb = firebaseAuth.currentUser
                user_fb?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(this, "El correo de verificación ha sido enviado", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "No se pudo enviar el correo de verificación", Toast.LENGTH_SHORT).show()
                }

                Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("psw", contrasenia)
                startActivity(intent)
                finish()


            }else{
                binding.progressBar.visibility = View.GONE
                manejaErrores(authResult)
            }
        }*/
    }

    private fun validaCampos(): Boolean{
        email = binding.etLoginEmail.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.etLoginPassword.text.toString().trim()

        if(email.isNotEmpty()){
            if(!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
                val snack = Snackbar.make(binding.root, getString(R.string.inavalid_email), Snackbar.LENGTH_LONG)
                snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
                snack.show()
                return false
            }

        }else{
            val snack = Snackbar.make(binding.root, getString(R.string.inavalid_email), Snackbar.LENGTH_LONG)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
            snack.show()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            val snack = Snackbar.make(binding.root, getString(R.string.invalid_password), Snackbar.LENGTH_LONG)
            snack.view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error))
            snack.show()
            return false
        }

        return true
    }

    private fun autenticaUsuario(usr: String, psw: String) {
        /*firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            if(authResult.isSuccessful){
                Toast.makeText(this, "Autenticacion exitosa", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("psw", psw)

                startActivity(intent)
                finish()
            }else{
                binding.progressBar.visibility = View.GONE
                manejaErrores(authResult)
            }
        }*/
    }

}