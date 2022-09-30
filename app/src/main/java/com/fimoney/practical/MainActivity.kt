package com.fimoney.practical

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fimoney.practical.databinding.ActivityMainBinding
import com.fimoney.practical.databinding.DialogProgressBinding
import com.fimoney.practical.ui.main.MainView

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var binding: ActivityMainBinding

    private var progressDialog: Dialog? = null

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this

        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(R.id.nav_home)
        navController.graph = graph

    }

    override fun showProgress() {
        val dialog = progressDialog ?: Dialog(this).also {
            progressDialog = it
        }
        val dialogBinding = DialogProgressBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false)
        if (!isFinishing) {
            dialog.show()
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun hideProgress() {
        val dialog = progressDialog ?: return
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        progressDialog = null
    }
}