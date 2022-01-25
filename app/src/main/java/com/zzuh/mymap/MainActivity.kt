package com.zzuh.mymap

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.naver.maps.map.overlay.Marker
import com.zzuh.mymap.databinding.ActivityMainBinding
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.zzuh.mymap.MessageCode.CAL_PATHS
import com.zzuh.mymap.MessageCode.CAL_SERVICE_DONE
import com.zzuh.mymap.MessageCode.CONNECT_OK
import com.zzuh.mymap.MessageCode.GET_PATHS
import com.zzuh.mymap.MessageCode.GET_SERVICE_DONE
import com.zzuh.mymap.MessageCode.READY_SERVICE
import com.zzuh.mymap.databinding.LoadingDialogBinding
import kotlinx.coroutines.delay
import org.w3c.dom.Text

var CLIENT_ID = ""
var CLIENT_SECRET = ""
var BASE_URL_NAVER_API = ""

var markerData = mutableListOf<Marker>()
var addressData = mutableListOf<String>()

object MessageCode {
    const val READY_SERVICE = 10
    const val GET_SERVICE_DONE = 20
    const val CAL_SERVICE_DONE = 30
    const val CONNECT_OK = 100
    const val GET_PATHS = 200
    const val CAL_PATHS = 300
}

class MainActivity : AppCompatActivity() {

    lateinit var dialog: Dialog
    lateinit var loadingTextView: TextView
    lateinit var binding: ActivityMainBinding

    // Messenger
    lateinit var messenger: Messenger
    lateinit var replyMessenger: Messenger

    lateinit var transaction: FragmentTransaction
    lateinit var fragmentManager: FragmentManager
    lateinit var mapsFragment: MapsFragment
    lateinit var bottomSheetFragment: BottomSheetFragment

    var notifyCallback: () -> Unit = {}
    var searchButtonClick: Boolean = true

    //messenger........................
    inner class HandlerReplyMsg : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                READY_SERVICE -> {
                    Log.d("tester Activity", "Receive code READY_SERVICE")
                    val msg = Message()
                    msg.what = GET_PATHS

                    messenger.send(msg)
                    loadingTextView.text = "데이터를 불러오고 있습니다..."
                }
                GET_SERVICE_DONE -> {
                    Log.d("tester Activity", "Receive code GET_SERVICE_DONE")

                    val msg = Message()
                    msg.what = CAL_PATHS

                    messenger.send(msg)
                    loadingTextView.text = "가장 최적의 경로를 찾고 있습니다..."
                }
                CAL_SERVICE_DONE -> {
                    Log.d("tester Activity", "Receive code CAL_SERVICE_DONE")
                    unbindService(connection)
                    loadingTextView.text = "처리가 완료되었습니다!"
                    dialog.dismiss()
                }
                else -> super.handleMessage(msg)
            }
        }
    }
    val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            messenger = Messenger(service)

            val msg = Message()
            msg.what = CONNECT_OK
            msg.replyTo = replyMessenger

            messenger.send(msg)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        replyMessenger = Messenger(HandlerReplyMsg())

        // Naver api id, password 저장
        CLIENT_ID = getString(R.string.client_id)
        CLIENT_SECRET = getString(R.string.client_secret)
        BASE_URL_NAVER_API = getString(R.string.baseUrl)

        // Loading progress bar build
        var builder = AlertDialog.Builder(this);
        builder.setView(R.layout.loading_dialog)
        builder.setCancelable(true)
        dialog = builder.create()
        loadingTextView = LoadingDialogBinding.inflate(layoutInflater).loadingMsg

        // Fragment 관리
        mapsFragment = MapsFragment()
        bottomSheetFragment = BottomSheetFragment()

        fragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()

        transaction.add(R.id.maps_fragment, mapsFragment)
        transaction.add(R.id.bottom_sheet_fragment, bottomSheetFragment)
        transaction.commit()

        // Search Button 구성
        binding.searchButton.setOnClickListener {
            Log.d("Tester", "click the Search button")
            if(searchButtonClick) {
                // get current position
                if(markerData.size == 0){
                    Toast.makeText(this, "error",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                mapsFragment.getCurrentLanLong()

                // Loading dialog
                searchButtonClick = false
                dialog.show()

                loadingTextView.text = "Service 연결중..."
                val intent = Intent(this, GetPathService::class.java)
                bindService(intent, connection, Context.BIND_AUTO_CREATE)

                binding.searchButton.text = "초기화"
            }
            else{
                // Data 초기화
                addressData.clear()
                for(marker in markerData)
                    marker.map = null
                markerData.clear()
                notifyCallback()
                binding.searchButton.text = "검색"
                searchButtonClick = true
            }
        }

        setContentView(binding.root)
    }
}