package com.example.gestiondetelephone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.gestiondetelephone.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.email
import kotlinx.android.synthetic.main.activity_sign_up.loginBtn
import kotlinx.android.synthetic.main.activity_sign_up.nom
import kotlinx.android.synthetic.main.activity_sign_up.nomUtilisateur
import kotlinx.android.synthetic.main.activity_sign_up.prenom
import kotlinx.android.synthetic.main.activity_sign_up.signUpButton
import kotlinx.android.synthetic.main.activity_update_user.*
import java.util.concurrent.TimeUnit

class updateUser : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var database: DatabaseReference
    lateinit var  number : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        number = intent.getStringExtra("NumT").toString()
         NumTel.text = number
            loginBtn.setOnClickListener {
                startActivity(Intent(this, com.example.gestiondetelephone.MainActivity::class.java))
            }


        signUpButton.setOnClickListener {


                if (nom.text.toString().isEmpty()) {
                    Toast.makeText(
                        this,
                        "Enter your first name please",
                        Toast.LENGTH_SHORT
                    ).show()
                    nom.requestFocus()

                } else if (prenom.text.toString().isEmpty()) {
                    Toast.makeText(
                        this,
                        "Enter your last name please",
                        Toast.LENGTH_SHORT
                    ).show()
                    prenom.requestFocus()

                } else if (nomUtilisateur.text.toString().isEmpty()) {
                    Toast.makeText(
                        this,
                        "Enter your user name please",
                        Toast.LENGTH_SHORT
                    ).show()
                    nomUtilisateur.requestFocus()

                } else if (email.text.toString().isEmpty()) {
                    Toast.makeText(
                        this,
                        "Enter your email please",
                        Toast.LENGTH_SHORT
                    ).show()
                    email.requestFocus()

                } else {
                    val nom = binding.nom.text.toString()
                    val prenom = binding.prenom.text.toString()
                    val nomUtilisateur = binding.nomUtilisateur.text.toString()
                    val email = binding.email.text.toString()



                    database = FirebaseDatabase.getInstance().getReference("Users")
                    val User = User1(prenom, nomUtilisateur, email, number)
                    database.child(nom).setValue(User).addOnSuccessListener {

                        binding.nom.text?.clear()
                        binding.prenom.text?.clear()
                        binding.nomUtilisateur.text?.clear()
                        binding.email.text?.clear()
                        binding.numTel.text?.clear()


                    }
                }

            }

            auth = FirebaseAuth.getInstance()
            var currentUser = auth.currentUser


            if (currentUser != null) {
                startActivity(Intent(applicationContext, homeActivity::class.java))
                finish()
            }

            signUpButton.setOnClickListener {
                login()
            }


            // Callback function for Phone Auth
            callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(applicationContext,
                        "Please enter a valid number",
                        Toast.LENGTH_LONG)
                        .show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {

                    Log.d("TAG", "onCodeSent:$verificationId")
                    storedVerificationId = verificationId
                    resendToken = token

                    var intent = Intent(applicationContext, Verify::class.java)
                    intent.putExtra("storedVerificationId", storedVerificationId)
                    startActivity(intent)
                    finishAffinity()
                }
            }
        }



    private fun login() {
        var number = binding.numTel.text.toString().trim()
        if (!number.isEmpty()) {
            number = "+216" + number
            sendVerificationcode(number)
        } else {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }




    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    }
