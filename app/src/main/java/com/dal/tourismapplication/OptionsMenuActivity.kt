package com.dal.tourismapplication

import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

open class OptionsMenuActivity: AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater: MenuInflater = menuInflater;
        menuInflater.inflate(R.menu.nav_drawer_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.home -> {
                val intent= Intent(this ,MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.tickets -> {
                val intent= Intent(this ,ViewTicketsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}