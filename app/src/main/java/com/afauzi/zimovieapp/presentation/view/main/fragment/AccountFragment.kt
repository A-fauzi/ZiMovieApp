package com.afauzi.zimovieapp.presentation.view.main.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.databinding.FragmentAccountBinding
import com.afauzi.zimovieapp.presentation.view.auth.SignInActivity
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
            requireActivity().finish()
        }
    }
}