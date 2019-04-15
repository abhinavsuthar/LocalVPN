package com.vpn.ui


import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vpn.R
import com.vpn.px
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firewall_btn.setOnClickListener {
            // findNavController().navigate(R.id.firewallFragment)
            findNavController().navigate(R.id.webBrowserFragment)
        }

        access_btn.setOnClickListener {
            findNavController().navigate(R.id.appInternetAccessFragment)
        }

        animateImage()
    }

    private fun animateImage() {
        Glide.with(this)
            .load(R.drawable.sumitk)
            .apply(RequestOptions.circleCropTransform())
            .into(image)
        /*val anim = RotateAnimation(0f, 350f, 15f, 15f)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE
        anim.duration = 700

        image.startAnimation(anim)*/

        val height = Resources.getSystem().displayMetrics.heightPixels
        val x = 128.px

        val path = Path().apply {
            arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true)
        }
        val animator = ObjectAnimator.ofFloat(image, "translationY", (height - x) / 2F).apply {
            duration = 700
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

}
