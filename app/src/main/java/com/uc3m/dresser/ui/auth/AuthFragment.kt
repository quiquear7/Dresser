package com.uc3m.dresser.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.uc3m.dresser.ProviderType
import com.uc3m.dresser.R
import com.uc3m.dresser.databinding.FragmentAuthBinding


class AuthFragment : Fragment() {
    companion object{
        private const val BC_SIGN_IN = 120
    }
    private lateinit var  googleSignInClient: GoogleSignInClient
    private lateinit var button: FloatingActionButton
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: FragmentAuthBinding
    private val GOOGLE_SIGN_IN = 100
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAuthBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if(user!=null){
            findNavController().navigate(R.id.action_authFragment_to_navigation_home)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient =  GoogleSignIn.getClient(requireContext(), gso)
        googleSignInClient.signOut()
        binding.googleButton.setOnClickListener{
            signIn()
        }

        return binding.root
    }
    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, BC_SIGN_IN)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == BC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Fragment Login", "firebaseAuthWithGoogle: " + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException){
                Log.w("Fragment Login", "Google Sign In Failes", e)
            }
        }
    }



    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Fragment Login", "signInWithCredential:success")
                        val user = auth.currentUser
                        if (user != null){
                            findNavController().navigate(R.id.action_authFragment_to_navigation_home)
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Fragment Login", "signInWithCredential:failure", task.exception)

                    }
                }
    }
}