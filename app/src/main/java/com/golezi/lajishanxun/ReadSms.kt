package com.golezi.lajishanxun

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log

import android.net.Uri
import java.util.*

import android.database.ContentObserver;
import android.os.Handler
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class ReadSms(private val context: Application) : ContentObserver(Handler()) {


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
        Log.d("sos", "read  is " + c.getString(c.getColumnIndexOrThrow("read")))

        c.moveToNext()

        c.close()
        Log.i("lajishanxun", "$smsDate\n$address\n$body")

        // 密码是否过期
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val now = simpleDateFormat.format(Date())
        val regExp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"
        val messageBody = "尊敬的闪讯用户，您的宽带上网密码是：149186,密码在2019-03-18 12:35:06以前有效"

        val p = Pattern.compile(regExp)
        val m = p.matcher(messageBody)
        m.find()
        val expireDate = m.group()
        println(expireDate)
        var isExpire = "状态:"
        isExpire += if (now > expireDate) {
            "已过期"
        } else {
            "未过期"
        }

        return "最近收到的闪讯密码短信:\n\n$body\n\n$isExpire"

    }
}