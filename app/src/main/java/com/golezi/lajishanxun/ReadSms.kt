package com.golezi.lajishanxun

import android.app.Application
import android.util.Log
import android.net.Uri
import java.util.*
import android.database.ContentObserver
import android.os.Handler
import java.text.SimpleDateFormat
import java.util.regex.Pattern

class ReadSms(private val context: Application) : ContentObserver(Handler()) {

    var isExpire= false

    fun getMessageOfShanxun(): String {

        val message = Uri.parse("content://sms/inbox")

        // val c = context.contentResolver.query(message, null, null, null, null)
        val c =
                context.contentResolver.query(message, null, "address like '106593005'", null, "date DESC limit 1")

        val totalSMS = c!!.count
        Log.i("lajishanxun", "totalSMS is $totalSMS")
        if (totalSMS == 0) {
            return "无30小时内短信记录"
        }
        c.moveToFirst()

        Log.d("sos", "date  is " + (c.getString(c.getColumnIndexOrThrow("date"))))
        val timestamp = java.lang.Long.parseLong(c.getString(c.getColumnIndexOrThrow("date")))
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp
        val finaldate = calendar.time
        Log.i("lajishanxun", "finaldate is $finaldate")
        val smsDate = finaldate.toString()
//        message.date = smsDate
        val address = c.getString(c.getColumnIndexOrThrow("address"))
        val body = c.getString(c.getColumnIndexOrThrow("body"))
        Log.d("lajishanxun", "read  is " + c.getString(c.getColumnIndexOrThrow("read")))

        c.moveToNext()

        c.close()
        Log.i("lajishanxun", "$smsDate\n$address\n$body")

        // 密码是否过期
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        val now = simpleDateFormat.format(Date())
        val regExp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"

        val p = Pattern.compile(regExp)
        val m = p.matcher(body)
        m.find()
        val expireDate = m.group()

        this.isExpire = now > expireDate


        Log.i("lajishanxun", "过期时间$expireDate")

        return "当前时间:$now\n\n过期时间:$expireDate\n\n最近收到的闪讯密码短信:\n\n$body"

    }
}