package com.golezi.lajishanxun

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.IntentFilter


import android.telephony.SmsManager
import android.util.Log


import android.widget.Button


class MainActivity : AppCompatActivity() {
    private var mSmsReceiver: SmsReceiver? = null

    @SuppressLint("UnlocalizedSms")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val receiveFilter = IntentFilter()
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        receiveFilter.priority = 1000

        mSmsReceiver = SmsReceiver()
        mSmsReceiver?.smsContentTextView = findViewById(R.id.SmsContentTextView)
        mSmsReceiver?.shanXunTextView = findViewById(R.id.shanXunTextView)

        registerReceiver(mSmsReceiver, receiveFilter)


        val sendSmsButton: Button = findViewById(R.id.sendSmsButton)
        sendSmsButton.setOnClickListener {
            val manager = SmsManager.getDefault()
            manager.sendTextMessage("1065930051", null, "mm", null, null);
            sendSmsButton.text = "已发送"
        }
    }

}
