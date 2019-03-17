package com.golezi.lajishanxun


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import android.widget.TextView
import android.os.Build
import java.io.Serializable


import java.util.regex.Pattern


class Message(val number: String, val content: String) : Serializable {
    companion object {
        const val serialVersionUID = 3L
    }

    var code: String = ""
}


@Suppress("DEPRECATION")
class SmsReceiver : BroadcastReceiver() {

    private val TAG = "lajishanxun"
    private var smsContent = "smsContent"

    var smsContentTextView: TextView? = null
        set(value) {
            field = value
            field!!.text = smsContent
            Log.i(TAG, "设置短信内容 TextView成功")
        }

    var shanXunTextView: TextView? = null
        set(value) {
            field = value
            Log.i(TAG, "设置闪讯信息 TextView成功")
        }


    init {
        Log.i(TAG, "载入成功")
    }


    override fun onReceive(context: Context, intent: Intent) {

        smsContentTextView?.text = "收到短信啦"
        val message = parseMessage(
                intent.extras["pdus"] as Array<*>?,
                intent.getStringExtra("format")) ?: return
        //Log.i(TAG, "短信内容: ${message.content}")

        val showText = "收到短信啦\n短信内容: ${message.content}"
        smsContentTextView?.text = showText

        if (message.content.indexOf("闪讯") > 0) {
            Log.i(TAG, "收到闪讯短信")
            val passwordPattern = "宽带上网密码是：\\d{6}"
            val expireTimePattern = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"
            val passwordMatcher = Pattern.compile(passwordPattern).matcher(message.content)
            val expireTimeMatcher = Pattern.compile(expireTimePattern).matcher(message.content)
            if (passwordMatcher.find() and expireTimeMatcher.find()) {
                val passwd = passwordMatcher.group()
                Log.i(TAG, passwd)
                val expireTime = expireTimeMatcher.group()
                val t = "$passwd\n过期时间是：$expireTime"
                shanXunTextView?.text = t
            } else {
                Log.i(TAG, "短信内容查找不到闪讯密码或过期时间")
                shanXunTextView?.text = "短信内容查找不到闪讯密码或过期时间"
            }
        }


    }


    private fun parseMessage(pdus: Array<*>?, format: String): Message? {
        if (pdus == null) {
            return null
        }
        val messages = pdus.map {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SmsMessage.createFromPdu(it as ByteArray, format)
            } else {
                SmsMessage.createFromPdu(it as ByteArray)
            }
        }
        //获得发送人
        val number = messages[0].originatingAddress
        val content = messages.joinToString { it.messageBody }

        Log.i(TAG, "发送人: $number \n 内容: $content")

        return Message(number, content)
    }
}

