package com.req

import android.Manifest.permission.*
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsRequest
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var locationManager: LocationManager? = null

    //@RequiresPermission(anyOf = [ACCESS_FINE_LOCATION,WRITE_EXTERNAL_STORAGE,ACCESS_COARSE_LOCATION, SET_WALLPAPER, BLUETOOTH, CALL_PHONE, WRITE_EXTERNAL_STORAGE])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val phone = "+34671701551"
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
        startActivity(intent)

        methodRequiresPermissions()
    }


    val quickPermissionsOption = QuickPermissionsOptions(
        rationaleMessage = "Custom rational message",
        permanentlyDeniedMessage = "Custom permanently denied message",
        rationaleMethod = { req -> rationaleCallback(req) },
        permanentDeniedMethod = { req -> permissionsPermanentlyDenied(req) }
    )
    private fun rationaleCallback(req: QuickPermissionsRequest) {
        // this will be called when permission is denied once or more time. Handle it your way
        AlertDialog.Builder(this)
            .setTitle("Permissions Denied")
            .setMessage("This is the custom rationale dialog. Please allow us to proceed " + "asking for permissions again, or cancel to end the permission flow.")
            .setPositiveButton("Go Ahead") { dialog, which -> req.proceed() }
            .setNegativeButton("cancel") { dialog, which -> req.cancel() }
            .setCancelable(false)
            .show()
    }


    private fun permissionsPermanentlyDenied(req: QuickPermissionsRequest) {
        // this will be called when some/all permissions required by the method are permanently
        // denied. Handle it your way.
        AlertDialog.Builder(this)
            .setTitle("Permissions Denied")
            .setMessage("This is the custom permissions permanently denied dialog. " +
                    "Please open app settings to open app settings for allowing permissions, " +
                    "or cancel to end the permission flow.")
            .setPositiveButton("App Settings") { dialog, which -> req.openAppSettings() }
            .setNegativeButton("Cancel") { dialog, which -> req.cancel() }
            .setCancelable(false)
            .show()
    }


    private fun methodRequiresPermissions() = runWithPermissions(ACCESS_FINE_LOCATION,WRITE_EXTERNAL_STORAGE,ACCESS_COARSE_LOCATION, "" //options = quickPermissionsOption
    ) {
        Toast.makeText(this, "permissions granted", Toast.LENGTH_LONG).show()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }


    //define the listener
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            texto.text = ("" + location.longitude + ":" + location.latitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }



}