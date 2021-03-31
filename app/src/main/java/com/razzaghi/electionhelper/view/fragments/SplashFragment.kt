package com.razzaghi.electionhelper.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.razzaghi.electionhelper.R
import com.razzaghi.electionhelper.util.SimpleAnimation.infinityFadeInFadeOut
import kotlinx.android.synthetic.main.splash_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment(R.layout.splash_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnimation()
        startsplash()

    }

    private fun startsplash() {
        lifecycleScope.launch {
            delay(3000L)
            val action =
                SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun playAnimation() {
        CoroutineScope(Dispatchers.Main).launch {
            presidentAnimation.playAnimation()

            infinityFadeInFadeOut(txtWelcome)
        }

    }
}