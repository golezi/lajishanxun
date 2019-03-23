package com.golezi.lajishanxun

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.IntentFilter


import android.telephony.SmsManager
import android.net.Uri
import android.widget.Button
import android.widget.TextView

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

        val readSms = ReadSms(application)
        this.contentResolver.registerContentObserver(
                Uri.parse("content://sms/"), true, readSms);
        findViewById<TextView>(R.id.shanXunTextView).text = readSms.getMessageOfShanxun()
        val titleText = if (readSms.isExpire) {
            "$title 密码已过期"
        } else {
            "$title 密码未过期"
        }
        title = titleText


        val sendSmsButton: Button = findViewById(R.id.sendSmsButton)
        sendSmsButton.setOnClickListener {
            val manager = SmsManager.getDefault()
            manager.sendTextMessage("1065930051", null, "mm", null, null);
            sendSmsButton.text = "已发送"
        }
    }

}
