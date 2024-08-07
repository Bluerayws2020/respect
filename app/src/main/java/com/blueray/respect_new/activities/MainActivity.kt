package com.blueray.respect_new.activities

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.blueray.respect.activities.Login
import com.blueray.respect_new.DrawerController
import com.blueray.respect_new.R
import com.blueray.respect_new.fragments.ApprovalsFragment
import com.blueray.respect_new.fragments.HomeFragment
import com.blueray.respect_new.fragments.MyProfileFragment
import com.blueray.respect_new.fragments.MyTeamFragment
import com.blueray.respect_new.fragments.PricingDetailsFragment
import com.blueray.respect_new.fragments.PricingFragment
import com.blueray.respect_new.helpers.HelperUtils
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle

class MainActivity : AppCompatActivity(), DrawerController {

    private lateinit var duoDrawerLayout: DuoDrawerLayout
    private lateinit var duoMenuView: DuoMenuView
    private lateinit var drawerToggle: DuoDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        duoDrawerLayout = findViewById(R.id.duo_drawer_layout)
        duoMenuView = findViewById(R.id.duo_menu)

        // Dynamically set the width of the drawer to 45% of the screen width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = (displayMetrics.widthPixels * 0.45).toInt()
        val params = duoMenuView.layoutParams
        params.width = width
        duoMenuView.layoutParams = params
        drawerToggle = DuoDrawerToggle(
            this,
            duoDrawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        duoDrawerLayout.setDrawerListener(drawerListener)
        drawerToggle.syncState()

        // Set the default fragment
        replaceFragment(HomeFragment())

        // Set up menu layout
        setupMenu()


        val menuBackground = findViewById<ImageView>(R.id.duo_view_menu_background)
        menuBackground.setImageResource(R.drawable.menu_bac1_new_color)
        menuBackground.setBackgroundColor(resources.getColor(R.color.text_color))
        duoDrawerLayout.setBackgroundResource(R.drawable.menu_bac1_new_color2)
        duoDrawerLayout.setMarginFactor(0.4F)
//        duoMenuView.setHeaderView(R.layout.approval_item)
        val header = findViewById<TextView>(R.id.header_title)
        val userName = HelperUtils.getUserName(this)
        header.text = "Hi $userName"
    }


    override fun openDrawer() {
        duoDrawerLayout.openDrawer(GravityCompat.START)
    }

    private val drawerListener = object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

        override fun onDrawerOpened(drawerView: View) {
            duoMenuView.visibility = View.VISIBLE
        }

        override fun onDrawerClosed(drawerView: View) {
            duoMenuView.visibility = View.GONE
        }

        override fun onDrawerStateChanged(newState: Int) {}
    }

    private fun setupMenu() {
        if (HelperUtils.getUserRole(this) == "sales") {
            duoMenuView.findViewById<LinearLayout>(R.id.menu_item_my_team).visibility = View.GONE
            duoMenuView.findViewById<LinearLayout>(R.id.menu_item_approval_center).visibility =
                View.GONE
        } else {
            duoMenuView.findViewById<LinearLayout>(R.id.menu_item_quotations_history).visibility =
                View.GONE
        }
        duoMenuView.findViewById<LinearLayout>(R.id.menu_item_home).setOnClickListener {
            replaceFragment(HomeFragment())
            duoDrawerLayout.closeDrawer(GravityCompat.START)
        }

        duoMenuView.findViewById<LinearLayout>(R.id.menu_item_profile).setOnClickListener {
            replaceFragment(MyProfileFragment())
            duoDrawerLayout.closeDrawer(GravityCompat.START)
        }

        duoMenuView.findViewById<LinearLayout>(R.id.menu_item_quotations_history)
            .setOnClickListener {
                replaceFragment(PricingFragment(null))
                duoDrawerLayout.closeDrawer(GravityCompat.START)
            }
        duoMenuView.findViewById<LinearLayout>(R.id.menu_item_my_team).setOnClickListener {
            replaceFragment(MyTeamFragment())
            duoDrawerLayout.closeDrawer(GravityCompat.START)
        }
        duoMenuView.findViewById<LinearLayout>(R.id.menu_item_approval_center).setOnClickListener {
            replaceFragment(ApprovalsFragment())
            duoDrawerLayout.closeDrawer(GravityCompat.START)
        }
        duoMenuView.findViewById<LinearLayout>(R.id.menu_item_logout).setOnClickListener {
            val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

            sharedPreferences.edit().apply {
                putString("uid", "0")
                putString("token", "0")
            }.apply()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
            duoDrawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }



    fun navigateToPricingDetailsFragment() {
        replaceFragment(PricingDetailsFragment())
    }

    fun navigateToHomeFragment() {
        replaceFragment(HomeFragment())
    }

    fun navigateToPricingFragment() {
        replaceFragment(PricingFragment(null))
    }
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed() // Or exit the app as the default behavior
        }
    }

}
