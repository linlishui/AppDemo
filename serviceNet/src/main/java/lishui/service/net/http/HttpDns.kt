package lishui.service.net.http

import android.text.TextUtils
import lishui.lib.base.log.LogUtils
import lishui.service.net.NetCommonConfigs.NET_DEBUG
import lishui.service.net.NetCommonConfigs.NET_TAG
import okhttp3.Dns
import java.net.InetAddress

/**
 * @author lishui.lin
 * Created it on 2021/7/28
 */
class HttpDns : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        val inetAddress = InetAddress.getByName(hostname)
        val ip = inetAddress.hostAddress
        if (NET_DEBUG) LogUtils.d(NET_TAG,"HttpDns host name=$hostname, host ip=${ip}")
        return if (!TextUtils.isEmpty(ip)) {
            InetAddress.getAllByName(ip).toList()
        } else {
            Dns.SYSTEM.lookup(hostname)
        }
    }
}